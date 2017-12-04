package br.com.lima.erpcoors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lima.erpcoors.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
