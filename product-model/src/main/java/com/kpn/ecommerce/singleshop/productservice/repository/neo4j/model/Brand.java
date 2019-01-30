package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.constants.RelationShipConstants;
import java.util.HashSet;
import java.util.Set;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "brand")
public class Brand {
	@Id
	@GeneratedValue
	private Long id;
	@Index(unique = true)
	private String name;
	@Relationship(type = RelationShipConstants.OWNED_BY, direction = Relationship.INCOMING)
	private Set<Subscription> subscriptions = new HashSet<>();

}