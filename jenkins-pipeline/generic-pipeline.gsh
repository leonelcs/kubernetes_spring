
// The name of the generic Helm + Rancher image that we'll create/use to execute rancher commands
rancherImage = "helmrancher"

// The name of the rancher credentials JSON stored in Jenkins
rancherCredentialsId = "sys_single_shop"

artifactoryHost = "artifacts.kpn.org"

// The GIT url + credentialsID
gitUrl = "ssh://git@git.kpn.org:7999/ssp/single-shop.git"
gitCredentialsId = "5def9162-43f8-4607-b899-039067040060"

// The module and specific branch to deploy, as supplied by the user when triggering the Jenkins build
moduleName = params.MODULE_NAME
branchName = params.BRANCH

// The following values will override whatever is in the values.yaml of a module:
namespace = params.NAMESPACE
// Deduce the environment from the namespace
environment = namespace.replaceFirst("singleshop-", "")
ingressSuffix = (environment == "prod") ? "" : "-${environment}"
tcloudDomain = "main.tcloud.kpn.org"
configMap = "singleshop-config"
artifactoryUrl = "${artifactoryHost}/ecommerce"


node {
    def rtGradle = Artifactory.newGradleBuild()
    def server = Artifactory.server(artifactoryHost)

    stage("Prepare Build Environment") {
        rtGradle.tool = 'GRD' // Tool name from Jenkins configuration
        rtGradle.deployer repo: 'libs-snapshot-local', server: server
        rtGradle.resolver repo: 'libs-snapshot', server: server

    }
    stage("Checkout") {
        git(url: gitUrl, branch: branchName, credentialsId: gitCredentialsId)
    }

    def dockerDirectory
    if (fileExists("${moduleName}/Dockerfile")) { // is this a (db-)module with a static Dockerfile in its root ?
        dockerDirectory = "./${moduleName}"
    } else if (fileExists("${moduleName}/build.gradle")) {  // is this a Java+Gradle build ?
        dockerDirectory = "./${moduleName}/build/docker"
        def buildInfo
        stage('Gradle Build') {
            rtGradle.usesPlugin = true
            buildInfo = rtGradle.run rootDir: moduleName, buildFile: 'build.gradle', tasks: 'clean build buildDocker artifactoryPublish'
        }
        stage('Push Buildinfo') {
            server.publishBuildInfo buildInfo
        }
    } else {    // is this neither, i.e. the config-map module ?
        // nop
    }

    if (dockerDirectory != null) {
        def dockerImage
        stage("DockerBuild-${moduleName}") {
            dir('./') {
                dockerImage = docker.build("${artifactoryUrl}/${moduleName}", "-f ${dockerDirectory}/Dockerfile ${dockerDirectory}")
            }
        }
        docker.withRegistry("https://${artifactoryUrl}/${moduleName}", artifactoryHost) {
            stage("Push image: ${moduleName}") {
                dockerImage.push("latest")
            }
        }
    }

    stage("Deploy ${moduleName} with Rancher to namespace ${namespace}") {
        def tag = "latest"

        deploy(moduleName, tag)
        println("Deployed module $moduleName to namespace $namespace")
    }
}

void deploy(moduleName, tag) {
    ensureRancherImageExists(rancherImage)
    dir(moduleName) {
        k8s(moduleName, """
            helm template ./helm-chart/ \
                --set version=${tag} \
                --set namespace=${namespace} \
                --set environment=${environment} \
                --set ingressSuffix=${ingressSuffix} \
                --set tcloudDomain=${tcloudDomain} \
                --set configMap=${configMap} \
                --set artifactoryUrl=${artifactoryUrl} \
                 > deployment.yaml && \
            cat deployment.yaml && \
            rancher kubectl apply -f deployment.yaml --namespace=${namespace}
            """.stripIndent(), rancherCredentialsId)
    }
}

void ensureRancherImageExists(rancherImage) {
    println(sh("docker images -q ${rancherImage} | grep . || docker build . -f ./jenkins-pipeline/generic-DockerfileRancher -t ${rancherImage}"))
}

String k8s(moduleName, cmd, rancherCredentialsId) {
    withCredentials([file(credentialsId: rancherCredentialsId, variable: 'RCLI_CONFIG')]) {
        def response = sh(
                script: "docker run --rm \
                                    -v ${RCLI_CONFIG}:/root/.rancher/cli2.json \
                                    -v ${env.WORKSPACE}/${moduleName}/k8s:/k8s \
                                    ${rancherImage} '${cmd}' 2>&1 || true",
                returnStdout: true,
        ).trim()

        if (response && (response.contains('level=') || response.contains('FATA=') || response.toLowerCase().contains('error'))) {
            error("Error occurred while running rancher command: ${response}")
        } else {
            println("Rancher command result: ${response}")
        }
        return response
    }
}

