package br.com.lima.erpcoors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lima.erpcoors.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

}
