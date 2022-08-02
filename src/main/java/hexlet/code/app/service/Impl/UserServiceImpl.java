package hexlet.code.app.service.Impl;

import hexlet.code.app.model.entity.Task;
import hexlet.code.app.model.entity.User;
import hexlet.code.app.model.DTO.UserDTO;
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

import javax.management.relation.RelationException;
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
    public UserDTO updateUser(String id, User updatedUser) throws NoPermissionException {
        checkIdentityPermissions(id);

        User existsUser = userRepository.findById(Long.parseLong(id)).orElse(null);

        if (existsUser == null) {
            throw new NotFoundException("User Not Found");
        }
        updatedUser.setId(existsUser.getId());
        updatedUser.setCreatedAt(existsUser.getCreatedAt());

        userRepository.save(updatedUser);
        updatedUser = userRepository.findById(Long.parseLong(id)).get();

        return userDTOMapper.userToUserDTO(updatedUser);
    }

    @Override
    public String deleteUser(String id) throws NoPermissionException, RelationException {
        checkIdentityPermissions(id);

        if (!userRepository.existsById(Long.parseLong(id))) {
            throw new NotFoundException("User Not Found");
        } else {
            List<Task> ownedTasks = userRepository.findById(Long.parseLong(id)).get().getOwnedTasks();

            if (!ownedTasks.isEmpty()) {
                throw new RelationException("User have owned tasks, unable to delete");
            }

            userRepository.deleteById(Long.parseLong(id));
        }

        return "User successfully deleted";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private void checkIdentityPermissions(String id) throws NoPermissionException {
        if (!getCurrentUser().getId().equals(Long.parseLong(id))) {
            throw new NoPermissionException("You can edit and delete only your profile");
        }

    }

}
