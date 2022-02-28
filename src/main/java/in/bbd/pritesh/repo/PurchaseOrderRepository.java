package in.bbd.pritesh.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.bbd.pritesh.model.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

	@Modifying
	@Query("UPDATE  PurchaseOrder  SET  status=:status  WHERE  id=:orderId")
	public void updatePurchaseOrderStatusById(Integer orderId,String status);
	
	@Query("SELECT id,orderCode FROM PurchaseOrder WHERE status=:status")
	public List<Object[]> getPurchaseOrderIdAndCodeByStatus(String status);
}
