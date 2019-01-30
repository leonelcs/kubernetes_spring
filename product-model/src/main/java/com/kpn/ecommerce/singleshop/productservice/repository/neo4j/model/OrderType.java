package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "ordertype")
@CompositeIndex(properties = { "id", "name" })
public class OrderType {

	@Id
	@GeneratedValue
	private Long _id;

	@Index(unique = true)
	private String id;

	@Index(unique = true)
	private String name;
}
