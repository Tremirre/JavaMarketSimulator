package application.panels.creative;

import application.panels.ReferencingController;
import application.util.DoubleFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import simulation.asset.AssetCategory;
import simulation.asset.CurrencyData;
import simulation.asset.InformedSupplementaryAssetFactory;
import simulation.util.RandomService;
import simulation.util.Resourced;
import simulation.util.records.CurrencyRecord;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;

public class CurrencyAssetCreationPanelController extends ReferencingController implements Resourced {

    @FXML
    private TextField rateField;
    @FXML
    private TextField stabilityField;
    @FXML
    private TextField countryField;
    @FXML
    private ListView<String> countryListView;

    public void initialize() {
        this.rateField.setTextFormatter(DoubleFormatter.createFormatter());
        this.stabilityField.setTextFormatter(DoubleFormatter.createFormatter());
        this.countryField.setOnKeyPressed( ke -> {
            if (ke.getCode() == KeyCode.ENTER) {
                String text = countryField.getText();
                if (text.length() > 0 && !this.countryListView.getItems().contains(text))
                    this.countryListView.getItems().add(text);
            }
        });
        this.countryListView.setOnMouseClicked( me -> {
           if (me.getButton() == MouseButton.SECONDARY) {
               int index = this.countryListView.getSelectionModel().getSelectedIndex();
               if (index >= 0)
                   this.countryListView.getItems().remove(index);
           }
        });
    }

    @Override
    public void onCreateButtonClicked() {
        var newCurrency = new InformedSupplementaryAssetFactory(
                this.simulation.getAssetManager(),
                this.nameField.getText(),
                Double.parseDouble(this.rateField.getText())
        ).createCurrencyAsset();
        var currencyData = (CurrencyData) this.simulation.getAssetManager().getAssetData(newCurrency);
        currencyData.setStability(Double.parseDouble(this.stabilityField.getText()));
        currencyData.addCountriesOfUse(new HashSet<>(this.countryListView.getItems()));
        this.mainController.newAssetAdded(newCurrency, AssetCategory.CURRENCY);
    }

    @Override
    public void onRandomizeButtonClicked() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        var decimal = new DecimalFormat("#0.00#", decimalFormatSymbols);
        this.countryListView.getItems().clear();
        var rand = RandomService.getInstance();
        var currency = (CurrencyRecord) rand.sampleElement(this.resourceHolder.getCurrencies().toArray());
        this.nameField.setText(currency.getName());
        this.rateField.setText(String.valueOf(currency.getInitialRate()));
        this.stabilityField.setText(decimal.format(rand.yieldRandomNumber(1)));
        for (var country : currency.getCountriesOfUse())
            this.countryListView.getItems().add(country);
    }
}
