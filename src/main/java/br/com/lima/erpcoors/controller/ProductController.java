package br.com.lima.erpcoors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.Product;
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

	@GetMapping("/{id}")
	public ModelAndView updateClient(@PathVariable("id") Long id) {
		Product one = products.findOne(id);

		return getFicha(one);
	}

	@PostMapping(value = "/novo", params = { "newProd" })
	public ModelAndView newClient(Product product) {
		if (product.getId() < 0)
			product.setId(0);

		product = products.save(product);

		return produtos();
	}

	@GetMapping("/novo")
	public ModelAndView getFicha(Product product) {
		ModelAndView mav = new ModelAndView("produtos_ficha");
		mav.addObject("product", product);

		return mav;
	}

	@GetMapping("/del/{id}")
	public ModelAndView deleteClient(@PathVariable("id") Long id) {
		Product one = products.findOne(id);

		if (one != null && one.getId() != 0)
			products.delete(one);

		return produtos();
	}
}
