package gr.hua.ds.demo.service;

import gr.hua.ds.demo.exception.RoleNotFoundException;
import gr.hua.ds.demo.model.Role;
import gr.hua.ds.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService{
    //Repository
    private final RoleRepository roleRepository;
    @Override
    public Role getRole(String roleName) {
        log.info("Fetching role {}", roleName);
        return roleRepository.findById(roleName).orElseThrow(()->new RoleNotFoundException(roleName));
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
}
