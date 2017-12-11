package br.com.lima.erpcoors.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

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

import br.com.lima.erpcoors.model.Client;
import br.com.lima.erpcoors.model.Phone;
import br.com.lima.erpcoors.repository.ClientRepository;
import br.com.lima.erpcoors.repository.PhoneRepository;

@Controller
@RequestMapping("/clientes")
public class ClientController {
	
	@Autowired
	private ClientRepository clients;
	
	@Autowired
	private PhoneRepository phones;

	@GetMapping(path = "/filtrar", produces= {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Client> filterSugestion(@RequestParam String phrase) {
		List<Client> all = clients.findAll();
		
		Client cli = new Client();
		cli.setAddress("Rua 10");
		cli.setCpf_cnpj("02932165178");
		cli.setId(7777);
		cli.setName("Everton ALeixo");
		List<Phone> phones = new ArrayList<>();
		Phone f = new Phone();
		f.setId(456);
		f.setPhone("981181968");
		phones.add(f);
		cli.setPhones(phones);
		
		all.add(cli);
		
		return all;
	}
	
	
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
	
	@PostMapping(value="/novo", params = {"newClient"})
	public ModelAndView newClient(Client client) {
		Client clientOnDB = client.getId() <= 0 ? new Client() : clients.findOne(client.getId());
		List<Phone> phonesInMemory = client.getPhones();
		List<Long> idsInMemory = new ArrayList<>();
		List<Long> idsInDB = new ArrayList<>();
		
		for(Phone phoneInDB: clientOnDB.getPhones()) {
			idsInDB.add(phoneInDB.getId());
		}
		
		for(Phone phoneInMemory: phonesInMemory) {
			if(phoneInMemory.getId() <= 0) {
				phoneInMemory.setId(0);
			}
			phoneInMemory = phones.save(phoneInMemory);
			idsInMemory.add(phoneInMemory.getId());
		}
		
		client = clients.saveAndFlush(client);

		for(Long phoneInDB: idsInDB) {
			if(!idsInMemory.contains(phoneInDB))
				phones.delete(phoneInDB);
		}
		
		return clientes();
	}
	
	@PostMapping(value="/novo", params = {"addContactPhone"})
	public ModelAndView addRowPhoneNumber(Client client) {
		Phone phone = new Phone();
		long id = new Random().nextLong();
		while (id == 0) 
			id = new Random().nextLong();
		if (id > 0)
			id = -id;
		
		phone.setId(id);
		if(client.getPhones() == null) {
			synchronized (clients) {
				if(client.getPhones() == null) {
					client.setPhones(new ArrayList<>());
				}
			}
		}
		client.getPhones().add(phone);
		
		return getFicha(client);
	}
	
	@PostMapping(value="/novo", params = {"rmContactPhone"})
	public ModelAndView rmRowPhoneNumber(Client client, HttpServletRequest req) {
		Long phoneId = Long.valueOf(req.getParameter("rmContactPhone"));
		
		for(Phone ph: client.getPhones()) {
			if(ph.getId()==phoneId.longValue()) {
				client.getPhones().remove(ph);
				break;
			}
		}
		
		return getFicha(client);
	}
	
}
