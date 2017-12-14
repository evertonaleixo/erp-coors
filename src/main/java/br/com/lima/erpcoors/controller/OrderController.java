package br.com.lima.erpcoors.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.Order;
import br.com.lima.erpcoors.model.OrderDTO;
import br.com.lima.erpcoors.model.Product;
import br.com.lima.erpcoors.repository.ClientRepository;
import br.com.lima.erpcoors.repository.OrderRepository;
import br.com.lima.erpcoors.repository.ProductRepository;

@Controller
@RequestMapping("/orcamentos")
public class OrderController {
	@Autowired
	private OrderRepository orders;

	@Autowired
	private ProductRepository products;

	@Autowired
	private ClientRepository clients;

	@GetMapping("/listar")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("orcamentos");

		List<OrderDTO> list = new ArrayList<>();
		List<Order> findAll = orders.findAll();
		for (Order o : findAll) {
			list.add(new OrderDTO(o));
		}

		mav.addObject("orders", list);

		return mav;
	}

	@GetMapping("/{id}")
	public ModelAndView viewOrder(@PathVariable("id") Long id, BindingResult bindingResult) {
		OrderDTO one = new OrderDTO(orders.findOne(id));

		return getFicha(one, bindingResult);
	}

	@PostMapping(value = "/novo")
	public ModelAndView newOrder(OrderDTO orderDTO, BindingResult bindingResult) {
		Order order = orderDTO.getAsOrder();

		if (order.getId() < 0) {
			order.setId(0L);
		}

		// TODO: Throw exception
		if (clients.exists(order.getClient().getId())) {

		}
		else { // TODO: REMOVE THIS ELSE BLOCK
			order.setClient(clients.save(order.getClient()));
			Map<Product, Double> map = new HashMap<>();
			for(Entry<Product, Double> en: order.getProducts().entrySet()) {
				if(!products.exists(en.getKey().getId())) {
					Product save = products.save(en.getKey());
					map.put(save, en.getValue());
				} else {
					map.put(en.getKey(), en.getValue());
				}
			}
			order.setProducts(map);
		}
		
		if (order.getCreated() == null) {
			order.setCreated(new Date(System.currentTimeMillis()));
		}

		order = orders.save(order);

		return list();
	}

	@PostMapping(value = "/novo", params = { "newItem", "prod_add_id", "prod_qnt" })
	public ModelAndView addItem(OrderDTO order, @RequestParam(value = "prod_add_id") long idProd,
			@RequestParam(value = "prod_qnt") BigDecimal qnt, BindingResult bindingResult) {

		Product product = products.getOne(idProd);

		try {
			product.getId();
		} catch (Exception e) {
			product = new Product();
			product.setCod("cal");
			product.setId(idProd);
			product.setDescription("Calcio");
			product.setUnit("Cx");
			product.setValue(new BigDecimal(45.3));
		}

		int index = -1;
		for (int i = 0; i < order.getProds().size(); i++) {
			if (order.getProds().get(i).getId() == product.getId()) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			order.getProds().add(product);
			order.getQnts().add(qnt.doubleValue());
		} else {
			double count = (order.getQnts().get(index) + qnt.doubleValue());
			if(count == 0f) {
				order.getProds().remove(index);
			} else {
				order.getQnts().set(index, count);
			}
		}
		
		// Calculate prices
		BigDecimal valueToAdd = product.getValue().multiply(qnt);
		
		order.setValue(order.getValue().add(valueToAdd));
		order.setFullValue(order.getFullValue().add(valueToAdd));
		order.setTotalItens(order.getProds().size());
		
		return getFicha(order, bindingResult);
	}
	
	@PostMapping(value = "/novo", params = { "rmProd" })
	public ModelAndView rmItem(OrderDTO order, @RequestParam(value = "rmProd") long idProd, BindingResult bindingResult) {
		int index = -1;
		for (int i = 0; i < order.getProds().size(); i++) {
			if (order.getProds().get(i).getId() == idProd) {
				index = i;
				break;
			}
		}

		
		if (index == -1) {
			return getFicha(order, bindingResult);
		}
		
		// Calculate prices
		Product product = order.getProds().remove(index);
		Double qnt = -order.getQnts().remove(index);
		BigDecimal valueToAdd = product.getValue().multiply(new BigDecimal(qnt));
		
		order.setValue(order.getValue().add(valueToAdd));
		order.setFullValue(order.getFullValue().add(valueToAdd));
		order.setTotalItens(order.getProds().size());
		
		return getFicha(order, bindingResult);
	}

	@GetMapping("/novo")
	public ModelAndView getFicha(OrderDTO order, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView("orcamentos_ficha");
		mav.addObject("order", order);

		return mav;
	}

	@GetMapping("/del/{id}")
	public ModelAndView deleteClient(@PathVariable("id") Long id) {
		Order one = orders.findOne(id);

		if (one != null && one.getId() != 0)
			orders.delete(one);

		return list();
	}
}
