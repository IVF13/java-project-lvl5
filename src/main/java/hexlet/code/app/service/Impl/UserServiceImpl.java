package hexlet.code.app.service.Impl;

import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import hexlet.code.app.util.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.naming.NoPermissionException;
import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(String id) throws NoPermissionException {
        checkIdentityPermissions(id);

        User user = userRepository.findById(Long.parseLong(id)).orElse(null);

        if (user == null) {
            throw new NotFoundException("User Not Found");
        }

        return userDTOMapper.userToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(userDTOMapper::userToUserDTO).toList();
    }

    @Override
    public UserDTO createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityExistsException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        user = userRepository.findByEmail(user.getEmail()).get();

        return userDTOMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(String id, User user) throws NoPermissionException {
        checkIdentityPermissions(id);

        User userToUpdate = userRepository.findById(Long.parseLong(id)).orElse(null);

        if (userToUpdate == null) {
            throw new NotFoundException("User Not Found");
        }

        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());

        return userDTOMapper.userToUserDTO(userRepository.save(userToUpdate));
    }

    @Override
    public String deleteUser(String id) throws NoPermissionException {
        checkIdentityPermissions(id);

        if (!userRepository.existsById(Long.parseLong(id))) {
            throw new NotFoundException("User Not Found");
        } else {
            List<Task> ownedTasks = userRepository.findById(Long.parseLong(id)).get().getOwnedTasks();

            if (!ownedTasks.isEmpty()) {
                throw new RuntimeException("User have tasks, can't delete");
            }

            userRepository.deleteById(Long.parseLong(id));
        }

        return "User successfully deleted";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private void checkIdentityPermissions(String id) throws NoPermissionException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();

        if (!authUser.getId().equals(Long.parseLong(id))) {
            throw new NoPermissionException("You can edit and delete only your profile");
        }

    }

}
