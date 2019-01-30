# This is an image for a neo4j container that already has APOC installed,
# and that before startup removes any pre-existing username/password info
# contained in the 'auth' file in the mounted database volume, so that you
# can always inject a new (or the same) username/password combo via either the
# standard NEO4J_AUTH environment variable, or via the ${neo4j_user} and ${neo4j_password}
# environment variables that we have setup as Kubernetes cluster secrets.

ARG neo4j_version=3.4.9
ARG neo4j_image=neo4j:$neo4j_version

# Using hardcoded image specification to workaround a bug where Kubernetes doesn't seem to
# support using ARGs in a FROM statement ?
FROM neo4j:3.4.9

ARG apoc_version=3.4.0.3

MAINTAINER single@shop.com

#expose neo4j ports
EXPOSE 7474 7473 7687

# Put the APOC plugin into neo4j's plugins directory
ADD https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/${apoc_version}/apoc-${apoc_version}-all.jar /var/lib/neo4j/plugins
RUN chown neo4j:neo4j /var/lib/neo4j/plugins/apoc-${apoc_version}-all.jar

# Ensure that APOC can be used instead of running into a security exception
ENV NEO4J_dbms_security_procedures_unrestricted=apoc.\\\*

# TODO find out if there's a way to determine the uid:gid related to neo4j:neo4j programmatically
# instead of assuming they're always the same value ?
USER 100:101

# Not using '/sbin/tini' as in the original neo4j community docker image, as
# 1) tini requires being run as root, and Tcloud won't allow that, and
# 2) tini isn't really needed any more, as Kubernetes is supposed to be doing reaping of
#    zombie-processes already by itself on a namespace level.
ENTRYPOINT ["/bin/sh", "-c", "rm -f /data/dbms/auth && \
                              export NEO4J_AUTH=${NEO4J_AUTH:-$neo4j_user/$neo4j_password} && \
                              exec /docker-entrypoint.sh neo4j" ]
