package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import lombok.Data;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "contractduration")
@CompositeIndex(properties = { "id", "name" })
@Data
public class ContractDuration {

	// ToDo : should be a relation ship
	@Id
	@GeneratedValue
	private Long _id;
	@Index(unique = true)
	private String id;
	private String name;
}
