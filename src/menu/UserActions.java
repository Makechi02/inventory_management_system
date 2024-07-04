package menu;

import entity.User;

public interface UserActions {
    void displayUserProfile(User user);
    void updateProfile(User user);
    void deleteAccount(User user);
}
