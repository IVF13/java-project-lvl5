package hexlet.code.mapper;

import hexlet.code.DTO.TaskResponseDTO;
import hexlet.code.model.Task;
import org.mapstruct.Mapper;


@Mapper(imports = Task.class, componentModel = "spring")
public interface TaskResponseDTOMapper {

    TaskResponseDTO taskToTaskResponseDTO(Task task);

}
