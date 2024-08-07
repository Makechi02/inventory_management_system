package com.makechi.inventory_management_system.service.user;

import com.makechi.inventory_management_system.entity.User;

import java.util.List;

public interface UserService {
    User getUserByUsername(String username);
    List<User> getAllUsers();
    int saveUser(User user);
    int updateUser(int id, User user);
    int deleteUser(String username);
    List<User> searchUserByUsername(String username);
}
