package hexlet.code.app.service;

import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {

    UserDTO getUserById(String id);

    List<UserDTO> getAllUsers();

    UserDTO createUser(User user);

    UserDTO updateUser(String id, User user);

    String deleteUser(String id);
}
