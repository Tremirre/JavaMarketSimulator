package application.panels;

import application.driver.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simulation.core.Simulation;

import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

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
        var stage = this.getStage();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public Stage getStage() {
        return (Stage) this.cancelButton.getScene().getWindow();
    }

    public void refresh() {}
}
