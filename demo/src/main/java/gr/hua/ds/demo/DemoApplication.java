package gr.hua.ds.demo;

import gr.hua.ds.demo.model.AppUser;
import gr.hua.ds.demo.model.Request;
import gr.hua.ds.demo.model.Role;
import gr.hua.ds.demo.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	};

	@Bean
	CommandLineRunner run(AppUserService appUserService){
		return args -> {
			appUserService.saveRole(new Role(null,"ROLE_USER"));
			appUserService.saveRole(new Role(null,"ROLE_MANAGER"));
			appUserService.saveRole(new Role(null,"ROLE_ADMIN"));
			appUserService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));

			appUserService.saveAppUser(new AppUser(null, "John Doe","johndoe","1234",new ArrayList<>(),new ArrayList<>()));
			appUserService.saveAppUser(new AppUser(null, "Jane Doe","janedoe","1234",new ArrayList<>(),new ArrayList<>()));
			appUserService.saveAppUser(new AppUser(null, "Jack Doe","jackdoe","1234",new ArrayList<>(),new ArrayList<>()));
			appUserService.saveAppUser(new AppUser(null, "Jim Doe","jimdoe","1234",new ArrayList<>(),new ArrayList<>()));

			appUserService.saveRequest(new Request(null,null));
			appUserService.saveRequest(new Request(null,null));
			appUserService.saveRequest(new Request(null,null));
			appUserService.saveRequest(new Request(null,null));

			appUserService.addRequestToUser("johndoe",Long.valueOf(1));
			appUserService.addRequestToUser("johndoe",Long.valueOf(2));
			appUserService.addRequestToUser("jackdoe",Long.valueOf(3));
			appUserService.addRequestToUser("janedoe",Long.valueOf(4));

			appUserService.addRoleToUser("johndoe","ROLE_USER");
			appUserService.addRoleToUser("janedoe","ROLE_MANAGER");
			appUserService.addRoleToUser("jackdoe","ROLE_ADMIN");
			appUserService.addRoleToUser("jimdoe","ROLE_SUPER_ADMIN");
			appUserService.addRoleToUser("jimdoe","ROLE_ADMIN");
			appUserService.addRoleToUser("jimdoe","ROLE_USER");
		};
	}
}
