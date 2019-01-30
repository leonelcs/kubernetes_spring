package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "freeunit")
@CompositeIndex(properties = { "id", "type", "unit" })
public class FreeUnit {

	@Id
	@GeneratedValue
	private Long _id;
	@Index(unique = true)
	private String id;
	private int amount;
	private String type;
	private String unit;
}
