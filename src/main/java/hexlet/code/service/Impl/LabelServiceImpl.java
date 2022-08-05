package hexlet.code.service.Impl;

import hexlet.code.model.entity.Label;
import hexlet.code.model.entity.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.management.relation.RelationException;
import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label getLabelById(Long id) {
        Label label = labelRepository.findById(id).orElse(null);

        if (label == null) {
            throw new NotFoundException("Label Not Found");
        }

        return label;
    }

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label createLabel(Label label) {
        if (labelRepository.findByName(label.getName()).isPresent()) {
            throw new EntityExistsException("Label already exists");
        }

        labelRepository.save(label);
        label = labelRepository.findByName(label.getName()).get();

        return label;
    }

    @Override
    public Label updateLabel(Long id, Label updatedLabel) {
        Label existsLabel = labelRepository.findById(id).orElse(null);

        if (existsLabel == null) {
            throw new NotFoundException("Label Not Found");
        }
        updatedLabel.setId(existsLabel.getId());
        updatedLabel.setCreatedAt(existsLabel.getCreatedAt());

        labelRepository.save(updatedLabel);
        updatedLabel = labelRepository.findById(id).get();

        return updatedLabel;
    }

    @Override
    public String deleteLabel(Long id) throws RelationException {

        if (!labelRepository.existsById(id)) {
            throw new NotFoundException("Label Not Found");
        } else {

            List<Task> tasks = labelRepository.findById(id).get().getTasks();

            if (!tasks.isEmpty()) {
                throw new RelationException("Label have related tasks, unable to delete");
            }

            labelRepository.deleteById(id);
        }

        return "Task status successfully deleted";
    }
}
