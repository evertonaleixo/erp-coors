package br.com.lima.erpcoors.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class OrderDTO extends Order {
	private List<Product> prods = new ArrayList<>();
	private List<Double> qnts = new ArrayList<>();
	private int totalItens = 0;

	public OrderDTO() {
		setDiscount(new BigDecimal(0f));
		setFullValue(new BigDecimal(0f));
		setValue(new BigDecimal(0f));
	}

	public OrderDTO(Order findOne) {
		this.setId(findOne.getId());
		this.setClient(findOne.getClient());
		this.setCreated(findOne.getCreated());
		this.setDiscount(findOne.getDiscount());
		this.setFullValue(findOne.getFullValue());

		for (Entry<Product, Double> en : findOne.getProducts().entrySet()) {
			prods.add(en.getKey());
			qnts.add(en.getValue().doubleValue());
		}

		this.setValue(findOne.getValue());
	}

	public List<Product> getProds() {
		return prods;
	}

	public void setProds(List<Product> prods) {
		this.prods = prods;
	}

	public List<Double> getQnts() {
		return qnts;
	}

	public void setQnts(List<Double> qnts) {
		this.qnts = qnts;
	}

	public Order getAsOrder() {
		Order o = new Order();

		o.setId(this.getId());
		o.setClient(this.getClient());
		o.setCreated(this.getCreated());
		o.setDiscount(this.getDiscount());
		BigDecimal fullValue = new BigDecimal(0);

		// Fill products
		for (int i = 0; i < this.getProds().size(); i++) {
			Product prod = this.getProds().get(i);
			Double qnt = this.getQnts().get(i);
			o.getProducts().put(prod, qnt);

			BigDecimal valProd = prod.getValue().multiply(new BigDecimal(qnt));
			fullValue = fullValue.add(valProd);
		}

		o.setFullValue(fullValue);
		o.setValue(o.getFullValue().subtract(o.getDiscount()));
		return o;
	}

	public int getTotalItens() {
		return totalItens;
	}

	public void setTotalItens(int totalItens) {
		this.totalItens = totalItens;
	}

}
