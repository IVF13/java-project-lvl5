package hexlet.code.app.controller;

import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;
import hexlet.code.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.util.List;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;

@Validated
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";

    private final UserService userService;

    @GetMapping(path = ID)
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) throws NoPermissionException {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        return ResponseEntity.ok().body(userDTOS);
    }

    @PostMapping(path = "")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping(path = ID)
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody @Valid User user)
            throws NoPermissionException {
        UserDTO userDTO = userService.updateUser(id, user);
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping(path = ID)
    public String deleteUser(@PathVariable String id) throws NoPermissionException {
        return userService.deleteUser(id);
    }

}
