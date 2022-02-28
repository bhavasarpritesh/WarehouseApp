package in.bbd.pritesh.setup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import in.bbd.pritesh.model.User;
import in.bbd.pritesh.service.IUserService;

@Component
public class SetupAdmin implements CommandLineRunner {
	@Autowired
	private IUserService service;
	
	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setName("pritesh");
		user.setEmail("pritesh@gmail.com");
		user.setPwd("pritesh");
		user.setActive(true);
		user.setRoles(List.of("ADMIN","APPUSER"));
		if(service.findByEmail(user.getEmail()).isEmpty()) {
			service.saveUser(user);
		}
	}

}
