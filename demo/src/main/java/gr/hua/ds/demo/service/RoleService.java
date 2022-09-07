package gr.hua.ds.demo.service;

import gr.hua.ds.demo.model.Role;

import java.util.List;

public interface RoleService {
    Role getRole(String roleName);
    List<Role> getRoles();
    Role saveRole(Role role);
}
