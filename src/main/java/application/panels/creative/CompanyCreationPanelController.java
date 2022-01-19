package application.panels.creative;

import application.util.format.DecimalDisplayFormat;
import application.util.format.DoubleFormatter;
import application.util.format.IntegerFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import simulation.address.Address;
import simulation.address.RandomAddressFactory;
import simulation.asset.AssetCategory;
import simulation.holders.InformedHolderFactory;
import simulation.util.Constants;
import simulation.util.RandomService;
import simulation.util.Resourced;

import java.time.LocalDate;

public class CompanyCreationPanelController extends CreativePanelController implements Resourced {
    @FXML
    private DatePicker datePicker;
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
    private TextField profitField;
    @FXML
    private TextField revenueField;
    @FXML
    private TextField shareValueField;
    @FXML
    private TextField shareCountField;

    @Override
    protected void setupValidations() {
        this.validator.addNotEmptyCheck("nameField", this.nameField);
        this.validator.addNotEmptyCheck("datePicker", this.datePicker);
        this.validator.addNotEmptyCheck("countryField", this.countryField);
        this.validator.addNotEmptyCheck("cityField", this.cityField);
        this.validator.addNotEmptyCheck("postalCodeField", this.postalCodeField);
        this.validator.addNotEmptyCheck("streetField", this.streetField);
        this.validator.addNotEmptyCheck("buildingNumberField", this.buildingNumberField);
        this.validator.addNotEmptyCheck("profitField", this.profitField);
        this.validator.addNotEmptyCheck("revenueField", this.revenueField);
        this.validator.addPositiveCheck("revenueFieldPositive", this.revenueField);
        this.validator.addNotEmptyCheck("shareValueField", this.shareValueField);
        this.validator.addPositiveCheck("shareValueFieldPositive", this.shareValueField);
        this.validator.addNotEmptyCheck("shareCountField", this.shareCountField);
        this.validator.addPositiveCheck("shareCountFieldPositive", this.shareCountField);
    }

    public void initialize() {
        this.buildingNumberField.setTextFormatter(IntegerFormatter.createFormatter());
        this.profitField.setTextFormatter(DoubleFormatter.createFormatter());
        this.revenueField.setTextFormatter(DoubleFormatter.createFormatter());
        this.shareValueField.setTextFormatter(DoubleFormatter.createFormatter());
        this.shareCountField.setTextFormatter(IntegerFormatter.createFormatter());
        super.initialize();
    }

    @Override
    public void onCreateButtonClicked() {
        if (!this.validator.validate()) {
            return;
        }
        var factory = new InformedHolderFactory(
                this.simulation.getEntitiesManager().getTotalNumberOfEntities(),
                this.simulation.getAssetManager()
        );
        var address = new Address(
                this.countryField.getText(),
                this.cityField.getText(),
                this.postalCodeField.getText(),
                this.streetField.getText(),
                Integer.parseInt(this.buildingNumberField.getText())
        );
        factory.setCompanyData(
                Integer.parseInt(this.shareCountField.getText()),
                this.nameField.getText(),
                this.datePicker.getValue().toString(),
                address,
                Double.parseDouble(this.shareValueField.getText()),
                Double.parseDouble(this.profitField.getText()),
                Double.parseDouble(this.revenueField.getText())
        );
        var company = this.simulation.getEntitiesManager().createNewCompany(factory);
        this.mainController.newAssetAdded(company.getAssociatedAsset(), AssetCategory.STOCK);
    }

    @Override
    public void onRandomizeButtonClicked() {
        var decimal = new DecimalDisplayFormat(3);
        var rand = RandomService.getInstance();
        this.nameField.setText((String) rand.sampleElement(this.resourceHolder.getCompanyNames().toArray()));
        this.datePicker.setValue(LocalDate.parse(rand.yieldDate()));
        var randAddress = new RandomAddressFactory().createAddress();
        this.countryField.setText(randAddress.getCountry());
        this.cityField.setText(randAddress.getCity());
        this.postalCodeField.setText(randAddress.getPostalCode());
        this.streetField.setText(randAddress.getStreetName());
        this.buildingNumberField.setText(String.valueOf(randAddress.getBuildingNumber()));
        var initialStockValue = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE)
                + Constants.COMPANY_MINIMAL_INITIAL_STOCK_VALUE;
        var initialStockSize = (Constants.COMPANY_GENERATION_CONSTANT / initialStockValue)
                + rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER);
        var profit = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_PROFIT) - Constants.COMPANY_MINIMAL_PROFIT;
        var revenue = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_REVENUE) + Constants.COMPANY_MINIMAL_REVENUE;
        this.profitField.setText(decimal.format(profit));
        this.revenueField.setText(decimal.format(revenue));
        this.shareValueField.setText(decimal.format(initialStockValue));
        this.shareCountField.setText(String.valueOf(initialStockSize));
    }
}
