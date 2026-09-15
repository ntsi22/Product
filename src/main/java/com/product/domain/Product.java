package com.product.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Entity
public class Product {
	@Id
	 private Long id;
	 private String name;
	 private double price;

}
