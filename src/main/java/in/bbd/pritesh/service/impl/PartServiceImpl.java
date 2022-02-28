package in.bbd.pritesh.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.bbd.pritesh.exception.PartNotFoundException;
import in.bbd.pritesh.model.Part;
import in.bbd.pritesh.repo.PartRepository;
import in.bbd.pritesh.service.IPartService;
import in.bbd.pritesh.util.MyCollectionUtil;

@Service
public class PartServiceImpl implements IPartService {
	
	@Autowired
	private PartRepository repo;
	
	@Override
	public Integer savePart(Part part) {
		return repo.save(part).getId();
	}

	@Override
	public List<Part> getAllParts() {
		return repo.findAll();
	}

	@Override
	public void deletePart(Integer id) {
		Part part  = getOnePart(id);
		repo.delete(part);
	}
	
	@Override
	public Part getOnePart(Integer id) {
		Part part = repo.findById(id)
				.orElseThrow(()-> new PartNotFoundException("Part '"+id+"' Not exist"));
		return part;
	}
	
	@Override
	public void updatePart(Part part) {
		repo.save(part);
	}
	
	@Override
	public Map<Integer, String> getPartIdAndCode() {
		List<Object[]> list =  repo.getPartIdAndCode();
		return MyCollectionUtil.convertListToMap(list);
	}
	
	
}