package in.bbd.pritesh.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.bbd.pritesh.model.GrnDtl;

public interface GrnDtlRepository extends JpaRepository<GrnDtl, Integer> {

	@Query("SELECT dtl FROM GrnDtl dtl JOIN dtl.grn as grn WHERE grn.id=:grnId")
	public List<GrnDtl> getAllGrnDtlsByGrnId(Integer grnId);
	
	@Modifying
	@Query("UPDATE GrnDtl SET status=:grnDtlStatus WHERE id=:grnDtlId")
	public Integer updateGrnDtlStatus(Integer grnDtlId,String grnDtlStatus); 
}
