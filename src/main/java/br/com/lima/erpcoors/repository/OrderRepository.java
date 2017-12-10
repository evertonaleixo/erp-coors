package br.com.lima.erpcoors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lima.erpcoors.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
