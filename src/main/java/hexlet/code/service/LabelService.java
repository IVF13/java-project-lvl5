package hexlet.code.service;

import hexlet.code.model.entity.Label;

import javax.management.relation.RelationException;
import java.util.List;

public interface LabelService {
    Label getLabelById(String id);

    List<Label> getAllLabels();

    Label createLabel(Label label);

    Label updateLabel(String id, Label label);

    String deleteLabel(String id) throws RelationException;

}
