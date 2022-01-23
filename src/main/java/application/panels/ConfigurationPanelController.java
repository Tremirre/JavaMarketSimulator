package application.panels;

import application.util.format.IntegerFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import simulation.core.SimulationConfig;

public class ConfigurationPanelController extends ReferencingController {

    @FXML
    private TextField transactionCountField;
    @FXML
    private TextField offerAgeField;
    @FXML
    private Slider ratioSlider;
    @FXML
    private Slider naiveSlider;
    @FXML
    private Slider qualitativeSlider;
    @FXML
    private Slider momentumSlider;
    @FXML
    private ToggleButton restoringToggleButton;

    private boolean strategiesProportionsChanged = false;

    public void initialize() {
        var simConfig = SimulationConfig.getInstance();
        this.offerAgeField.setTextFormatter(IntegerFormatter.createFormatter());
        this.transactionCountField.setTextFormatter(IntegerFormatter.createFormatter());
        this.ratioSlider.setValue(simConfig.getBullProportion());
        this.offerAgeField.setText(String.valueOf(simConfig.getMaxOfferAge()));
        this.transactionCountField.setText(String.valueOf(simConfig.getMaxTransactionsPerDayPerMarket()));
        this.naiveSlider.setValue(simConfig.getNaiveProportion());
        this.qualitativeSlider.setValue(simConfig.getQualitativeProportion());
        this.momentumSlider.setValue(simConfig.getMomentumProportion());
        this.restoringToggleButton.setSelected(simConfig.restoringMechanismEnabled());
    }

    public void onStrategyProportionSliderChanged() {
        this.strategiesProportionsChanged = true;
    }

    public void onApplyButtonClicked() {
        var simConfig = SimulationConfig.getInstance();
        if (this.strategiesProportionsChanged) {
            this.strategiesProportionsChanged = false;
            simConfig.setProportions(
                    this.naiveSlider.getValue(),
                    this.qualitativeSlider.getValue(),
                    this.momentumSlider.getValue()
            );
            this.simulation.getEntitiesManager().setInvestorStrategies();
        }
        simConfig.setBullProportion(this.ratioSlider.getValue());
        simConfig.setMaxOfferAge(Integer.parseInt(this.offerAgeField.getText()));
        simConfig.setMaxTransactionsPerDayPerMarket(Integer.parseInt(this.transactionCountField.getText()));
        simConfig.setRestoringMechanism(this.restoringToggleButton.isSelected());
        this.mainController.refreshSimulationData();
        this.onCancelButtonClicked();
    }
}
