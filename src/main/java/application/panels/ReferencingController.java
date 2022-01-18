package application.panels;

import application.driver.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import simulation.core.Simulation;

public abstract class ReferencingController {
    protected Simulation simulation;
    protected MainController mainController;

    @FXML
    protected Button cancelButton;

    public void passSimulationReference(Simulation simulation) {
        this.simulation = simulation;
    }

    public void passMainControllerReference(MainController controller) {
        this.mainController = controller;
    }

    public void onCancelButtonClicked() {
        Stage stage = this.getStage();
        stage.close();
    }

    public Stage getStage() {
        return (Stage) this.cancelButton.getScene().getWindow();
    }

    public void refresh() {}
}
