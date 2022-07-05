package hexlet.code.app.controller;

import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;
import hexlet.code.app.service.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/users/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) throws NotFoundException {
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
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserDTO> updateUser
            (@PathVariable String id, @RequestBody @Valid User user) throws NotFoundException {
        UserDTO userDTO = userService.updateUser(id, user);
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id) throws NotFoundException {
        return userService.deleteUser(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundExceptions(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
