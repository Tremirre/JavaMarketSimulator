package application.panels;

import application.driver.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import simulation.core.Simulation;

public abstract class CreationPanelController {
    protected Simulation simulation;
    protected MainController mainController;

    @FXML
    protected TextField nameField;
    @FXML
    protected Button cancelButton;
    @FXML
    protected Button createButton;
    @FXML
    protected Button randomizeButton;

    public void passSimulationReference(Simulation simulation) {
        this.simulation = simulation;
    }

    public void passMainControllerReference(MainController controller) {
        this.mainController = controller;
    }

    public abstract void onCreateButtonClicked();

    public abstract void onRandomizeButtonClicked();

    public void onCancelButtonClicked() {
        Stage stage = (Stage) this.cancelButton.getScene().getWindow();
        stage.close();
    }
}
