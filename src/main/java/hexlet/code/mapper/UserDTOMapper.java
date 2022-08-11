package hexlet.code.mapper;

import hexlet.code.DTO.UserDTO;
import hexlet.code.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(imports = User.class, componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class UserDTOMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract User userDTOToUser(UserDTO userDTO);

    @AfterMapping
    void userDTOToUser(@MappingTarget User user, UserDTO userDTO) {
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

}
