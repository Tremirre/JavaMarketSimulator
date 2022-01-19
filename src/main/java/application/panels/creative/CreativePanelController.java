package application.panels.creative;

import application.panels.ReferencingController;
import application.util.CustomValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public abstract class CreativePanelController extends ReferencingController {
    @FXML
    protected TextField nameField;
    @FXML
    protected Button createButton;
    @FXML
    protected Button randomizeButton;

    protected CustomValidator validator = new CustomValidator();

    protected abstract void setupValidations();

    public void initialize() {
        this.setupValidations();
    }

    public abstract void onCreateButtonClicked();
    public abstract void onRandomizeButtonClicked();
}
