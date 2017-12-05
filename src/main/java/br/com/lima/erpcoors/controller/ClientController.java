package br.com.lima.erpcoors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.Client;
import br.com.lima.erpcoors.repository.ClientRepository;

@Controller
@RequestMapping("/clientes")
public class ClientController {
	
	@Autowired
	ClientRepository clients;
	
	@GetMapping("/listar")
	public ModelAndView clientes() {
		ModelAndView mav = new ModelAndView("clientes");
		mav.addObject("clients", clients.findAll());
		
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView updateClient(@PathVariable("id") Long id) {
		Client one = clients.findOne(id);
		
		return getFicha(one);
	}
	
	@GetMapping("/del/{id}")
	public ModelAndView deleteClient(@PathVariable("id") Long id) {
		Client one = clients.findOne(id);
		
		if(one != null && one.getId() != 0)
			clients.delete(one);
		
		return clientes();
	}
	
	@GetMapping("/novo")
	public ModelAndView getFicha(Client client) {
		ModelAndView mav = new ModelAndView("clientes_ficha");
		mav.addObject("client", client);
		
		return mav;
	}
	
	@PostMapping("/novo")
	public ModelAndView newClient(Client client) {
		client = clients.saveAndFlush(client);
		
		return clientes();
	}
	
}
