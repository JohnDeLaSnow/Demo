package gr.hua.ds.demo.service;

import gr.hua.ds.demo.model.AppUser;

import java.util.List;

public interface AppUserService {
    AppUser getUser(String username);
    List<AppUser> getUsers();
    AppUser saveAppUser(AppUser appUser);
    AppUser editAppUser(AppUser appUser,String username);
    void deleteUser(String username);
    AppUser addRoleToUser(String username, String roleName);
    void addRequestToUser(String username,Long requestId);
}
