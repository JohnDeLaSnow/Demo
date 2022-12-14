package gr.hua.ds.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.hua.ds.demo.model.AppUser;
import gr.hua.ds.demo.model.Request;
import gr.hua.ds.demo.model.Role;
import gr.hua.ds.demo.service.AppUserService;
import gr.hua.ds.demo.service.RequestService;
import gr.hua.ds.demo.service.RoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final RequestService requestService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>>getUsers() {
        return ResponseEntity.ok().body(appUserService.getUsers());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<AppUser>getUser(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(appUserService.getUser(username));
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>deleteUser(@PathVariable("username") String username){
        appUserService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/request/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>deleteRequest(@PathVariable("id") Long id){
        requestService.deleteRequest(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{username}")
    public ResponseEntity<AppUser>editUser(@RequestBody AppUser newAppUser, @PathVariable("username") String username){
        return ResponseEntity.ok().body(appUserService.editAppUser(newAppUser,username));
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser>saveAppUser(@RequestBody AppUser appUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveAppUser(appUser));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>>getRoles() {
        return ResponseEntity.ok().body(roleService.getRoles());
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<Role>getRole(@PathVariable("roleName") String roleName) {
        return ResponseEntity.ok().body(roleService.getRole(roleName));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(roleService.saveRole(role));
    }

    @PostMapping("/user/add_role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser>addRoleToUser(@RequestBody RoleToUserForm form) {
        return ResponseEntity.ok().body(appUserService.addRoleToUser(form.getUsername(), form.getRoleName()));
    }
    @PutMapping("/request/add_user")
    public ResponseEntity<Request>addUserToRequest(@RequestBody UserToRequestForm form) {
        return ResponseEntity.ok().body(requestService.addUserToRequest(form.getUsername(), form.getRequestId()));
    }
    @GetMapping("/requests")
    public ResponseEntity<List<Request>>getRequests() {
        return ResponseEntity.ok().body(requestService.getRequests());
    }

    @GetMapping("/request/{id}")
    public ResponseEntity<Request>getRequest(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(requestService.getRequest(id));
    }

    @PostMapping("/request/save")
    public ResponseEntity<Request>saveRequest(@RequestBody Request request){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/request/save").toUriString());
        return ResponseEntity.created(uri).body(requestService.saveRequest(request));
    }

    @PutMapping("/request/status/registered/{id}")
    public ResponseEntity<Request>updateStatusRegistered(@PathVariable Long id){
        return ResponseEntity.ok().body(requestService.updateStatusRegistered(id));
    }

    @PutMapping("/request/status/passed/{id}")
    public ResponseEntity<Request>updateStatusPassed(@PathVariable Long id){
        return ResponseEntity.ok().body(requestService.updateStatusPassed(id));
    }

    @PutMapping("/request/status/approved/{id}")
    public ResponseEntity<Request>updateStatusApproved(@PathVariable Long id){
        return ResponseEntity.ok().body(requestService.updateStatusApproved(id));
    }

    @GetMapping("/request/status/{status}")
    public ResponseEntity<List<Request>>getRequestByStatus(@PathVariable String status){
        return ResponseEntity.ok().body(requestService.getRequestsByStatus(status));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser appUser = appUserService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", appUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception){
                log.error("Error logging in: {}",exception.getMessage());
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}
@Data
class UserToRequestForm {
    private String username;
    private Long requestId;
}
