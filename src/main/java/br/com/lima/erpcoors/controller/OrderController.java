package br.com.lima.erpcoors.controller;

import java.math.BigDecimal;

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
import br.com.lima.erpcoors.model.Product;
import br.com.lima.erpcoors.repository.OrderRepository;
import br.com.lima.erpcoors.repository.ProductRepository;

@Controller
@RequestMapping("/orcamentos")
public class OrderController {
	@Autowired
	private OrderRepository orders;
	
	@Autowired
	private ProductRepository products;

	@GetMapping("/listar")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("orcamentos");
		mav.addObject("orders", orders.findAll());

		return mav;
	}

	@GetMapping("/{id}")
	public ModelAndView viewOrder(@PathVariable("id") Long id) {
		Order one = orders.findOne(id);

		return getFicha(one);
	}

	@PostMapping(value = "/novo")
	public ModelAndView newOrder(Order order) {
		if (order.getId() < 0)
			order.setId(0L);

		// order = orders.save(order);

		return list();
	}

	@PostMapping(value = "/novo", params = {"newItem", "prod_add_id", "prod_qnt"})
	public ModelAndView addItem(Order order, @RequestParam(value = "prod_add_id") long idProd, @RequestParam(value = "prod_qnt") int qnt, BindingResult bindingResult) {
		
		System.out.println("aki");
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
		
		order.getProducts().add(product);
		

		return getFicha(order);
	}

	@GetMapping("/novo")
	public ModelAndView getFicha(Order order) {
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
