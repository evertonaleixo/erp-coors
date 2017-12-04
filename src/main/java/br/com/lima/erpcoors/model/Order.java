package br.com.lima.erpcoors.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Order {

	@Id
	private Long id;
	
	private Date createdAt;
	
}
