package in.bbd.pritesh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.bbd.pritesh.model.Grn;
import in.bbd.pritesh.model.GrnDtl;
import in.bbd.pritesh.repo.GrnDtlRepository;
import in.bbd.pritesh.repo.GrnRepository;
import in.bbd.pritesh.service.IGrnService;

@Service
public class GrnServiceImpl implements IGrnService {
	@Autowired
	private GrnRepository repo;
	@Autowired
	private GrnDtlRepository dtlRepo;
	
	@Override
	public Integer saveGrn(Grn grn) {
		return repo.save(grn).getId();
	}

	@Override
	public List<Grn> getAllGrns() {
		return repo.findAll();
	}

	@Override
	public Integer saveGrnDtl(GrnDtl grnDtl) {
		return dtlRepo.save(grnDtl).getId();
	}
	
	@Override
	public List<GrnDtl> getAllGrnDtlsByGrnId(Integer grnId) {
		return dtlRepo.getAllGrnDtlsByGrnId(grnId);
	}
	
	@Override
	public Grn getOneGrn(Integer id) {
		return repo.findById(id).get();
	}
	
	@Transactional
	@Override
	public Integer updateGrnDtlStatus(Integer grnDtlId, String grnDtlStatus) {
		return dtlRepo.updateGrnDtlStatus(grnDtlId, grnDtlStatus);
	}
}
