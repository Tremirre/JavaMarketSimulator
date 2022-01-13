package application.panels;

import application.util.DoubleFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import simulation.asset.AssetCategory;
import simulation.asset.CommodityData;
import simulation.asset.InformedSupplementaryAssetFactory;
import simulation.core.Simulation;
import simulation.util.Constants;
import simulation.util.RandomService;
import simulation.util.Resourced;
import simulation.util.records.CommodityRecord;

public class CommodityAssetCreationPanelController extends CreationPanelController implements Resourced {

    @FXML
    private TextField rateField;
    @FXML
    private ComboBox<String> tradingUnitComboBox;
    @FXML
    private ComboBox<String> tradingCurrencyComboBox;

    public void initialize() {
        this.rateField.setTextFormatter(DoubleFormatter.createFormatter());
        this.tradingUnitComboBox.getItems().addAll(
                "gram",
                "kilogram",
                "metric ton",
                "ounce",
                "bushel",
                "liter",
                "pound",
                "gallon",
                "barrel",
                "hundredweight"
        );
    }

    @Override
    public void onCreateButtonClicked() {
        var newCommodity = new InformedSupplementaryAssetFactory(
                this.simulation.getAssetManager(),
                this.nameField.getText(),
                Double.parseDouble(this.rateField.getText())
        ).createCommodityAsset();
        var assetData = (CommodityData) this.simulation.getAssetManager().getAssetData(newCommodity);
        assetData.setTradingCurrency(this.tradingCurrencyComboBox.getValue());
        assetData.setTradingUnit(this.tradingUnitComboBox.getValue());
        this.mainController.newAssetAdded(newCommodity, AssetCategory.COMMODITY);
    }

    @Override
    public void onRandomizeButtonClicked() {
        var rand = RandomService.getInstance();
        var commodity = (CommodityRecord) rand.sampleElement(this.resourceHolder.getCommodities().toArray());
        this.rateField.setText(String.valueOf(commodity.getInitialRate()));
        this.nameField.setText(commodity.getName());
        this.tradingUnitComboBox.setValue(commodity.getUnit());
        this.tradingCurrencyComboBox.setValue(
                (String) rand.sampleElement(this.tradingCurrencyComboBox.getItems().toArray(new String[0]))
        );
    }

    @Override
    public void passSimulationReference(Simulation simulation) {
        super.passSimulationReference(simulation);
        this.tradingCurrencyComboBox.getItems().addAll(
                this.simulation.getAssetManager().getAssetsByCategory(AssetCategory.CURRENCY)
        );
        this.tradingCurrencyComboBox.getItems().add(Constants.DEFAULT_CURRENCY);
    }
}
