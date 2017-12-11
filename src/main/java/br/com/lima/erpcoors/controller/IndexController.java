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
	
	@GetMapping("/usuarios")
	public String usuarios() {
		return "/usuarios";
	}

	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping(path = { "/403"})
	public String error403() {
		return "/error/403";
	}

}
