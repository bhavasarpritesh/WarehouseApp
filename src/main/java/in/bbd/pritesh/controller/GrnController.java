package in.bbd.pritesh.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.bbd.pritesh.model.Grn;
import in.bbd.pritesh.model.GrnDtl;
import in.bbd.pritesh.model.PurchaseDtl;
import in.bbd.pritesh.service.IGrnService;
import in.bbd.pritesh.service.IPurchaseOrderService;

@Controller
@RequestMapping("/grn")
public class GrnController {
	@Autowired
	private IGrnService service;

	@Autowired
	private IPurchaseOrderService poService;
	
	//Register Page DropDown
	private void addDynamicUiComponents(Model model) {
		Map<Integer,String> pos = poService.getPurchaseOrderIdAndCodeByStatus("INVOICED");
		model.addAttribute("pos", pos);
	}

	//1. show Grn Register page
	@GetMapping("/register")
	public String showGrnReg(Model model) {
		model.addAttribute("grn", new Grn());
		addDynamicUiComponents(model);
		return "GrnRegister";
	}

	//2. save Grn
	@PostMapping("/save")
	public String saveGrn(@ModelAttribute Grn grn,Model model) {
		Integer id = service.saveGrn(grn);
		model.addAttribute("message", "Grn '"+id+"' saved");
		model.addAttribute("grn", new Grn());
		
		//change po status on GRN created successful
		poService.updateStatus(grn.getPo().getId(), "RECEIVED");
		
		addDynamicUiComponents(model);
		
		createGrnDtls(grn);
		return "GrnRegister";
	}

	private void createGrnDtls(Grn grn) {
		//a# Read Purchase Order Id Using Grn linked PO
		Integer orderId = grn.getPo().getId();

		//b# Read all PurchaseDtls data using PurchaseOrder Id
		List<PurchaseDtl> poDtls =  poService.getPurchaseDtlsByOrderId(orderId);
		
		for(PurchaseDtl poDtl:poDtls) {
			//d# Create one GrnDtl object using one PurchaseDtl object
			GrnDtl grnDtl = new GrnDtl();
			grnDtl.setItemCode(poDtl.getPart().getPartCode());
			grnDtl.setBaseCost(poDtl.getPart().getPartCost());
			grnDtl.setQty(poDtl.getQty());
			grnDtl.setItemVal(grnDtl.getBaseCost()*grnDtl.getQty());
			
			//e# Link GrnDtl object to Grn object 
			grnDtl.setGrn(grn);
			
			//f# Save GrnDtl object
			service.saveGrnDtl(grnDtl);
		}
		
	}
	//-------------------------------------------------------------//

	//3. view All Grns
	@GetMapping("/all")
	public String showGrns(Model model) {
		List<Grn> list =  service.getAllGrns();
		model.addAttribute("list", list);
		return "GrnData";
	}
	
	//4. Show GrnDtlsView (Screen#2) based on GrnId
	@GetMapping("/viewParts")
	public String showGrnDtls(@RequestParam Integer grnId,Model model) 
	{
		//grn Object
		Grn grn = service.getOneGrn(grnId);
		model.addAttribute("grn", grn);
		//grn Dtls
		List<GrnDtl> list = service.getAllGrnDtlsByGrnId(grnId);
		model.addAttribute("list", list);
		return "GrnDtlView";
	}
	
	
	//5. Update GrnDtl status
	@GetMapping("/updateStatus")
	public String updateGrnDtlStatus(
			@RequestParam Integer grnId,
			@RequestParam Integer grnDtlId,
			@RequestParam String status
			) 
	{
		service.updateGrnDtlStatus(grnDtlId, status);
		
		return "redirect:viewParts?grnId="+grnId;
	}
	
	
}
