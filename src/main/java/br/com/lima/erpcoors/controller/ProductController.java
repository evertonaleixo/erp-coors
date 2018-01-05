package br.com.lima.erpcoors.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.Product;
import br.com.lima.erpcoors.repository.ProductRepository;

@Controller
@RequestMapping("/produtos")
public class ProductController {
	@Autowired
	private ProductRepository products;
	
	@GetMapping(path = "/filtrar", produces= {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Product> filterSugestion(@RequestParam String phrase) {
		List<Product> filtered = products.customQuery(phrase);
		
		return filtered;
	}

	@GetMapping("/listar")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("produtos");
		mav.addObject("products", products.findAll());

		return mav;
	}
	
	@GetMapping(value="/listar", params = {"filter"})
	public ModelAndView listFiltered(@RequestParam String filter) {
		filter = filter.trim();
		if(filter.isEmpty())
			return list();
		
		ModelAndView mav = new ModelAndView("produtos");
		List<Product> filtered = products.customQuery(filter);
		
		mav.addObject("products", filtered);
		
		return mav;
	}

	@GetMapping("/{id}")
	public ModelAndView updateProduct(@PathVariable("id") Long id) {
		Product one = products.findOne(id);

		return getFicha(one);
	}

	@PostMapping(value = "/novo", params = { "newProd" })
	public ModelAndView newProduct(Product product) {
		if (product.getId() < 0)
			product.setId(0);

		product = products.save(product);

		return list();
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

		return list();
	}
}
