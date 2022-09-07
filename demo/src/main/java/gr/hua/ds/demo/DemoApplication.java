package gr.hua.ds.demo;

import gr.hua.ds.demo.model.AppUser;
import gr.hua.ds.demo.model.Request;
import gr.hua.ds.demo.model.Role;
import gr.hua.ds.demo.service.AppUserService;
import gr.hua.ds.demo.service.RequestService;
import gr.hua.ds.demo.service.RoleService;
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
	CommandLineRunner run(AppUserService appUserService, RoleService roleService, RequestService requestService){
		return args -> {
			roleService.saveRole(new Role("ROLE_USER","Default role granted to any user"));
			roleService.saveRole(new Role("ROLE_MANAGER","This role has more authorities than a user"));
			roleService.saveRole(new Role("ROLE_ADMIN","This is a semi-administrator role"));
			roleService.saveRole(new Role("ROLE_SUPER_ADMIN","This is the system administrator role"));

			appUserService.saveAppUser(new AppUser("johndoe", "John","Doe","1234",new ArrayList<>(),new ArrayList<>()));
			appUserService.saveAppUser(new AppUser("janedoe", "Jane","Doe","1234",new ArrayList<>(),new ArrayList<>()));
			appUserService.saveAppUser(new AppUser("jackdoe", "Jack","Doe","1234",new ArrayList<>(),new ArrayList<>()));
			appUserService.saveAppUser(new AppUser("jimdoe", "Jim","Doe","1234",new ArrayList<>(),new ArrayList<>()));

			requestService.saveRequest(new Request(null,"First Example",null));
			requestService.saveRequest(new Request(null,"Second Example",null));
			requestService.saveRequest(new Request(null,"Third Example",null));
			requestService.saveRequest(new Request(null,"Fourth Example",null));

			appUserService.addRequestToUser("johndoe",Long.valueOf(1));
			appUserService.addRequestToUser("johndoe",Long.valueOf(2));
			appUserService.addRequestToUser("jackdoe",Long.valueOf(3));
			appUserService.addRequestToUser("janedoe",Long.valueOf(4));


			appUserService.addRoleToUser("johndoe","ROLE_USER");
			appUserService.addRoleToUser("janedoe","ROLE_MANAGER");
			appUserService.addRoleToUser("jackdoe","ROLE_ADMIN");
			appUserService.addRoleToUser("jimdoe","ROLE_MANAGER");
			appUserService.addRoleToUser("jimdoe","ROLE_SUPER_ADMIN");
			appUserService.addRoleToUser("jimdoe","ROLE_ADMIN");
			appUserService.addRoleToUser("jimdoe","ROLE_USER");
		};
	}
}
