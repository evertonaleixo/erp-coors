package br.com.lima.erpcoors.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.Order;
import br.com.lima.erpcoors.repository.OrderRepository;

@Controller
@RequestMapping("/fluxo")
public class FluxController {
	@Autowired
	private OrderRepository orders;

	@GetMapping("/consulta")
	public ModelAndView query() {
		ModelAndView mav = new ModelAndView("fluxo");
		List<Order> findAll = orders.findAll();
		
		mav.addObject("orders", findAll);

		return mav;
	}
	
	@GetMapping(value="/ver", params = { "view" })
	public ModelAndView show(@RequestParam(value = "data_ini") Date dateIni, @RequestParam(value = "data_end") Date dateEnd) {
		ModelAndView mav = new ModelAndView("fluxo");
		List<Order> findAll = orders.findAll();

		mav.addObject("orders", findAll);

		return mav;
	}
	
	@GetMapping(value="/ver", params = { "print" })
	public ModelAndView print(@RequestParam(value = "data_ini") Date dateIni, @RequestParam(value = "data_end") Date dateEnd) {
		ModelAndView mav = new ModelAndView("fluxo");
		List<Order> findAll = orders.findAll();

		mav.addObject("orders", findAll);

		return mav;
	}
}
