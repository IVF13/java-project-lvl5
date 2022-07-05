package hexlet.code.app.util;

import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(imports = User.class, componentModel = "spring")
public interface UserDTOMapper {

    UserDTO userToUserDTO(User user);

}
