package application.panels;

import application.util.IntegerFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import simulation.core.SimulationConfig;

public class ConfigurationPanelController extends ReferencingController {

    @FXML
    private TextField transactionCountField;
    @FXML
    private TextField offerAgeField;
    @FXML
    private Slider ratioSlider;

    public void initialize() {
        var simConfig = SimulationConfig.getInstance();
        this.offerAgeField.setTextFormatter(IntegerFormatter.createFormatter());
        this.transactionCountField.setTextFormatter(IntegerFormatter.createFormatter());
        this.ratioSlider.setValue(simConfig.getBullProportion());
        this.offerAgeField.setText(String.valueOf(simConfig.getMaxOfferAge()));
        this.transactionCountField.setText(String.valueOf(simConfig.getMaxTransactionsPerDayPerMarket()));
    }

    public void onApplyButtonClicked() {
        var simConfig = SimulationConfig.getInstance();
        simConfig.setBullProportion(this.ratioSlider.getValue());
        simConfig.setMaxOfferAge(Integer.parseInt(this.offerAgeField.getText()));
        simConfig.setMaxTransactionsPerDayPerMarket(Integer.parseInt(this.transactionCountField.getText()));
        this.getStage().close();
    }
}
