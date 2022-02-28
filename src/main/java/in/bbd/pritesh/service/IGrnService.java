package in.bbd.pritesh.service;

import java.util.List;

import in.bbd.pritesh.model.Grn;
import in.bbd.pritesh.model.GrnDtl;

public interface IGrnService {

	public Integer saveGrn(Grn grn);
	public Grn getOneGrn(Integer id);
	public List<Grn> getAllGrns();
	
	public Integer saveGrnDtl(GrnDtl grnDtl);
	
	//screen#2
	public List<GrnDtl> getAllGrnDtlsByGrnId(Integer grnId);
	public Integer updateGrnDtlStatus(Integer grnDtlId,String grnDtlStatus);
}
