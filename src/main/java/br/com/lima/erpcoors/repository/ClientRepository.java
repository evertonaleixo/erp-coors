package br.com.lima.erpcoors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lima.erpcoors.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	
	@Query("SELECT p FROM Client p WHERE LOWER(p.name) LIKE CONCAT('%',LOWER(:name),'%')"
			+ "")
    public List<Client> customQuery(@Param("name") String name);
}
