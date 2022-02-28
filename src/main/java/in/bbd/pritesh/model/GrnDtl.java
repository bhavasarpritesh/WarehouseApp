package in.bbd.pritesh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
//c# Create GrnDtl Model class 
@Data
@Entity
@Table(name="grn_dtl_tab")
public class GrnDtl {
	@Id
	@GeneratedValue
	@Column(name="grn_id_col")
	private Integer id;
	
	@Column(name="grn_code_col")
	private String itemCode;
	@Column(name="grn_cost_col")
	private Double baseCost;
	@Column(name="grn_qty_col")
	private Integer qty;
	@Column(name="grn_val_col")
	private Double itemVal;
	
	@Column(name="grn_status_col")
	private String status;
	
	@ManyToOne
	@JoinColumn(name="grn_id_fk_col")
	private Grn grn; //HAS-A
}
