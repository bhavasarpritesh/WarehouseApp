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
@Table(name="shipment_type_tab")
public class ShipmentType {
	@Id
	@GeneratedValue(generator = "shipmenttype")
	@SequenceGenerator(name = "shipmenttype",
		sequenceName = "shipmenttype_seq")
	@Column(name="shp_id_col")
	private Integer id;
	
	@Column(name="shp_mode_col")
	private String shipmentMode;
	@Column(name="shp_code_col",unique = true)
	private String shipmentCode;
	@Column(name="shp_enable_ship_col")
	private String enableShipment;
	@Column(name="shp_grade_col")
	private String shipmentGrade;
	@Column(name="shp_description_col")
	private String description;
	
}
