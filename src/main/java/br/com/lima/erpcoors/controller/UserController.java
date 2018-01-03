package br.com.lima.erpcoors.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import br.com.lima.erpcoors.model.UserDTO;
import br.com.lima.erpcoors.model.UserRoles;
import br.com.lima.erpcoors.repository.UserRepository;
import br.com.lima.erpcoors.repository.UserRolesRepository;

@Controller
@RequestMapping("/usuarios")
public class UserController {
	@Autowired
	private UserRepository users;
	
	@Autowired
	private UserRolesRepository userRoles;

	@GetMapping("/listar")
	public ModelAndView list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("usuarios");
		List<User> all = users.findAll();
		List<UserDTO> allView = new LinkedList<>();
		List<UserRoles> roles = userRoles.findAll();
		
		Map<String, String> userRoles = new HashMap<>();
		
		for(UserRoles ur: roles) {
			if(!userRoles.containsKey(ur.getUsername())) {
				userRoles.put(ur.getUsername(), ur.getRole());
			}
		}

		Principal principal = request.getUserPrincipal();
		String principalName = principal.getName();
		
		for(int i=0 ; i<all.size() ; i++) {
			User u = all.get(i);
			if(!u.getUsername().equals(principalName)) {
				UserDTO userDTO = new UserDTO();
				userDTO.setUsername(u.getUsername());
				userDTO.setRole(userRoles.get(u.getUsername()));
				allView.add(userDTO);
			}
		}
		
		mav.addObject("users", allView);
		return mav;
	}
	
	@GetMapping("/novo")
	public ModelAndView create(UserDTO user) {
		ModelAndView mav = new ModelAndView("usuarios_ficha");
		mav.addObject("user", user);
		
		return mav;
	}
	
	@PostMapping("/novo")
	public ModelAndView insert(UserDTO userDTO, HttpServletRequest request) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
		
		User user = new User();
		
		user.setUsername(userDTO.getUsername());
		user.setPassword(hashedPassword);
		user.setEnabled(true);
		user = users.save(user);

		UserRoles ur = new UserRoles();
		ur.setUsername(user.getUsername());
		ur.setRole(userDTO.getRole());
		userRoles.save(ur);
		
		return list(request);
	}
	
	@GetMapping("/del/{id}")
	public ModelAndView delete(@PathVariable("id") String name, HttpServletRequest request) {
		User one = users.findOne(name);

		if (one != null && one.getUsername() != null && !one.getUsername().isEmpty()) {
			List<UserRoles> roles = userRoles.findAll();
			for(int i=0 ; i<roles.size() ; i++) {
				if(roles.get(i).getUsername().equals(one.getUsername())) {
					userRoles.delete(roles.get(i));
				}
			}
			users.delete(one);
		}

		return list(request);
	}
}
