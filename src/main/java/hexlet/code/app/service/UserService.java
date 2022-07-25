package hexlet.code.app.service;

import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;

import javax.naming.NoPermissionException;
import java.util.List;

public interface UserService {

    UserDTO getUserById(String id) throws NoPermissionException;

    List<UserDTO> getAllUsers();

    UserDTO createUser(User user);

    UserDTO updateUser(String id, User user) throws NoPermissionException;

    String deleteUser(String id) throws NoPermissionException;
}
