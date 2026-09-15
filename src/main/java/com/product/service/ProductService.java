package com.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.product.domain.*;

@Service
public class ProductService {

	  private final Map<Long, Product> products = new HashMap<>();
	  private final AtomicLong counter = new AtomicLong();

	  public ProductService() {

	    // Données de test
	    save( new Product(null, "Clavier", 25.5));
	    save(new Product(null, "Souris", 15.0));
	  }

	  public List<Product> findAll() {
	    return new ArrayList<>(products.values());
	  }

	  public Product findById(Long id) {
	    return products.get(id);
	  }

	  public Product save(Product p) {
	    long id = (p.getId() == null) ? counter.incrementAndGet() : p.getId();
	    Product saved = new Product(id, p.getName(), p.getPrice());
	    products.put(id, saved);
	    return saved;
	  }

	  public boolean deleteById(Long id) {
	    return products.remove(id) != null;
	  }
}
