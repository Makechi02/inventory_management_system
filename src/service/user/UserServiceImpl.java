package service.user;

import connections.Connections;
import entity.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final Connections connections = Connections.getInstance();

    @Override
    public User getUserByUsername(String username) {
        User user = connections.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User with username: " + username + " not found");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return connections.getAllUsers();
    }

    @Override
    public int saveUser(User user) {
        if (connections.UserExistsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        return connections.saveUser(user);
    }

    @Override
    public int updateUser(String username, User user) {
        User fetchedUser = connections.getUserByUsername(username);
        if (fetchedUser == null) {
            throw new RuntimeException("User with username: " + username + " not found");
        }

        if (connections.UserExistsByUsername(user.getUsername())) {
            throw new RuntimeException("username already exists");
        }

        return connections.updateUser(fetchedUser.getId(), user.getName(), user.getUsername(), user.getPassword());
    }

    @Override
    public int deleteUser(String username) {
        if (!connections.UserExistsByUsername(username)) {
            throw new RuntimeException("User with username: " + username + " not found");
        }
        return connections.deleteUser(username);
    }
}
