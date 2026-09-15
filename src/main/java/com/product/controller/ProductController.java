package com.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.domain.Product;
import com.product.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// GET /api/products
	@GetMapping
	public List<Product> all() {
		return productService.findAll();
	}

	// GET /api/products/{id}
	@GetMapping("/{id}")
	public ResponseEntity<Product> getById(@PathVariable Long id) {
		Product p = productService.findById(id);
		return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
	}

	// POST /api/products
	@PostMapping
	public ResponseEntity<Product> create(@RequestBody Product p) {
		Product created = productService.save(p);
		return ResponseEntity.ok(created);
	}

	// PUT /api/products/{id}
	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product p) {
		if (productService.findById(id) == null) {
			return ResponseEntity.notFound().build();
		}
		p.setId(id);
		return ResponseEntity.ok(productService.save(p));
	}

	// DELETE /api/products/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		boolean deleted = productService.deleteById(id);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}
