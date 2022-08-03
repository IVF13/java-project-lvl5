package hexlet.code.util;

import hexlet.code.model.entity.User;
import hexlet.code.model.DTO.UserDTO;
import org.mapstruct.Mapper;

@Mapper(imports = User.class, componentModel = "spring")
public interface UserDTOMapper {

    UserDTO userToUserDTO(User user);

}
