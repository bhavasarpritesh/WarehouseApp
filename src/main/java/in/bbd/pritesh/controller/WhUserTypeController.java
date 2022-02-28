package in.bbd.pritesh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.bbd.pritesh.model.WhUserType;
import in.bbd.pritesh.service.IWhUserTypeService;
import in.bbd.pritesh.util.EmailUtil;

@Controller
@RequestMapping("/whuser")
public class WhUserTypeController {
	@Autowired
	private IWhUserTypeService service;
	@Autowired
	private EmailUtil emailUtil;

	//1. show Register Page
	@GetMapping("/register")
	public String showReg(Model model) {
		//Form backing Object
		model.addAttribute("whUserType", new WhUserType());
		return "WhUserTypeRegister";
	}

	//2. on click save
	@PostMapping("/save")
	public String saveWhUserType(
			@ModelAttribute WhUserType whUserType,  //Read form data as object 
			Model model) //send data to UI
	{
		//calling service
		Integer id = service.saveWhUserType(whUserType); 
		String message = " WhUserType saved with id:"+id;

		if(id!=null && id>0) {
			new Thread(new Runnable() {
				public void run() {
					emailUtil.sendEmail(
							whUserType.getUserEmail(), 
							"Welcome to WhUser", 
							"HELLO:"+whUserType.getUserCode());
				}
			}).start();
		}

		//sending data to UI
		model.addAttribute("message", message);

		//Form backing Object
		model.addAttribute("whUserType", new WhUserType());
		return "WhUserTypeRegister";
	}

	//3. show data 
	@GetMapping("/all")
	public String getAllWhUserTypes(Model model) {
		List<WhUserType> list =  service.getAllWhUserTypes();
		model.addAttribute("list", list);
		return "WhUserTypeData";
	}

	//4. delete one whUserType
	@GetMapping("/delete")
	public String deleteWhUserType(
			@RequestParam Integer id, 
			Model model)
	{
		service.deleteWhUserType(id);
		model.addAttribute("message", "WhUserType '"+id+"' deleted");
		model.addAttribute("list", service.getAllWhUserTypes());
		return "WhUserTypeData";
	}
	//5. show whUserType edit
	@GetMapping("/edit")
	public String showEdit(
			@RequestParam Integer id,
			Model model)
	{
		WhUserType whUserType = service.getOneWhUserType(id);
		model.addAttribute("whUserType", whUserType);
		return "WhUserTypeEdit";
	}

	//6. update whUserType
	@PostMapping("/update")
	public String updateWhUserType(
			@ModelAttribute WhUserType whUserType,
			Model model ) 
	{
		service.updateWhUserType(whUserType);
		model.addAttribute("message", "WhUserType '"+whUserType.getId()+"' Updated");
		model.addAttribute("list", service.getAllWhUserTypes());
		return "WhUserTypeData";
	}

	//7. AJAX CALL MAIL VALIDATION
	@GetMapping("/checkMail")
	public @ResponseBody String validateEmailExist(
			@RequestParam String mail) 
	{
		String msg="";
		if(service.isWhUserMailIdExist(mail)) {
			msg=mail + ", <b>already exist</b>";
		}
		return msg;
	}


}
