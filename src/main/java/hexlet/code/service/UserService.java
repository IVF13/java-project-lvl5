package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.DTO.UserDTO;

import javax.management.relation.RelationException;
import javax.naming.NoPermissionException;
import java.util.List;

public interface UserService {

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    UserDTO createUser(User user);

    UserDTO updateUser(Long id, User user) throws NoPermissionException;

    String deleteUser(Long id) throws NoPermissionException, RelationException;

    User getCurrentUser();

}
