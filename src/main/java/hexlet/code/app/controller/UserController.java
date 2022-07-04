package hexlet.code.app.controller;

import hexlet.code.app.model.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @GetMapping(path = "/api/users/{id}")
    public UserDTO getUserById(@PathVariable String id) {
        return new UserDTO();
    }

    @GetMapping(path = "/api/users")
    public List<UserDTO> getAllUsers() {
        return List.of(new UserDTO());
    }

    @PostMapping(path = "/api/users")
    public UserDTO createUser(UserDTO userDTO) {
        return new UserDTO();
    }

    @PutMapping(path = "/api/users/{id}")
    public UserDTO updateUser(@PathVariable String id, UserDTO userDTO) {
        return new UserDTO();
    }

    @DeleteMapping(path = "/api/users/{id}")
    public String deleteUser(@PathVariable String id) {
        return "";
    }

}
