package hexlet.code.util;

import hexlet.code.model.DTO.TaskResponseDTO;
import hexlet.code.model.entity.Task;
import org.mapstruct.Mapper;


@Mapper(imports = Task.class, componentModel = "spring")
public interface TaskResponseDTOMapper {

    TaskResponseDTO taskToTaskResponseDTO(Task task);

}
