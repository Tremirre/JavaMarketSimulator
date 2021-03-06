package application.panels.creative;

import application.util.format.DecimalDisplayFormat;
import application.util.format.DoubleFormatter;
import application.util.format.IntegerFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import simulation.address.Address;
import simulation.address.RandomAddressFactory;
import simulation.asset.AssetCategory;
import simulation.core.Simulation;
import simulation.market.InformedMarketFactory;
import simulation.market.StockMarket;
import simulation.util.Constants;
import simulation.util.RandomService;

public class MarketCreationPanelController extends CreativePanelController {
    @FXML
    private TextField buyFeeField;
    @FXML
    private TextField sellFeeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField buildingNumberField;
    @FXML
    private ToggleGroup marketType;
    @FXML
    private ListView<String> assetListView;
    @FXML
    private ComboBox<String> assetComboBox;
    @FXML
    private ComboBox<String> currencyComboBox;
    @FXML
    private Label availableAssetLabel;

    private AssetCategory readMarketTypeFromComboBox() {
        AssetCategory type;
        switch(((RadioButton) this.marketType.getSelectedToggle()).getText()) {
            case "Stock" -> type = AssetCategory.STOCK;
            case "Currency" -> type = AssetCategory.CURRENCY;
            case "Commodity" -> type = AssetCategory.COMMODITY;
            default -> throw new IllegalStateException();
        }
        return type;
    }

    protected void setupValidations() {
        this.validator.addPercentageCheck("buyField", this.buyFeeField);
        this.validator.addPercentageCheck("sellField", this.sellFeeField);
        this.validator.addNotEmptyCheck("countryField", this.countryField);
        this.validator.addNotEmptyCheck("cityField", this.cityField);
        this.validator.addNotEmptyCheck("postalCodeField", this.postalCodeField);
        this.validator.addNotEmptyCheck("streetField", this.streetField);
        this.validator.addNotEmptyCheck("buildingNumberField", this.buildingNumberField);
        this.validator.addNotEmptyCheck("assetListView", this.assetListView);
    }

    public void initialize() {
        this.buyFeeField.setTextFormatter(DoubleFormatter.createFormatter());
        this.sellFeeField.setTextFormatter(DoubleFormatter.createFormatter());
        this.buildingNumberField.setTextFormatter(IntegerFormatter.createFormatter());
        this.assetListView.setOnMouseClicked( me -> {
            if (me.getButton() == MouseButton.SECONDARY) {
                int index = this.assetListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    this.assetListView.getItems().remove(index);
                }
            }
        });
        super.initialize();
    }

    public void onAssetComboBoxChanged() {
        String asset = this.assetComboBox.getValue();
        if (!this.assetListView.getItems().contains(asset)) {
            this.assetListView.getItems().add(asset);
        }
    }

    public void onCreateButtonClicked() {
        if (!this.validator.validate()) {
            return;
        }
        var type = this.readMarketTypeFromComboBox();
        var address = new Address(this.countryField.getText(),
                this.cityField.getText(),
                this.postalCodeField.getText(),
                this.streetField.getText(),
                Integer.parseInt(this.buildingNumberField.getText()));
        var newMarket = new InformedMarketFactory(
                this.simulation.getAssetManager(),
                this.nameField.getText(),
                address,
                Double.parseDouble(this.buyFeeField.getText()),
                Double.parseDouble(this.sellFeeField.getText()))
                .createMarket(this.readMarketTypeFromComboBox());
        if (this.simulation.doesMarketExist(newMarket.getName()))
            return;
        if (type != AssetCategory.STOCK) {
            for (var assetType : this.assetListView.getItems())
                newMarket.addNewAsset(assetType);
        } else {
            ((StockMarket) newMarket).setMarketCurrency(this.currencyComboBox.getValue());
            for (var idxName : this.assetListView.getItems()) {
                var idx = this.simulation.getEntitiesManager().getIndexByName(idxName);
                ((StockMarket) newMarket).addStockMarketIndex(idx);
            }
        }
        this.simulation.addNewMarket(newMarket);
        this.mainController.newMarketAdded(newMarket.getName());
    }

    public void onRandomizeButtonClicked() {
        var decimal = new DecimalDisplayFormat(3);
        var sellFee = RandomService.getInstance().yieldRandomGaussianNumber(0.01, 0.05);
        this.sellFeeField.setText(decimal.format(sellFee));
        var buyFee = RandomService.getInstance().yieldRandomGaussianNumber(0.01, 0.05);
        this.buyFeeField.setText(decimal.format(buyFee));
        var address = new RandomAddressFactory().createAddress();
        this.countryField.setText(address.getCountry());
        this.cityField.setText(address.getCity());
        this.postalCodeField.setText(address.getPostalCode());
        this.streetField.setText(address.getStreetName());
        this.buildingNumberField.setText(String.valueOf(address.getBuildingNumber()));
    }

    public void onRadioButtonChange() {
        var type = this.readMarketTypeFromComboBox();
        this.assetComboBox.getItems().clear();
        if (type != AssetCategory.STOCK) {
            this.assetComboBox.setPromptText("Choose Asset");
            this.assetComboBox.getItems().addAll(simulation.getAssetManager().getAssetsByCategory(type));
            this.currencyComboBox.setDisable(true);
            this.availableAssetLabel.setText(
                    "Assets available on the market:"
            );
        }
        else {
            this.assetComboBox.setPromptText("Choose Index");
            this.addStockMarketIndexesAsComboBoxOptions();
            this.currencyComboBox.setDisable(false);
            this.availableAssetLabel.setText(
                    "Indexes available on the market:"
            );
        }
        this.assetListView.getItems().clear();
    }

    private void addStockMarketIndexesAsComboBoxOptions() {
        for (var idx : this.simulation.getEntitiesManager().getStockMarketIndexes())
            this.assetComboBox.getItems().add(idx.getName());
    }

    public void passSimulationReference(Simulation simulation) {
        super.passSimulationReference(simulation);
        this.addStockMarketIndexesAsComboBoxOptions();
        this.currencyComboBox.getItems().addAll(simulation.getAssetManager().getAssetsByCategory(AssetCategory.CURRENCY));
        this.currencyComboBox.getItems().add(Constants.DEFAULT_CURRENCY);
    }
}
