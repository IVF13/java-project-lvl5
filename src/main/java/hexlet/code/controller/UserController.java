package hexlet.code.controller;

import hexlet.code.model.entity.User;
import hexlet.code.model.DTO.UserDTO;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationException;
import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;

@Validated
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String USER_ID_IN_CONTROLLER = "/{id}";

    private final UserService userService;

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User with that id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(path = USER_ID_IN_CONTROLLER)
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "Id of user to be found")
                                               @PathVariable String id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(userDTO);
    }


    @Operation(summary = "Get list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all users",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(path = "")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        return ResponseEntity.ok().body(userDTOS);
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "User already exists"),
            @ApiResponse(responseCode = "422", description = "Not valid user data")
    })
    @PostMapping(path = "")
    public ResponseEntity<UserDTO> createUser(@Parameter(description = "User data to save")
                                              @RequestBody @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User updated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "423", description = "Unable to update another user, only yourself"),
            @ApiResponse(responseCode = "404", description = "User with that id not found"),
            @ApiResponse(responseCode = "422", description = "Not valid user data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(path = USER_ID_IN_CONTROLLER)
    public ResponseEntity<UserDTO> updateUser(@Parameter(description = "Id of user to be updated")
                                              @PathVariable String id,
                                              @Parameter(description = "User data to update")
                                              @RequestBody @Valid User user)
            throws NoPermissionException {
        UserDTO userDTO = userService.updateUser(id, user);
        return ResponseEntity.ok().body(userDTO);
    }

    @Operation(summary = "Delete user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User with that id not found"),
            @ApiResponse(responseCode = "423", description = "Unable to delete another user, only yourself"),
            @ApiResponse(responseCode = "304", description = "Unable to delete user because he has related tasks"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(path = USER_ID_IN_CONTROLLER)
    public String deleteUser(@Parameter(description = "Id of user to be deleted")
                             @PathVariable String id) throws NoPermissionException, RelationException {
        return userService.deleteUser(id);
    }

}
