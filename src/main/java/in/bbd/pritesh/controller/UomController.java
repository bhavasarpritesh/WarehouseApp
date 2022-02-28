package in.bbd.pritesh.controller;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import in.bbd.pritesh.model.Uom;
import in.bbd.pritesh.service.IUomService;
import in.bbd.pritesh.util.UomUtil;
import in.bbd.pritesh.view.UomExcelView;
import in.bbd.pritesh.view.UomPdfView;

@Controller
@RequestMapping("/uom")
public class UomController {
	@Autowired
	private IUomService service;
	
	@Autowired
	private UomUtil util;
	
	@Autowired
	private ServletContext sc;

	//1. show Register Page
	@GetMapping("/register")
	public String showReg(Model model) {
		//Form backing Object
		model.addAttribute("uom", new Uom());
		return "UomRegister";
	}

	//2. on click save
	@PostMapping("/save")
	public String saveUom(
			@ModelAttribute Uom uom,  //Read form data as object 
			Model model) //send data to UI
	{
		//calling service
		Integer id = service.saveUom(uom); 
		String message = " Uom saved with id:"+id;
		//sending data to UI
		model.addAttribute("message", message);

		//Form backing Object
		model.addAttribute("uom", new Uom());
		return "UomRegister";
	}

	///.../all?page=<number>
	//3. show data 
	@GetMapping("/all")
	public String getAllUoms(
			@PageableDefault(page = 0,size = 3)Pageable p,
			@RequestParam(value="uomModel",required = false,defaultValue = "")String uomModel,
			Model model) 
	{
		Page<Uom> page = null;
		if("".equals(uomModel)) { //if no search input then fetch all
			 page = service.getAllUoms(p);
		}else {
			//specific search input exist then get matching data
			page = service.findByUomModelContaining(uomModel, p);
		}
		//returns List<T> data exist in current page
		model.addAttribute("list", page.getContent());
		//Pagination information
		model.addAttribute("page", page);
		
		return "UomData";
	}

	//4. delete one uom
	@GetMapping("/delete")
	public String deleteUom(
			@RequestParam Integer id,
			@PageableDefault(page = 0,size = 3)Pageable p,
			Model model)
	{
		try {
			service.deleteUom(id);
			model.addAttribute("message", "Uom '"+id+"' deleted");
			//model.addAttribute("list", service.getAllUoms());
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("message", "Uom '"+id+"' can not be deleted");
		}
		Page<Uom> page = service.getAllUoms(p);
		//returns List<T> data exist in current page
		model.addAttribute("list", page.getContent());
		//Pagination information
		model.addAttribute("page", page);
		return "UomData";
	}
	//5. show uom edit
	@GetMapping("/edit")
	public String showEdit(
			@RequestParam Integer id,
			Model model)
	{
		Uom uom = service.getOneUom(id);
		model.addAttribute("uom", uom);
		return "UomEdit";
	}

	//6. update uom
	@PostMapping("/update")
	public String updateUom(
			@ModelAttribute Uom uom,
			@PageableDefault(page = 0,size = 3)Pageable p,
			Model model ) 
	{
		service.updateUom(uom);
		model.addAttribute("message", "Uom '"+uom.getId()+"' Updated");
		//model.addAttribute("list", service.getAllUoms());
		Page<Uom> page = service.getAllUoms(p);
		//returns List<T> data exist in current page
		model.addAttribute("list", page.getContent());
		//Pagination information
		model.addAttribute("page", page);
		return "UomData";
	}

	//7. excel export
	@GetMapping("/excel")
	public ModelAndView exportToExcel() {
		ModelAndView m = new ModelAndView();
		m.setView(new UomExcelView());

		//fetch data from DB
		List<Uom> list= service.getAllUoms();
		//send data to View class
		m.addObject("list", list);
		return m;
	}

	//8. pdf export
	@GetMapping("/pdf")
	public ModelAndView exportToPdf() {
		ModelAndView m = new ModelAndView();
		m.setView(new UomPdfView());

		//fetch data from DB
		List<Uom> list= service.getAllUoms();
		//send data to View class
		m.addObject("list", list);
		return m;
	}

	//9. AJAX VALIDATION
	@GetMapping("/validate")
	public @ResponseBody String validateModel(
			@RequestParam String model,
			@RequestParam Integer id
			)
	{
		String message = "";
		if(id!=0 && service.isUomModelExistForEdit(model, id)) { 
			//if id exist then request came from edit page
			message = "Uom Model '"+model+"' already exist";
		}
		if(id==0 && service.isUomModelExist(model)) {
			//if not id exist then request came from register page
			message = "Uom Model '"+model+"' already exist";
		}
		
		return message;
	}
	
	//10. Show Charts
	@GetMapping("/charts")
	public String showCharts() {
		List<Object[]> data =  service.getUomTypeAndCount();
		String path = sc.getRealPath("/");
		util.generatePie(path, data);
		util.generateBar(path, data);
		return "UomCharts";
	}
	
	
}
