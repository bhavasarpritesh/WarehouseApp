package in.bbd.pritesh.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.bbd.pritesh.model.PurchaseDtl;

public interface PurchaseDtlRepository extends JpaRepository<PurchaseDtl, Integer> {

	@Query("SELECT dtl FROM PurchaseDtl dtl JOIN dtl.order as order WHERE order.id =:orderId")
	public List<PurchaseDtl> getPurchaseDtlsByOrderId(Integer orderId);
	
	
	@Query("SELECT count(dtl.id) FROM PurchaseDtl dtl JOIN dtl.order as order WHERE order.id =:orderId")
	public Integer getPurchaseDtlsCountByOrderId(Integer orderId);
}
