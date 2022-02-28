package in.bbd.pritesh.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.bbd.pritesh.exception.OrderNotFoundException;
import in.bbd.pritesh.model.OrderMethod;
import in.bbd.pritesh.repo.OrderMethodRepository;
import in.bbd.pritesh.service.IOrderMethodService;
import in.bbd.pritesh.util.MyCollectionUtil;

@Service
public class OrderMethodServiceImpl implements IOrderMethodService {

	@Autowired
	private OrderMethodRepository repo;
	
	@Override
	@Transactional
	public Integer saveOrderMethod(OrderMethod om) {
		return repo.save(om).getId();
	}

	@Override
	@Transactional
	public void updateOrderMethod(OrderMethod om) {
		 Optional<OrderMethod> opt =  repo.findById(om.getId());
		 if(opt.isEmpty()) 
				throw new OrderNotFoundException("Order Method '"+om.getId()+"' Not Found");
		repo.save(om);
	}

	@Override
	@Transactional
	public void deleteOrderMethod(Integer id) {
		 Optional<OrderMethod> opt =  repo.findById(id);
		 if(opt.isEmpty()) 
				throw new OrderNotFoundException("Order Method '"+id+"' Not Found");
		repo.delete(opt.get());
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<OrderMethod> getOneOrderMethod(Integer id) {
		 Optional<OrderMethod> opt =  repo.findById(id);
		 if(opt.isEmpty()) 
				throw new OrderNotFoundException("Order Method '"+id+"' Not Found");
		 return opt;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderMethod> getAllOrderMethods() {
		return repo.findAll();
	}


	@Override
	public Map<Integer, String> getOrderMethodIdAndCodeByMode(String mode) {
		List<Object[]> list = repo.getOrderMethodIdAndCodeByMode(mode);
		Map<Integer,String> map = MyCollectionUtil.convertListToMap(list);
		return map;
	}
	
}
