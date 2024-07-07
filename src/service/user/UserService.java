package service.user;

import entity.User;

import java.util.List;

public interface UserService {
    User getUserByUsername(String username);
    List<User> getAllUsers();
    int saveUser(User user);
    int updateUser(String username, User user);
    int deleteUser(String username);
    List<User> searchUserByUsername(String username);
}
