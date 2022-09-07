package gr.hua.ds.demo.service;

import gr.hua.ds.demo.exception.AppUserNotFoundException;
import gr.hua.ds.demo.exception.RequestNotFoundException;
import gr.hua.ds.demo.exception.RoleNotFoundException;
import gr.hua.ds.demo.model.AppUser;
import gr.hua.ds.demo.model.Request;
import gr.hua.ds.demo.model.Role;
import gr.hua.ds.demo.repository.AppUserRepository;
import gr.hua.ds.demo.repository.RequestRepository;
import gr.hua.ds.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    //Repositories
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final RequestRepository requestRepository;
    //PasswordEncoder
    private final PasswordEncoder passwordEncoder;
    //Methods
    @Override
    public AppUser getUser(String username) {
        log.info("Fetching user {}", username);
        return appUserRepository.findById(username).orElseThrow(()->new AppUserNotFoundException(username));
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll();
    }

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        log.info("Saving new user {} to the database", appUser.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser editAppUser(AppUser newAppUser,String username) {
        log.info("Editing user {}", username);
        Collection<Role> roles = this.getUser(username).getRoles();
        Collection<Request> requests = this.getUser(username).getRequests();
        String oldPassword = this.getUser(username).getPassword();
        return appUserRepository.findById(username)
                .map(appUser -> {
                    appUser.setUsername(newAppUser.getUsername());
                    appUser.setFirstName(newAppUser.getFirstName());
                    appUser.setLastName(newAppUser.getLastName());
                    if (newAppUser.getPassword().equals("")){
                        appUser.setPassword(oldPassword);
                    }else {
                        appUser.setPassword(passwordEncoder.encode(newAppUser.getPassword()));
                    }
                    if (newAppUser.getRoles().isEmpty()) {
                        appUser.setRoles(roles);
                    } else {
                        appUser.setRoles(newAppUser.getRoles());
                    }
                    appUser.setRequests(requests);
                    return saveAppUser(appUser);})
                .orElseThrow(()->new AppUserNotFoundException(username));
    }

    @Override
    public void deleteUser(String username) {
        log.info("Deleting user {}", username);
        appUserRepository.deleteById(username);
    }

    @Override
    public AppUser addRoleToUser(String username, String roleName) {
        log.info("Adding new role {} to user {}", roleName,username);
        AppUser appUser = appUserRepository.findById(username).orElseThrow(()->new AppUserNotFoundException(username));
        Role role = roleRepository.findById(roleName).orElseThrow(()->new RoleNotFoundException(roleName));
        appUser.getRoles().add(role);
        return appUser;
    }

    @Override
    public void addRequestToUser(String username, Long requestId) {
        log.info("Adding new request with id {} to user {}", requestId, username);
        AppUser appUser = appUserRepository.findById(username).orElseThrow(()->new AppUserNotFoundException(username));
        Request request = requestRepository.findById(requestId).orElseThrow(()->new RequestNotFoundException(requestId));
        appUser.getRequests().add(request);
        request.setAppUser(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findById(username).orElseThrow(()->new UsernameNotFoundException("Could not find user in the database"));
        if(appUser == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(),appUser.getPassword(),authorities);
    }
}
