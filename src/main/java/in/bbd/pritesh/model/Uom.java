package in.bbd.pritesh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="uom_tab")
public class Uom {
	
	@Id
	@GeneratedValue(generator = "uom")
	//@GenericGenerator(name = "uom",strategy = "in.nareshit.raghu.UomIdGenerator")
	@SequenceGenerator(name = "uom",sequenceName = "uom_seq")
	@Column(name="uom_id_col")
	private Integer id;
	
	@Column(name="uom_type_col")
	private String uomType;
	@Column(name="uom_model_col",unique = true,nullable = false)
	private String uomModel;
	@Column(name="uom_description_col")
	private String description;
	
}
