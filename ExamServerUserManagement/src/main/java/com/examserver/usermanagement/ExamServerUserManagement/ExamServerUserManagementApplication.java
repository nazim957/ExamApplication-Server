package com.examserver.usermanagement.ExamServerUserManagement;

import com.examserver.usermanagement.ExamServerUserManagement.entity.Role;
import com.examserver.usermanagement.ExamServerUserManagement.entity.User;
import com.examserver.usermanagement.ExamServerUserManagement.entity.UserRole;
import com.examserver.usermanagement.ExamServerUserManagement.repository.UserRepository;
import com.examserver.usermanagement.ExamServerUserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ExamServerUserManagementApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;


	public static void main(String[] args) {
		SpringApplication.run(ExamServerUserManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if(userRepository.findByEmail("admin@gmail.com")==null) {
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstName("Mohd");
			user.setLastName("Nazim");
			user.setPassword(this.bCryptPasswordEncoder.encode("Admin@123"));
		//	user.setPassword("Admin@123");
			user.setPhone("9876543210");
			user.setProfile("ADMIN.PNG");
			Role role1 = new Role();
			role1.setRoleId(44L);
			role1.setRoleName("ADMIN");

			Set<UserRole> userRoleSet = new HashSet<>();
			UserRole userRole = new UserRole();

			userRole.setRole(role1);

			userRole.setUser(user);

			userRoleSet.add(userRole);

			User user1 = this.userService.createUser(user, userRoleSet);
			System.out.println(user1.getUsername());


		}

		System.out.println("starting code");
	}

}
