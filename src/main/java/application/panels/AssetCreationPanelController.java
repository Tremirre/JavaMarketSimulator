package application.panels;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AssetCreationPanelController extends CreationPanelController {

    @FXML
    private RadioButton currencyRadioButton;
    @FXML
    private RadioButton commodityRadioButton;
    @FXML
    private TextField rateField;
    @FXML
    private ComboBox<String> tradingUnitComboBox;
    @FXML
    private ComboBox<String> tradingCurrencyComboBox;
    @FXML
    private TextField stabilityField;
    @FXML
    private TextField countryField;
    @FXML
    private ListView<String> countryListView;

    @Override
    public void onCreateButtonClicked() {

    }

    @Override
    public void onRandomizeButtonClicked() {

    }
}
