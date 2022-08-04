package hexlet.code.service;

import hexlet.code.model.entity.User;
import hexlet.code.model.DTO.UserDTO;

import javax.management.relation.RelationException;
import javax.naming.NoPermissionException;
import java.util.List;

public interface UserService {

    UserDTO getUserById(String id);

    List<UserDTO> getAllUsers();

    UserDTO createUser(User user);

    UserDTO updateUser(String id, User user) throws NoPermissionException;

    String deleteUser(String id) throws NoPermissionException, RelationException;

    User getCurrentUser();

}
