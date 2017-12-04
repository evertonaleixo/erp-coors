package br.com.lima.erpcoors.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Client {
	@Id
	private long id;
	
	private String cpf_cnpj;
	private String name;
	@ElementCollection
	@CollectionTable(name = "phone")
	private List<String> phones;
	private String address;
	
	@OneToMany
	private List<Order> orders;
}
