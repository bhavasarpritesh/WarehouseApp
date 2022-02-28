package in.bbd.pritesh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="part_tab")
public class Part {
	@Id
	@GeneratedValue
	@Column(name="prt_id_col")
	private Integer id;
	
	@Column(name="prt_code_col")
	private String partCode;
	@Column(name="prt_len_col")
	private Double partLen;
	
	@Column(name="prt_wid_col")
	private Double partWid;
	@Column(name="prt_hght_col")
	private Double partHght;
	
	@Column(name="prt_cost_col")
	private Double partCost;
	@Column(name="prt_curr_col")
	private String partCurrency;
	
	//Association Mappings
	@ManyToOne
	@JoinColumn(name="uom_id_fk_col")
	private Uom uom; //HAS-A
	
	@ManyToOne
	@JoinColumn(name="om_sale_id_fk_col")
	private OrderMethod omSale; //HAS-A
	
}
