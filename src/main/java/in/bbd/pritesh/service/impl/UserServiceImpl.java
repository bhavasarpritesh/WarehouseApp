package in.bbd.pritesh.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.bbd.pritesh.model.User;
import in.bbd.pritesh.repo.UserRepository;
import in.bbd.pritesh.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Override
	public Integer saveUser(User user) {
		//read password
		String pwd = user.getPwd();
		//encode password
		String encPwd = encoder.encode(pwd);
		//set back to same user object
		user.setPwd(encPwd);
		
		return repo.save(user).getId();
	}

	@Override
	public List<User> getAllUsers() {
		return repo.findAll();
	}

	@Transactional
	public void modifyStatus(Integer id, boolean active) {
		repo.updateStatus(id, active);
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	//-------------------------------------------------------//
	
	@Override
	public UserDetails loadUserByUsername(String username) 
				throws UsernameNotFoundException {
		
		//read model class user
		Optional<User> opt = findByEmail(username);
		
		if( opt.isEmpty() || !opt.get().isActive()) {
			throw new UsernameNotFoundException("User not exist");
		} else {
			User user = opt.get();
			return new org.springframework.security.core.userdetails.User(
					username, 
					user.getPwd(), 
					user.getRoles()
					.stream()
					.map(role->new SimpleGrantedAuthority(role))
					.collect(Collectors.toSet())
					);
		}
		
	}
	
	@Override
	public User getOneUser(Integer id) {
		return repo.findById(id).get();
	}
	
	@Override
	@Transactional
	public void updatePwd(Integer id, String pwd) {
		repo.updatePwd(id, encoder.encode(pwd));
	}

	@Override
	@Transactional
	public void updateNewOtpById(Integer id, String otpNew) {
		repo.updateNewOtpById(id, otpNew);
		
	}
}
