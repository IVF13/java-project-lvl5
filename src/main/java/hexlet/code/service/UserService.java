package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.DTO.UserDTO;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> getAllUsers();

    User createUser(UserDTO userDTO);

    User updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    User getCurrentUser();

}
