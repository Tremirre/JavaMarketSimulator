package application.panels.creative;

import application.util.format.DecimalDisplayFormat;
import application.util.format.DoubleFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import simulation.holders.InformedHolderFactory;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.holders.strategies.MomentumInvestmentStrategy;
import simulation.holders.strategies.NaiveInvestmentStrategy;
import simulation.holders.strategies.QualitativeAssessmentStrategy;
import simulation.util.Constants;
import simulation.util.RandomService;
import simulation.util.Resourced;

public class InvestorCreationPanelController extends CreativePanelController implements Resourced {
    @FXML
    private TextField secondNameField;
    @FXML
    private TextField fundsField;
    @FXML
    private ComboBox<String> strategyComboBox;

    public void initialize() {
        this.strategyComboBox.getItems().addAll(
                "Naive Investment Strategy",
                "Qualitative Assessment Strategy",
                "Momentum Investment Strategy"
        );
        this.strategyComboBox.setValue("Naive Investment Strategy");
        this.fundsField.setTextFormatter(DoubleFormatter.createFormatter());
        super.initialize();
    }

    @Override
    protected void setupValidations() {
        this.validator.addPositiveCheck("fundsField", fundsField);
        this.validator.addNotEmptyCheck("nameField", this.nameField);
        this.validator.addNotEmptyCheck("secondNameField", this.secondNameField);
    }

    @Override
    public void onCreateButtonClicked() {
        if (!this.validator.validate()) {
            return;
        }
        InvestmentStrategy strategy;
        var rand = RandomService.getInstance();
        switch(this.strategyComboBox.getValue().charAt(0)) {
            case 'N' -> strategy = new NaiveInvestmentStrategy(
                    this.simulation.getAssetManager(),
                    rand.yieldRandomGaussianNumber(0.03, 0.9)
            );
            case 'Q' -> strategy = new QualitativeAssessmentStrategy(
                    this.simulation.getAssetManager(),
                    rand.yieldRandomGaussianNumber(0.05, 1)
            );
            default -> strategy = new MomentumInvestmentStrategy(
                    this.simulation.getAssetManager(),
                    rand.yieldRandomInteger(5) + 1
            );
        }
        var factory = new InformedHolderFactory(
                this.simulation.getEntitiesManager().getTotalNumberOfEntities(),
                this.simulation.getAssetManager()
        );
        factory.setInvestorData(
                Double.parseDouble(this.fundsField.getText()),
                this.nameField.getText(),
                this.secondNameField.getText(),
                strategy
                );
        this.simulation.getEntitiesManager().createNewInvestor(factory);
        this.mainController.refreshInvestorView();
    }

    @Override
    public void onRandomizeButtonClicked() {
        var rand = RandomService.getInstance();
        this.nameField.setText((String) rand.sampleElement(resourceHolder.getNames()));
        this.secondNameField.setText((String) rand.sampleElement(resourceHolder.getSurnames()));
        double funds = rand.yieldRandomGaussianNumber(Constants.INVESTOR_INITIAL_FUNDS_DEVIATION,
                Constants.INVESTOR_MEAN_INITIAL_FUNDS);
        var decimal = new DecimalDisplayFormat(4);
        this.fundsField.setText(decimal.format(funds));
        this.strategyComboBox.setValue(
                (String) rand.sampleElement(this.strategyComboBox.getItems().toArray(new String[0]))
        );
    }
}
