package com.example.demo.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserRole;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.UserRoleRepository;
import com.example.demo.SecurityConfig.JwtProvider;

@Service
public class UserServiceImplementation implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        System.out.println(user);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with this email" + email);

        }

        System.out.println("Loaded user: " + user.getEmail() + ", Role: ");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole userRole : user.getUserRoles()) {

            System.out.println("role details: " + userRole.getRole());
            Role role = userRole.getRole(); // Get the Role from UserRole
            authorities.add(new SimpleGrantedAuthority(role.getRoleName())); // Convert Role to authority
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    // Implement methods from UserService
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserProfileByJwt(String jwt) {
        // Assuming you have a method to extract the email from JWT and find the user by email
        String email = JwtProvider.extractUserEmail(jwt); // Adjust based on your implementation
        System.out.println("Email extracted from JWT: " + email);
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserById(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElse(null);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User saveUserWithRoles(User user, List<String> roleNames) {
        // TODO Auto-generated method stub
        // Save the user first
        User savedUser = userRepository.save(user);

        // For each role name, find the role, and create a UserRole entry
        for (String roleName : roleNames) {
            Role role = roleRepository.findByRoleName(roleName); // Fetch role by name
            if (role != null) {
                UserRole userRole = new UserRole();
                userRole.setUser(savedUser);
                userRole.setRole(role);
                userRole.setStatus("A"); // Example status
                userRole.setAssignedDate(new Date()); // Set assigned date

                // Save the user-role mapping
                userRoleRepository.save(userRole);
            }
        }

        return savedUser;
    }
}
