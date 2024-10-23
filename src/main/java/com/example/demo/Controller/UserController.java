package com.example.demo.Controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.gson.Gson;

import io.jsonwebtoken.io.IOException;

import com.example.demo.Entity.AuthResponse;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserRole;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.UserRoleRepository;
import com.example.demo.SecurityConfig.JwtProvider;
import com.example.demo.Service.UserService;
import com.example.demo.Service.UserServiceImplementation;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

@RestController
@RequestMapping("/auth")

public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImplementation customUserDetails;

    @Autowired
    private UserService userService;
    private final String CLIENT_ID = "1097657085730-piqggsolbcphetb10a66d1j7ib699css.apps.googleusercontent.com";

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String mobile = user.getMobile();

        if (user.getUserRoles() != null) {
            System.out.println("Received User Roles:");
            for (UserRole userRole : user.getUserRoles()) {
                if (userRole.getRole() != null) {
                    System.out.println("Role Name: " + userRole.getRole().getRoleName());
                } else {
                    System.out.println("Received UserRole has no associated Role");
                }
            }
        } else {
            System.out.println("No user roles received.");
        }

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Email is already used with another account");
            errorResponse.setStatus(false);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setMobile(mobile);
        createdUser.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(createdUser);
        // Prepare to assign roles to the user
        Set<UserRole> newUserRoles = new HashSet<>();
        if (user.getUserRoles() != null) {
            for (UserRole userRole : user.getUserRoles()) {

                System.out.println("in the loop" + userRole.getRole().getRoleName());

                Role existingRole = roleRepository.findByRoleName(userRole.getRole().getRoleName());
                if (existingRole != null) {
                    UserRole newUserRole = new UserRole();
                    newUserRole.setUser(createdUser); // Link user
                    newUserRole.setRole(existingRole); // Assign role
                    newUserRole.setAssignedDate(new Date());
                    newUserRole.setStatus("A");
                    newUserRoles.add(newUserRole);
                    userRoleRepository.save(newUserRole); // Save the UserRole object
                }
            }
        }
        // Assign roles to the user
        // createdUser.setUserRoles(newUserRoles);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("You Have resigster succesfully MR. " + fullName);
        authResponse.setStatus(true);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        System.out.println("only user name is" + email);
        System.out.println(email + "-------" + password);

        User detailsofUser = userRepository.findByEmail(email);
        String Full_name = detailsofUser.getFullName();

        System.out.println("Full name is: " + Full_name);

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login success" + Full_name);
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String email, String password) {

        System.out.println(email + "---++----" + password);

        UserDetails userDetails = customUserDetails.loadUserByUsername(email);

        System.out.println("Sig in in user details" + userDetails);

        if (userDetails == null) {
            System.out.println("Sign in details - null" + userDetails);

            throw new BadCredentialsException("Invalid username and password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("Sign in userDetails - password mismatch" + userDetails);

            throw new BadCredentialsException("Invalid password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleSignin(@RequestBody Map<String, String> token) {
        try {
            NetHttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            String idTokenString = token.get("token");

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = null;
            ;
            try {
                idToken = verifier.verify(idTokenString);
                System.out.println("idToken: " + idToken);
            } catch (GeneralSecurityException | java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (idToken == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {

                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                System.out.println("email: " + email);
                String name = (String) payload.get("name");
                System.out.println("name: " + name);
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    user = new User();
                    user.setEmail(email);
                    user.setFullName((String) payload.get("name")); // or payload.get("given_name") + " " +
                                                                    // payload.get("family_name")
                    user.setPassword(passwordEncoder.encode("defaultPassword")); // Use a default password or generate
                                                                                 // one
                    user.setMobile("N/A"); // You might want to handle mobile differently

                    userRepository.save(user); // Save the new user
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null,
                        Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token1 = JwtProvider.generateToken(authentication);

                AuthResponse authResponse = new AuthResponse();
                authResponse.setJwt(token1);
                authResponse.setMessage("Login successful");
                authResponse.setStatus(true);
                return new ResponseEntity<>(authResponse, HttpStatus.OK);

            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("Error verifying ID token: " + e.getMessage(), false));
        }

    }

}
