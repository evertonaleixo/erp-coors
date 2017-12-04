package br.com.lima.erpcoors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClientController {
	
	@GetMapping("/clientes")
	public ModelAndView clientes() {
		ModelAndView mav = new ModelAndView("clientes");

		return mav;
	}
	
	
}
