package hexlet.code.mapper;

import hexlet.code.model.User;
import hexlet.code.DTO.UserDTO;
import org.mapstruct.Mapper;

@Mapper(imports = User.class, componentModel = "spring")
public interface UserDTOMapper {

    UserDTO userToUserDTO(User user);

}
