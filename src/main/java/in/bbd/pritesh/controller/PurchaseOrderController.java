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
import org.springframework.web.servlet.ModelAndView;

import in.bbd.pritesh.model.PurchaseDtl;
import in.bbd.pritesh.model.PurchaseOrder;
import in.bbd.pritesh.service.IPartService;
import in.bbd.pritesh.service.IPurchaseOrderService;
import in.bbd.pritesh.service.IShipmentTypeService;
import in.bbd.pritesh.service.IWhUserTypeService;
import in.bbd.pritesh.view.VendorInvoicePdfView;

@Controller
@RequestMapping("/po")
public class PurchaseOrderController {
	@Autowired
	private IPurchaseOrderService service;
	
	@Autowired
	private IShipmentTypeService shipmentService;
	@Autowired
	private IWhUserTypeService whservice;
	@Autowired
	private IPartService partService;
	
	//Register Page DropDown
	private void addDynamicUiComponents(Model model) {
		model.addAttribute("shipmenttypes", shipmentService.getShipmentIdAndCodeByEnable("YES"));
		model.addAttribute("vendors", whservice.getWhUserTypeByUserType("Vendor"));
	}
	
	//1. show register page
	@GetMapping("/register")
	public String showReg(Model model) {
		PurchaseOrder po = new PurchaseOrder();
		po.setStatus("OPEN");
		model.addAttribute("purchaseOrder", po);
		addDynamicUiComponents(model);
		return "PurchaseOrderRegister";
	}
	
	//2. save data
	@PostMapping("/save")
	public String save(
			@ModelAttribute PurchaseOrder purchaseOrder,
			Model model) 
	{
		Integer id = service.savePurchaseOrder(purchaseOrder);
		model.addAttribute("message", "Purchase Order '"+id+"' saved");
		PurchaseOrder po = new PurchaseOrder();
		po.setStatus("OPEN");
		model.addAttribute("purchaseOrder", po);
		addDynamicUiComponents(model);
		return "PurchaseOrderRegister";
	}
	
	//3. display POs
	@GetMapping("/all")
	public String showAll(Model model) {
		List<PurchaseOrder> list = service.getAllPurchaseOrders();
		model.addAttribute("list", list);
		return "PurchaseOrderData";
	}
	
	//------------Methods for Screen#2 Purchase Order Parts ------------//
	//--section#2-------
	private void addDynamicUiComponentsForParts(Model model) {
		model.addAttribute("parts", partService.getPartIdAndCode());
	}
	
	/**
	 * This method is showing output of Screen#2 
	 * It is displayed when we click on ADD PARTS Button from Screen#1
	 * and even after adding new Part/Remove added part same page is loaded.
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/parts")
	public String showPartsPage(
			@RequestParam Integer id, //OrderId
			Model model) 
	{
		//--section#1-------
		// Get PurchaseOrder by Id
		/**
		 * We have orderId, i want to display orderCode and orderStatus. 
		 * So, we can get order object using order id that contains order code and status.
		 * Send this object to ui and display using ${po.orderCode}, ${po.orderStatus}
		 */
		PurchaseOrder po = service.getOnePurchaseOrder(id);
		model.addAttribute("po", po);
		
		//--section#2-------
		//dynamic DropDown 
		addDynamicUiComponentsForParts(model);
		
		//send FormBacking Object
		model.addAttribute("purchaseDtl", new PurchaseDtl());
		
		//----section-#4------
		// Show Parts added to Order based on orderId
		List<PurchaseDtl> dtls = service.getPurchaseDtlsByOrderId(id);// orderId
		//send data to UI
		model.addAttribute("dtls", dtls);
		
		
		return "PurchaseOrderParts";
	}
	
	/**
	 *  On click add part button,
	 *  Read Form data as PurchaseDtl
	 *  save into Db using service method
	 *  redirect to same UI with /parts?id=<OrderId>
	 */
	@PostMapping("/add") //----section#3----
	public String addPart(@ModelAttribute PurchaseDtl purchaseDtl)
	{
		Integer orderId = purchaseDtl.getOrder().getId();
		
		service.savePurchaseDtl(purchaseDtl);
		
		//update order status
		service.updateStatus(orderId, "PICKING");
		//service.updateStatus(orderId, OrderStatus.PICKING.name());
		
		// From PurchaseDtl -> get Order --> from Order get Id (ie order id)
		return "redirect:parts?id="+orderId; //Order Id (PO-ID)
	}
	
	/**
	 * On Click Remove Button/Link, this method is called 
	 * with 2 inputs @param dtlId and @param orderId
	 * It will remove one part using DtlId and then redirect to
	 * same page using order id
	 * 
	 */
	@GetMapping("/remove")
	public String removePart(
			@RequestParam Integer dtlId,
			@RequestParam Integer orderId
			) 
	{
		service.removePurchaseDtl(dtlId);
		if(service.getDtlsCountByOrderId(orderId)==0) {
			service.updateStatus(orderId, "OPEN");
			//service.updateStatus(orderId, OrderStatus.OPEN.name());
		}
		return "redirect:parts?id="+orderId;
	}
	
	/**
	 * Read orderId and update status of PO to ORDERED
	 * Finally redirect to screen#2 /part?id=<orderId>
	 */
	@GetMapping("/confirmOrder")
	public String placeOrder(
			@RequestParam Integer orderId
			) 
	{
		service.updateStatus(orderId, "ORDERED");
		//service.updateStatus(orderId, OrderStatus.ORDERED.name());
		//back to screen#2
		return "redirect:parts?id="+orderId;
	}
	
	/**
	 * Generate Invoice Status Change
	 * from ORDERED TO INVOICED 
	 */
	@GetMapping("/genInv")
	public String generateInvoice(@RequestParam Integer id) //orderId 
	{
		
		service.updateStatus(id, "INVOICED");
		//service.updateStatus(id, OrderStatus.INVOICED.name());
		return "redirect:all"; //screen#1
	}
	
	/**
	 * print Vendor Invoice PDF based on OrderId
	 * 
	 */
	@GetMapping("/printInv")
	public ModelAndView showInvoice(@RequestParam Integer id) //orderId
	{
		ModelAndView m = new ModelAndView();
		m.setView(new VendorInvoicePdfView());
		
		m.addObject("po",service.getOnePurchaseOrder(id));
		m.addObject("dtls",service.getPurchaseDtlsByOrderId(id));
		
		return m;
	}
	
}
