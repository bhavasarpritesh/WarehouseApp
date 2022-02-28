package in.bbd.pritesh.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.bbd.pritesh.exception.ShipmentTypeNotFoundException;
import in.bbd.pritesh.model.ShipmentType;
import in.bbd.pritesh.repo.ShipmentTypeRepository;
import in.bbd.pritesh.service.IShipmentTypeService;
import in.bbd.pritesh.util.MyCollectionUtil;

@Service
public class ShipmentTypeServiceImpl implements IShipmentTypeService {
	@Autowired
	private ShipmentTypeRepository repo;
	
	@Override
	public Integer saveShipmentType(ShipmentType st) {
		return repo.save(st).getId();
	}

	@Override
	public void updateShipmentType(ShipmentType st) {
		repo.save(st);
	}

	@Override
	public void deleteShipmentType(Integer id) {
		ShipmentType st = getOneShipmentType(id);
		repo.delete(st);
	}

	@Override
	public ShipmentType getOneShipmentType(Integer id) {
		ShipmentType st = repo.findById(id)
				.orElseThrow(
						()-> new ShipmentTypeNotFoundException(
								"ShipmentType '"+id+"' Not exist"));
		return st;
	}

	@Override
	public List<ShipmentType> getAllShipmentTypes() {
		return repo.findAll();
	}
	
	@Override
	public boolean isShipmentTypeCodeExist(String code) {
		return repo.getShipmentTypeCodeCount(code)>0 ? true:false;
	}
	
	@Override
	public List<Object[]> getShipmentModeAndCount() {
		return repo.getShipmentModeAndCount();
	}

	@Override
	public Map<Integer, String> getShipmentIdAndCodeByEnable(String enable) {
		List<Object[]> list = repo.getShipmentIdAndCodeByEnable(enable);
		Map<Integer,String> map = MyCollectionUtil.convertListToMap(list);
		return map;
	}
	
	@Override
	public Page<ShipmentType> getAllShipmentTypes(Pageable p) {
		return repo.findAll(p);
	}
}
