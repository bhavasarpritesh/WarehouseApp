package in.bbd.pritesh.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.bbd.pritesh.exception.PurchaseOrderNotFound;
import in.bbd.pritesh.model.PurchaseDtl;
import in.bbd.pritesh.model.PurchaseOrder;
import in.bbd.pritesh.repo.PurchaseDtlRepository;
import in.bbd.pritesh.repo.PurchaseOrderRepository;
import in.bbd.pritesh.service.IPurchaseOrderService;
import in.bbd.pritesh.util.MyCollectionUtil;

@Service
public class PurchaseOrderServiceImpl implements IPurchaseOrderService {
	@Autowired
	private PurchaseOrderRepository repo;
	@Autowired
	private PurchaseDtlRepository dtlRepo;
	
	@Override
	public Integer savePurchaseOrder(PurchaseOrder po) {
		return repo.save(po).getId();
	}

	@Override
	public List<PurchaseOrder> getAllPurchaseOrders() {
		return repo.findAll();
	}

	@Override
	public PurchaseOrder getOnePurchaseOrder(Integer id) {
		return repo.findById(id).orElseThrow(
				()-> new PurchaseOrderNotFound("Not Found"));
	}
	
	
	//---screen#2-------------------
	@Override
	public Integer savePurchaseDtl(PurchaseDtl dtl) {
		return dtlRepo.save(dtl).getId();
	}
	
	@Override
	public List<PurchaseDtl> getPurchaseDtlsByOrderId(Integer orderId) {
		return dtlRepo.getPurchaseDtlsByOrderId(orderId);
	}
	
	@Override
	public void removePurchaseDtl(Integer id) {
		dtlRepo.deleteById(id);
	}
	
	@Override
	@Transactional
	public void updateStatus(Integer orderId,String status) {
		repo.updatePurchaseOrderStatusById(orderId, status);
	}
	
	@Override
	public Integer getDtlsCountByOrderId(Integer orderId) {
		return dtlRepo.getPurchaseDtlsCountByOrderId(orderId);
	}
	
	@Override
	public Map<Integer, String> getPurchaseOrderIdAndCodeByStatus(String status) {
		List<Object[]> list = repo.getPurchaseOrderIdAndCodeByStatus(status);
		return MyCollectionUtil.convertListToMap(list);
	}
}
