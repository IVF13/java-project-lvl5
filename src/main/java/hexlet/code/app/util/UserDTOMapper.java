package hexlet.code.app.util;

import hexlet.code.app.model.entity.User;
import hexlet.code.app.model.DTO.UserDTO;
import org.mapstruct.Mapper;

@Mapper(imports = User.class, componentModel = "spring")
public interface UserDTOMapper {

    UserDTO userToUserDTO(User user);

}
