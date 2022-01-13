package application.panels;

import application.util.DoubleFormatter;
import application.util.IntegerFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import simulation.address.Address;
import simulation.address.RandomAddressFactory;
import simulation.asset.AssetCategory;
import simulation.core.Simulation;
import simulation.market.InformedMarketFactory;
import simulation.util.Constants;
import simulation.util.RandomService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MarketCreationPanelController extends CreationPanelController {
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

    public void initialize() {
        this.buyFeeField.setTextFormatter(DoubleFormatter.createFormatter());
        this.sellFeeField.setTextFormatter(DoubleFormatter.createFormatter());
        this.buildingNumberField.setTextFormatter(IntegerFormatter.createFormatter());
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
        for (var assetType : this.assetListView.getItems())
            newMarket.addNewAsset(assetType);
        this.simulation.addNewMarket(newMarket, false);
        this.mainController.newMarketAdded(newMarket.getName());
    }

    public void onRandomizeButtonClicked() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        var decimal = new DecimalFormat("#0.00#", decimalFormatSymbols);
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
        this.assetComboBox.getItems().addAll(simulation.getAssetManager().getAssetsByCategory(type));
        this.assetListView.getItems().clear();
        this.currencyComboBox.setDisable(type != AssetCategory.STOCK);
    }

    public void passSimulationReference(Simulation simulation) {
        super.passSimulationReference(simulation);
        this.assetComboBox.getItems().addAll(simulation.getAssetManager().getAssetsByCategory(this.readMarketTypeFromComboBox()));
        this.currencyComboBox.getItems().addAll(simulation.getAssetManager().getAssetsByCategory(AssetCategory.CURRENCY));
        this.currencyComboBox.getItems().add(Constants.DEFAULT_CURRENCY);
    }
}
