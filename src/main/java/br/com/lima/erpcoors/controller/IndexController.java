package br.com.lima.erpcoors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping(path = { "/", "/index", "/home" })
	public String index() {
		return "/index";
	}
	
	@GetMapping("/fluxo")
	public String fluxo() {
		return "/fluxo";
	}
	
	@GetMapping("/nota")
	public String nota() {
		return "/nota";
	}
	
	@GetMapping("/usuarios")
	public String usuarios() {
		return "/usuarios";
	}

	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/403")
	public String error403() {
		return "/error/403";
	}

}
