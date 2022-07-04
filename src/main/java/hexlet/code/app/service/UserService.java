package hexlet.code.app.service;

import hexlet.code.app.model.UserDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {

    UserDTO getUserById();
    List<UserDTO> getAllUsers();
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    String deleteUser(String id);
}
