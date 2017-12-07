package br.com.lima.erpcoors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.repository.ProductRepository;

@Controller
@RequestMapping("/produtos")
public class ProductController {
	@Autowired
	private ProductRepository products;
	
	@GetMapping("/listar")
	public ModelAndView produtos() {
		ModelAndView mav = new ModelAndView("produtos");
		mav.addObject("products", products.findAll());
		
		return mav;
	}
}
