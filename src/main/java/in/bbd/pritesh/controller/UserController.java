package in.bbd.pritesh.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.bbd.pritesh.model.User;
import in.bbd.pritesh.service.IUserService;
import in.bbd.pritesh.util.EmailUtil;
import in.bbd.pritesh.util.UserUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserService service;
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private UserUtil userUtil;
	
	@GetMapping("/showLogin")
	public String showLogin() {
		return "UserLogin";
	}
	
	
	//1. show User Reg page
	@GetMapping("/register")
	public String showReg() {
		return "UserRegister";
	}
	
	//2. Add user
	@PostMapping("/save")
	public String saveUser(
			@ModelAttribute User user,
			Model model) 
	{
		String pwd=user.getPwd();
		String otp = userUtil.genOtp();
		user.setOtp(otp);
		
		Integer id = service.saveUser(user);
		model.addAttribute("message", "User '"+user.getName()+",("+id+")' saved");
		
		if(id!=null) {
			String text = 
					"Hello User, you username : " + user.getEmail()
					+ ", Password : " + pwd 
					+ ", OTP : " + otp 
					+ ", Roles :"+user.getRoles()
					+ ", Status :" + (user.isActive()?"ACTIVE":"IN ACTIVE");
			emailUtil.sendEmail(user.getEmail(), "WELCOME TO USER!!", text);
		}
		return "UserRegister";
	}
	
	//3. View All Users.
	@GetMapping("/all")
	public String viewAll(Model model)
	{
		List<User> list = service.getAllUsers();
		model.addAttribute("list", list);
		return "UserData";
	}
	
	//activate user
	@GetMapping("/activate")
	public String activateUser(@RequestParam Integer uid)
	{
		service.modifyStatus(uid, true);
		User user = service.getOneUser(uid);
		String text = 
				"Hello User (" + user.getName()
				+ "), Your Modified Status : ACTIVE ";
		emailUtil.sendEmail(user.getEmail(), "Status Update Message", text);
		return "redirect:all";
	}
	
	//inactivate user
	@GetMapping("/inactivate")
	public String inActivateUser(@RequestParam Integer uid)
	{
		service.modifyStatus(uid, false);
		User user = service.getOneUser(uid);
		String text = 
				"Hello User (" + user.getName()
				+ "), Your Modified Status : IN-ACTIVE ";
		emailUtil.sendEmail(user.getEmail(), "Status Update Message", text);
		return "redirect:all";
	}
	
	@GetMapping("/setup")
	public String loginSetup(Principal p,HttpSession ses) {
		Optional<User> opt = service.findByEmail(p.getName());
		ses.setAttribute("userOb", opt.get());
		
		return "redirect:../uom/all";
	}

	@GetMapping("/profile")
	public String showProfile(HttpSession ses,Model model) {
		User user = (User) ses.getAttribute("userOb");
		model.addAttribute("user", user);
		return "UserProfile";
	}
	
	@GetMapping("/modifyPwd")
	public String showModifyPwd() 
	{
		
		return "UserPwd";
	}
	
	@PostMapping("/pwdUpdate")
	public String updatePwd(
			@RequestParam String pwd1,
			HttpSession ses) 
	{
		User user = (User) ses.getAttribute("userOb");
		service.updatePwd(user.getId(), pwd1);
		
		String text="Hello,"+user.getName()
		+", Your password is modified, New password is:"+pwd1;
		emailUtil.sendEmail(user.getEmail(), "Password Modified", text);
		
		return "redirect:profile";
	}
	
	@GetMapping("/showForgotPwd")
	public String showForgotPwd() 
	{
		
		return "UserForgotPwd";
	}
	
	@PostMapping("/newPwdGen")
	public String newPwdGen(
			@RequestParam String uemail,Model model) 
	{
		String message = null;
		
		Optional<User>  opt =service.findByEmail(uemail);
		if(opt.isPresent()) {
			User user = opt.get();
			//generate new pwd
			String pwd = userUtil.genPwd();
			//update to db.
			service.updatePwd(user.getId(), pwd);
			
			String text="Hello,"+user.getName()
			+", Your password is Updated, New password is:"+pwd;
			
			emailUtil.sendEmail(user.getEmail(), "Password Generated", text);
			
			message ="New password is sent to your email!";
		} else {
			message= uemail +", not exist";
		}
		model.addAttribute("message", message);
		
		return "UserForgotPwd";
	}
	
	@GetMapping("/showactivateUserotp")
	public String showactivateUserotp() {
		return "UserOtpPage";
	}

	@PostMapping("/activatebyOtp")
	public String activateByotp(
			@RequestParam String uemail,
			@RequestParam String otp,
			@RequestParam String opr,
			Model model
			) 
	{
		String message = null;
		
		Optional<User> opt = service.findByEmail(uemail);
		if(opt.isEmpty()) {
			message = "Invail Email Id provided";
		} else {
			User user = opt.get();
			if(opr.equalsIgnoreCase("ACTIVATE")) {
				if(user.getOtp().equals(otp)) {
					service.modifyStatus(user.getId(), true);
					message = "User Activated Please Login now!!";
				} else {
					message = "Invail OTP provided";
				}
			} else if(opr.equalsIgnoreCase("RESEND")) {
				//generate opt
				String otpNew = userUtil.genOtp();
				//update in DB
				service.updateNewOtpById(user.getId(),otpNew);
				//also send email
				String text="Hello,"+user.getName()
				+", Your NEW  OTP is "+otpNew;
				
				emailUtil.sendEmail(user.getEmail(), "NEW OTP ", text);
				
				// otp sent , plese check it
				message = "OTP SENT TO EMAIL PLEASE CHECK IT";
			}
			
		}
		model.addAttribute("message", message);
		return "UserOtpPage";
	}
	
	
	
}
