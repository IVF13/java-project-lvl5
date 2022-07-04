package hexlet.code.app.service.Impl;

import hexlet.code.app.model.UserDTO;
import hexlet.code.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    @Override
    public UserDTO getUserById() {
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return null;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        return null;
    }

    @Override
    public String deleteUser(String id) {
        return null;
    }
}
