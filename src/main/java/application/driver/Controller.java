package application.driver;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import simulation.core.Simulation;
import simulation.util.DataExporter;

public class Controller {
    private Simulation simulation;

    @FXML
    private Label dayLabel;
    @FXML
    private Button startButton;
    @FXML
    private Button dayButton;
    @FXML
    private Button stopButton;

    @FXML
    public void initialize() {
        this.dayButton.setDisable(true);
        this.stopButton.setDisable(true);
    }

    public void onStartButtonClick() {
        this.simulation = new Simulation();
        this.simulation.start();
        this.startButton.setDisable(true);
        this.dayButton.setDisable(false);
        this.stopButton.setDisable(false);
    }

    public void onDayButtonClick() {
        this.simulation.runSimulationDay();
        var text = this.dayLabel.getText();
        var number = Integer.parseInt(text.split(" ")[1]);
        this.dayLabel.setText("Day: " + (++number));
    }

    public void onStopButtonClick() {
        this.simulation.stop();
        DataExporter dataEx = new DataExporter();
        dataEx.exportLabeledData(this.simulation.getAssetManager().getAllAssetsPriceHistory(), "GUI_export_test.csv");
        this.dayButton.setDisable(true);
        this.stopButton.setDisable(true);
        this.startButton.setDisable(false);
    }
}
