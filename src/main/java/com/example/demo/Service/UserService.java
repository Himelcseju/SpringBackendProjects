package com.example.demo.Service;

import java.util.List;

import com.example.demo.Entity.User;

public interface UserService {

    //private UserRepository userRepository;
    public List<User> getAllUser();

    public User findUserProfileByJwt(String jwt);

    public User findUserByEmail(String email);

    public User findUserById(String userId);

    public List<User> findAllUsers();

    User saveUserWithRoles(User user, List<String> roleNames);

}
