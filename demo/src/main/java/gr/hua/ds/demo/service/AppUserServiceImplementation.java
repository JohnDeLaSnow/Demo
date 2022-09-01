package gr.hua.ds.demo.service;

import gr.hua.ds.demo.exception.AppUserNotFoundException;
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


import java.util.*;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class AppUserServiceImplementation implements AppUserService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final RequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

    // USER

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
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

    @Override
    public AppUser getUser(String username) {
        log.info("Fetching user {}", username);
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser getUser(Long id) {
        log.info("Fetching user with id {}", id);
        return appUserRepository.findById(id).
                orElseThrow(()->new AppUserNotFoundException(id));
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll();
    }

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        log.info("Saving new user {} to the database", appUser.getName());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser editAppUser(AppUser newAppUser,Long id) {
        Collection<Role> roles = this.getUser(id).getRoles();
        Collection<Request> requests = this.getUser(id).getRequests();
        return appUserRepository.findById(id)
                .map(appUser -> {
                    appUser.setName(newAppUser.getName());
                    appUser.setUsername(newAppUser.getUsername());
                    appUser.setPassword(newAppUser.getPassword());
                    if (newAppUser.getRoles().isEmpty()) {
                        appUser.setRoles(roles);
                    } else {
                        appUser.setRoles(newAppUser.getRoles());
                    }
                    appUser.setRequests(requests);
                    return saveAppUser(appUser);})
                .orElseThrow(()->new AppUserNotFoundException(id));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id {}", id);
        appUserRepository.deleteById(id);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding new role {} to user {}", roleName,username);
        AppUser appUser = appUserRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        appUser.getRoles().add(role);
    }

    //ROLE

    @Override
    public Role getRole(String roleName){
        log.info("Fetching all roles with specific role name");
        return roleRepository.findByName(roleName);
    }

    @Override
    public List<Role> getRoles() {
        log.info("Fetching all roles");
        return roleRepository.findAll();
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    //REQUEST

    @Override
    public Request getRequest(Long id){
        log.info("Fetching the request with id {}", id);
        return requestRepository.getReferenceById(id);
    }

    @Override
    public List<Request> getRequests() {
        log.info("Fetching all requests");
        return requestRepository.findAll();
    }

    @Override
    public Request saveRequest(Request request) {
        log.info("Saving new request {} to the database", request.getId());
        return requestRepository.save(request);
    }

    @Override
    public void addRequestToUser(String username, Long requestId) {
        log.info("Adding new request {} to user {}", requestId,username);
        AppUser appUser = appUserRepository.findByUsername(username);
        Request request = requestRepository.getReferenceById(requestId);
        appUser.getRequests().add(request);
        request.setAppUser(appUser);
    }

}
