package in.bbd.pritesh.controller;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.bbd.pritesh.model.OrderMethod;
import in.bbd.pritesh.service.IOrderMethodService;

@Controller
@RequestMapping("/ordermethod")
public class OrderMethodController {

	private Logger log = LoggerFactory.getLogger(OrderMethodController.class);

	@Autowired
	private IOrderMethodService service;


	//1. show Reg page
	@GetMapping("/register")
	public String showReg(Model model) {
		model.addAttribute("orderMethod", new OrderMethod());
		return "OrderMethodRegister";
	}

	//2. save data
	@PostMapping("/save")
	public String save(
			@ModelAttribute OrderMethod orderMethod,
			Model model) 
	{
		Integer id=service.saveOrderMethod(orderMethod);
		String message="Order Method '"+id+"' saved";
		model.addAttribute("message", message);
		//clear form
		model.addAttribute("orderMethod", new OrderMethod());
		return "OrderMethodRegister";
	}

	//3. show all
	@GetMapping("/all")
	public String getAll(
			@ModelAttribute OrderMethod orderMethod, //search form data
			Model model) 
	{
		model.addAttribute("list", service.getAllOrderMethods());
		model.addAttribute("orderMethod", orderMethod); //form backing object
		return "OrderMethodData";
	}

	//4. delete
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id,Model model) {

		service.deleteOrderMethod(id);

		model.addAttribute("message", "Order Method '"+id+"' deleted");
		model.addAttribute("list", service.getAllOrderMethods());
		return "OrderMethodData";
	}


	//5. edit page
	@GetMapping("/edit/{id}")
	public String showEdit(@PathVariable Integer id ,Model model) {
		String page=null;
		Optional<OrderMethod> opt=service.getOneOrderMethod(id);
		if(opt.isPresent()) {
			model.addAttribute("orderMethod", opt.get());
			page="OrderMethodEdit";
		}else {
			page="redirect:../all";
		}
		return page;
	}

	//6. do update
	@PostMapping("/update")
	public String update(
			@ModelAttribute OrderMethod orderMethod, 
			Model model) 
	{
		service.updateOrderMethod(orderMethod);
		model.addAttribute("message", "Order Method '"+orderMethod.getId()+"' Updated!");
		model.addAttribute("list", service.getAllOrderMethods());
		return "OrderMethodData";
	}


	//7. show one
	@GetMapping("/view/{id}")
	public String showView(@PathVariable Integer id,Model model) 
	{
		try {
			log.info("Making Service call");
			Optional<OrderMethod> opt=service.getOneOrderMethod(id);
			log.info("Reading Data from Optional Object");
			OrderMethod om=opt.get();
			model.addAttribute("ob", om);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return "OrderMethodView";
	}

		
}