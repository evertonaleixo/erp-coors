package br.com.lima.erpcoors.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.lima.erpcoors.model.Order;
import br.com.lima.erpcoors.repository.OrderRepository;

@Controller
@RequestMapping("/fluxo")
public class FluxController {
	@Autowired
	private OrderRepository orders;

	@GetMapping("/consulta")
	public ModelAndView query() {
		ModelAndView mav = new ModelAndView("fluxo");
		List<Order> findAll = orders.findAll();
		
		mav.addObject("orders", findAll);

		return mav;
	}
	
	@GetMapping(value="/ver", params = { "view" })
	public ModelAndView show(@RequestParam(value = "data_ini") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateIni, 
			@RequestParam(value = "data_end") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateEnd) {
		ModelAndView mav = new ModelAndView("fluxo");
		List<Order> findAll = orders.findAll();

		mav.addObject("orders", findAll);
		mav.addObject("date_ini", dateIni);
		mav.addObject("date_end", dateEnd);
		

		return mav;
	}
	
	@GetMapping(value="/ver", params = { "print" })
	public ModelAndView print(@RequestParam(value = "data_ini") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateIni, 
			@RequestParam(value = "data_end") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateEnd) {
		ModelAndView mav = new ModelAndView("fluxo_imprimir");
		List<Order> findAll = orders.findAll();
		
		double value = 0f;
		double valueInProd = 0f;
		int numOrders = findAll.size();
		
		for(Order o: findAll) {
			value += o.getValue().doubleValue();
			valueInProd += o.getFullValue().doubleValue();
		}

		mav.addObject("orders", findAll);
		String dIni = String.format("%2d/%2d/%4d", dateIni.getDate(), (dateIni.getMonth()+1), (dateIni.getYear()+1900));
		mav.addObject("date_ini", dIni);
		String dEnd = String.format("%2d/%2d/%4d", dateEnd.getDate(), (dateEnd.getMonth()+1), (dateEnd.getYear()+1900));
		mav.addObject("date_end", dEnd);
		mav.addObject("ticket", (value/numOrders));
		mav.addObject("totalInProd", valueInProd);
		mav.addObject("totalDiscount", (valueInProd - value));
		mav.addObject("totalValue", value);

		return mav;
	}
}
