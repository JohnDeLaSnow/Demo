package gr.hua.ds.demo.repository;

import gr.hua.ds.demo.model.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, String> {
}
