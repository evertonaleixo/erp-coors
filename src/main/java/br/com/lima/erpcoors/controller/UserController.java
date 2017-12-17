package br.com.lima.erpcoors.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.User;
import br.com.lima.erpcoors.repository.UserRepository;

@Controller
@RequestMapping("/usuarios")
public class UserController {
	@Autowired
	private UserRepository users;

	@GetMapping("/listar")
	public ModelAndView list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("usuarios");
		List<User> all = users.findAll();

		Principal principal = request.getUserPrincipal();
		String principalName = principal.getName();
		
		int index = -1;
		for(int i=0 ; i<all.size() ; i++) {
			User u = all.get(i);
			if(u.getName().equals(principalName)) {
				index = i;
				break;
			}
		}
		
		if(index>0) {
			all.remove(index);
		}
		
		mav.addObject("users", all);
		return mav;
	}
	
	@GetMapping("/novo")
	public ModelAndView create(User user) {
		ModelAndView mav = new ModelAndView("usuarios_ficha");
		mav.addObject("user", user);
		
		return mav;
	}
	
	@PostMapping("/novo")
	public ModelAndView insert(User user, HttpServletRequest request) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(user.getPasswd());
		user.setPasswd(hashedPassword);
		
		user = users.save(user);
		
		return list(request);
	}
	
	@GetMapping("/del/{id}")
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletRequest request) {
		User one = users.findOne(id);

		if (one != null && one.getId() != 0)
			users.delete(one);

		return list(request);
	}
}
