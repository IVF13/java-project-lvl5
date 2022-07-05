package hexlet.code.app.service.Impl;

import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import hexlet.code.app.util.UserDTOMapper;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(String id) throws NotFoundException {
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        user = userRepository.findByEmail(user.getEmail()).get();

        return userDTOMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(String id, User user) throws NotFoundException {
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
    public String deleteUser(String id) throws NotFoundException {

        if (!userRepository.existsById(Long.parseLong(id))) {
            throw new NotFoundException("User Not Found");
        } else {
            userRepository.deleteById(Long.parseLong(id));
        }

        return "User successfully deleted";
    }
}
