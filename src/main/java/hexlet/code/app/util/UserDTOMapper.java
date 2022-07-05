package hexlet.code.app.util;

import hexlet.code.app.model.User;
import hexlet.code.app.model.UserDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(imports = User.class, componentModel = "spring")
public interface UserDTOMapper {

    UserDTO userToUserDTO(User user);

}
