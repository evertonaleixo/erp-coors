package br.com.lima.erpcoors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lima.erpcoors.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE LOWER(p.description) LIKE CONCAT('%',LOWER(:name),'%') OR LOWER(p.cod) LIKE CONCAT('%',LOWER(:name),'%')")
    public List<Product> customQuery(@Param("name") String name);
}
