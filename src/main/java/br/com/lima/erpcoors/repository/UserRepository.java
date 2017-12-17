package br.com.lima.erpcoors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lima.erpcoors.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
