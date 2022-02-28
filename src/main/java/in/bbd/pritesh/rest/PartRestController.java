package in.bbd.pritesh.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bbd.pritesh.exception.PartNotFoundException;
import in.bbd.pritesh.model.Part;
import in.bbd.pritesh.service.IPartService;

@RestController
@RequestMapping("/rest/part")
public class PartRestController {
	
	private Logger log = LoggerFactory.getLogger(PartRestController.class);
	
	@Autowired
	private IPartService service;
	
	//1. save Part Data 
	@PostMapping("/save")
	public ResponseEntity<String> savePart(
			@RequestBody Part part
			) 
	{
		log.info("ENTERED INTO SAVE METHOD");
		ResponseEntity<String>  resp = null;
		try {
			Integer id = service.savePart(part);
			String message = "Part '"+id+"' created Successfully!";
			log.info(message);
			resp = ResponseEntity.ok(message);
		} catch (Exception e) {
			log.error("Unable to create Part : {}",e.getMessage());
			resp = new ResponseEntity<String>(
					"UNABLE TO SAVE PART",HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		log.info("ABOUT TO EXIT FROM SAVE METHOD");
		return resp;
	}	
	
	
	//2. Fetch Part data by id
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOnePart(
			@PathVariable Integer id
			) 
	{
		ResponseEntity<?> resp = null;
		try {
			Part p = service.getOnePart(id);
			resp = new ResponseEntity<Part>(p,HttpStatus.OK);
			//resp =ResponseEntity.ok(p);
		} catch(PartNotFoundException pnfe) {
			throw pnfe;
		} catch (Exception e) {
			resp = new ResponseEntity<String>(
					"UNABLE TO FETCH PART",HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
		return resp;
	}
	
	
}
