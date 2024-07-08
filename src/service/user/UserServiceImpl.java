package service.user;

import connections.Connections;
import entity.User;
import exception.DuplicateResourceException;
import exception.RequestValidationException;
import exception.ResourceNotFoundException;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final Connections connections = Connections.getInstance();

    @Override
    public User getUserByUsername(String username) {
        User user = connections.getUserByUsername(username);
        if (user == null) throw new ResourceNotFoundException("User with username: " + username + " not found");
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return connections.getAllUsers();
    }

    @Override
    public int saveUser(User user) {
        if (connections.userExistsByUsername(user.getUsername())) throw new DuplicateResourceException("Username already taken");

        if (user.getPassword() == null) user.setPassword(user.getUsername());

        return connections.saveUser(user);
    }

    @Override
    public int updateUser(int id, User user) {
        boolean changes = false;
        User fetchedUser = connections.getUserById(id);
        if (fetchedUser == null) {
            throw new ResourceNotFoundException("User with id: " + id + " not found");
        }

        if (!user.getUsername().isBlank() && !user.getUsername().equals(fetchedUser.getUsername())) {
            if (connections.userExistsByUsername(user.getUsername()))
                throw new DuplicateResourceException("Username already taken");
            changes = true;
        }

        if (!user.getName().isBlank() && !user.getName().equals(fetchedUser.getName())) {
            changes = true;
        }

        if (!changes) throw new RequestValidationException("No changes found");

        return connections.updateUser(fetchedUser.getId(), user);
    }

    @Override
    public int deleteUser(String username) {
        if (!connections.userExistsByUsername(username)) {
            throw new ResourceNotFoundException("User with username: " + username + " not found");
        }
        return connections.deleteUser(username);
    }

    @Override
    public List<User> searchUserByUsername(String username) {
        return connections.searchUsersByUsername(username);
    }
}
