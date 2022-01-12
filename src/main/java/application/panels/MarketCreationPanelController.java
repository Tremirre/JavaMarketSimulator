package application.panels;

import application.util.DoubleFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import simulation.core.Simulation;
import simulation.market.InformedMarketFactory;
import simulation.market.MarketType;

public class MarketCreationPanelController {
    @FXML
    private TextField buyFeeField;
    @FXML
    private TextField sellFeeField;
    @FXML
    private TextField nameField;

    @FXML
    private ToggleGroup marketType;

    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;
    @FXML
    private Button randomizeButton;


    private Simulation simulation;

    public void initialize() {
        this.buyFeeField.setTextFormatter(DoubleFormatter.createFormatter());
        this.sellFeeField.setTextFormatter(DoubleFormatter.createFormatter());
        //this.createButton.setDisable(true);
        /*Validator validator = new Validator();
        validator.createCheck()
                .dependsOn("buyField", buyFeeField.textProperty())
                .withMethod(c -> {
                    double value = Double.parseDouble(c.get("buyField"));
                    if (value > 1 || value < 0)
                        c.warn("Fee can only be between 0 and 1!");
                }).decorates(buyFeeField).immediate();

        validator.createCheck()
                .dependsOn("sellField", buyFeeField.textProperty())
                .withMethod(c -> {
                    double value = Double.parseDouble(c.get("sellField"));
                    if (value > 1 || value < 0)
                        c.warn("Fee can only be between 0 and 1!");
                }).decorates(sellFeeField).immediate();

         */
    }

    public void onCreateButtonClicked() {
        MarketType type;
        System.out.println("Create Button Clicked!");
        switch(((RadioButton) this.marketType.getSelectedToggle()).getText()) {
            case "Stock" -> type = MarketType.STOCK_MARKET;
            case "Currency" -> type = MarketType.CURRENCIES_MARKET;
            case "Commodity" -> type = MarketType.COMMODITIES_MARKET;
            default -> throw new IllegalStateException();
        }
        var newMarket = new InformedMarketFactory(simulation.getAssetManager(),
                nameField.getText(),
                null,
                Double.parseDouble(buyFeeField.getText()),
                Double.parseDouble(sellFeeField.getText()))
                .createMarket(type);
        simulation.addNewMarket(newMarket, false); //SIMULATION IS NULL!
    }

    public void onRandomizeButtonClicked() {
        System.out.println("Randomize Button Clicked!");
    }

    public void onCancelButtonClicked() {
        System.out.println("Cancel Button Clicked!");
    }

    public void passSimulationReference(Simulation simulation) {
        this.simulation = simulation;
    }
}
