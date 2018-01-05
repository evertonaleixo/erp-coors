package br.com.lima.erpcoors.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
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
		BigDecimal total = new BigDecimal(0f);
		
		for(Order o: findAll) {
			total = total.add(o.getValue());
		}

		mav.addObject("total", total.floatValue());
		mav.addObject("orders", findAll);
		mav.addObject("filter", "");
		mav.addObject("filter_id", 0);

		return mav;
	}
	
	@GetMapping(value="/ver", params = { "view" })
	public ModelAndView show(@RequestParam(value = "data_ini") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateIni, 
			@RequestParam(value = "data_end") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateEnd,
			@RequestParam(value = "filter") String filter, @RequestParam(value = "filter_id") long filterID) {
		
		ModelAndView mav = new ModelAndView("fluxo");
		List<Order> find = this.selectOrders(dateIni, dateEnd, filterID);
		
		BigDecimal total = new BigDecimal(0f);
		
		for(Order o: find) {
			total = total.add(o.getValue());
		}

		mav.addObject("total", total.floatValue());
		mav.addObject("orders", find);
		mav.addObject("date_ini", dateIni);
		mav.addObject("date_end", dateEnd);
		mav.addObject("filter", filter);
		mav.addObject("filter_id", filterID);

		return mav;
	}
	
	@GetMapping(value="/ver", params = { "print" })
	public ModelAndView print(@RequestParam(value = "data_ini") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateIni, 
			@RequestParam(value = "data_end") @DateTimeFormat(pattern="dd/MM/yyyy") Date dateEnd,
			@RequestParam(value = "filter") String filter, @RequestParam(value = "filter_id") long filterID) {
		
		ModelAndView mav = new ModelAndView("fluxo_imprimir");
		List<Order> find = this.selectOrders(dateIni, dateEnd, filterID);
		
		double value = 0f;
		double valueInProd = 0f;
		int numOrders = find.size();
		
		for(Order o: find) {
			value += o.getValue().doubleValue();
			valueInProd += o.getFullValue().doubleValue();
		}

		mav.addObject("orders", find);
		String dIni = "inicio";
		if(dateIni != null)
			dIni = String.format("%2d/%2d/%4d", dateIni.getDate(), (dateIni.getMonth()+1), (dateIni.getYear()+1900));
		mav.addObject("date_ini", dIni);
		String dEnd = "dias atuais";
		if(dateEnd != null)
			dEnd = String.format("%2d/%2d/%4d", dateEnd.getDate(), (dateEnd.getMonth()+1), (dateEnd.getYear()+1900));
		mav.addObject("date_end", dEnd);
		mav.addObject("ticket", (value/numOrders));
		mav.addObject("totalInProd", valueInProd);
		mav.addObject("totalDiscount", (valueInProd - value));
		mav.addObject("totalValue", value);
		mav.addObject("filter", filter);
		mav.addObject("filter_id", filterID);

		return mav;
	}
	
	private List<Order> selectOrders (Date dateIni, Date dateEnd, long filter) {
		if(dateEnd != null && dateIni != null) {
			if(filter != 0f)
				return orders.betweenQueryAndFilter(dateIni, dateEnd, filter);
			else
				return orders.betweenQuery(dateIni, dateEnd);
		} else if (dateEnd != null) {
			if(filter != 0f)
				return orders.lessQueryAndFilter(dateEnd, filter);
			else
				return orders.lessQuery(dateEnd);
		} else if( dateIni != null) {
			if(filter != 0f)
				return orders.greaterQueryAndFilter(dateIni, filter);
			else
				return orders.greaterQuery(dateIni);
		} else {
			if(filter != 0f)
				return orders.filterQuery(filter);
			else
				return new LinkedList<>();
		}
	}
}
