package gr.hua.ds.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class AppUser {
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "appUser",cascade = CascadeType.ALL)
    private Collection<Request> requests = new ArrayList<>();

}
