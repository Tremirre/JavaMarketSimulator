package application.panels.informative;

import application.panels.ReferencingController;
import application.panels.Refreshable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public abstract class InfoPanelController extends ReferencingController implements Refreshable {
    @FXML
    protected Label nameLabel;
    @Override
    public abstract void refresh();
    public abstract void passID(String id);
}
