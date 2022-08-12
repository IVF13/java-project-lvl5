package hexlet.code.service.Impl;

import hexlet.code.model.Label;
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
        Label labelToUpdate = labelRepository.findById(id).orElse(null);

        if (labelToUpdate == null) {
            throw new NotFoundException("Label Not Found");
        }

        labelToUpdate.setName(updatedLabel.getName());

        return labelRepository.save(labelToUpdate);
    }

    @Override
    public String deleteLabel(Long id) throws RelationException {
        labelRepository.deleteById(id);
        return "Task status successfully deleted";
    }
}
