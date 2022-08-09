package hexlet.code.service.Impl;

import hexlet.code.DTO.UserDTO;
import hexlet.code.mapper.UserDTOMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
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

import static hexlet.code.configuration.SecurityConfiguration.DEFAULT_AUTHORITIES;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id).orElse(null);

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
    public UserDTO updateUser(Long id, User updatedUser) throws NoPermissionException {
        checkIdentityPermissions(id);

        User existsUser = userRepository.findById(id).orElse(null);

        if (existsUser == null) {
            throw new NotFoundException("User Not Found");
        }
        updatedUser.setId(existsUser.getId());
        updatedUser.setCreatedAt(existsUser.getCreatedAt());

        userRepository.save(updatedUser);
        updatedUser = userRepository.findById(id).get();

        return userDTOMapper.userToUserDTO(updatedUser);
    }

    @Override
    public String deleteUser(Long id) throws NoPermissionException, RelationException {
        checkIdentityPermissions(id);

        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User Not Found");
        } else {
            User user = userRepository.findById(id).get();
            List<Task> ownedTasks = user.getOwnedTasks();
            List<Task> claimedTasks = user.getClaimedTasks();

            if (!ownedTasks.isEmpty() || !claimedTasks.isEmpty()) {
                throw new RelationException("User have related tasks, unable to delete");
            }

            userRepository.deleteById(id);
        }

        return "User successfully deleted";
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::buildSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with 'username': " + username));
    }

    private UserDetails buildSpringUser(final User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                DEFAULT_AUTHORITIES
        );
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email).get();
    }

    private void checkIdentityPermissions(Long id) throws NoPermissionException {
        if (!getCurrentUser().getId().equals(id)) {
            throw new NoPermissionException("You can edit and delete only your profile");
        }

    }

}
