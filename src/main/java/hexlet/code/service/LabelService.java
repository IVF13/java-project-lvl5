package hexlet.code.service;

import hexlet.code.model.Label;

import javax.management.relation.RelationException;
import java.util.List;

public interface LabelService {
    Label getLabelById(Long id);

    List<Label> getAllLabels();

    Label createLabel(Label label);

    Label updateLabel(Long id, Label label);

    String deleteLabel(Long id) throws RelationException;

}
