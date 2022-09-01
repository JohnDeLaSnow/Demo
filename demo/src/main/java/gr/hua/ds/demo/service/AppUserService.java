package gr.hua.ds.demo.service;

import gr.hua.ds.demo.model.AppUser;
import gr.hua.ds.demo.model.Request;
import gr.hua.ds.demo.model.Role;

import java.util.List;

public interface AppUserService {
    //USERS
    AppUser getUser(Long id);
    AppUser getUser(String username);
    List<AppUser> getUsers();
    AppUser saveAppUser(AppUser appUser);
    AppUser editAppUser(AppUser appUser,Long id);
    void deleteUser(Long id);
    void addRoleToUser(String username, String roleName);
    //ROLES
    Role getRole(String roleName);
    List<Role> getRoles();
    Role saveRole(Role role);
    //REQUESTS
    Request getRequest(Long id);
    List<Request> getRequests();
    Request saveRequest(Request request);
    void addRequestToUser(String username,Long requestId);
}
