package br.com.lima.erpcoors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.Order;
import br.com.lima.erpcoors.repository.OrderRepository;

@Controller
@RequestMapping("/orcamentos")
public class OrderController {
	@Autowired
	private OrderRepository orders;

	@GetMapping("/listar")
	public ModelAndView produtos() {
		ModelAndView mav = new ModelAndView("orcamentos");
		mav.addObject("orders", orders.findAll());

		return mav;
	}

	@GetMapping("/{id}")
	public ModelAndView updateClient(@PathVariable("id") Long id) {
		Order one = orders.findOne(id);

		return getFicha(one);
	}

	@PostMapping(value = "/novo", params = { "newProd" })
	public ModelAndView newClient(Order order) {
		if (order.getId() < 0)
			order.setId(0L);

		order = orders.save(order);

		return produtos();
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

		return produtos();
	}
}
