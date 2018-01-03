package br.com.lima.erpcoors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lima.erpcoors.model.UserRoles;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

}
