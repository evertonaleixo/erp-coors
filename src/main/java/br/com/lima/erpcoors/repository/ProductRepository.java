package br.com.lima.erpcoors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lima.erpcoors.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
