package gr.hua.ds.demo.repository;

import gr.hua.ds.demo.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
