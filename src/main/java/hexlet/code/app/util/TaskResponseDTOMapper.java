package hexlet.code.app.util;

import hexlet.code.app.model.DTO.TaskResponseDTO;
import hexlet.code.app.model.entity.Task;
import org.mapstruct.Mapper;


@Mapper(imports = Task.class, componentModel = "spring")
public interface TaskResponseDTOMapper {

    TaskResponseDTO taskToTaskRequestDTO(Task task);

}
