package in.bbd.pritesh.batch.uom;

import org.springframework.batch.item.ItemProcessor;

import in.bbd.pritesh.model.Uom;

public class UomProcessor implements ItemProcessor<Uom,Uom>{

	@Override
	public Uom process(Uom uom) throws Exception {
		String type = uom.getUomType();
		if(type==null || "".equals(type.trim())) {
			uom.setUomType("NA");
		} else {
			uom.setUomType(type.toUpperCase());
		}

		String desc = uom.getDescription();
		if(desc==null || "".equals(desc.trim())) {
			uom.setDescription("FROM BATACH PROCESSING");
		} 

		String model = uom.getUomModel();
		uom.setUomModel(model.toUpperCase());

		//service.isUomModelExist(uomModel)
		//if true return null
		//else return object


		return uom;
	}
}
