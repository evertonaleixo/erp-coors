package br.com.lima.erpcoors.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="order_table")
public class Order {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private long id;

	@NotNull 
    @Temporal(TemporalType.TIMESTAMP) 
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date created;

	@OneToOne
	private Client client;

	@ElementCollection
	@CollectionTable(name="prods_in_order")
	@MapKeyJoinColumn(name="id")
	@Column(name="prod")
	private Map<Product, Double> products;

	private BigDecimal discount;

	private BigDecimal fullValue;

	private BigDecimal value;
	
	public Order() {
		products = new HashMap<>();
		this.discount = new BigDecimal(0f);
		this.fullValue = new BigDecimal(0f);
		this.value = new BigDecimal(0f);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Map<Product, Double> getProducts() {
		return products;
	}

	public void setProducts(Map<Product, Double> products) {
		this.products = products;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getFullValue() {
		return fullValue;
	}

	public void setFullValue(BigDecimal fullValue) {
		this.fullValue = fullValue;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
