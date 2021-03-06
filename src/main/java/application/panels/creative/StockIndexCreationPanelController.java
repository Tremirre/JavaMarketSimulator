package application.panels.creative;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import simulation.asset.AssetCategory;
import simulation.asset.StockData;
import simulation.core.Simulation;
import simulation.util.RandomService;

public class StockIndexCreationPanelController extends CreativePanelController {
    @FXML
    private ComboBox<String> stockComboBox;
    @FXML
    private ListView<String> stockListView;

    @Override
    protected void setupValidations() {
        this.validator.addNotEmptyCheck("nameField", this.nameField);
        this.validator.addNotEmptyCheck("stockListView", stockListView);
    }

    public void initialize() {
        this.stockListView.setOnMouseClicked( me -> {
            if (me.getButton() == MouseButton.SECONDARY) {
                int index = this.stockListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    this.stockListView.getItems().remove(index);
                }
            }
        });
        super.initialize();
    }

    public void onStockComboBoxChanged() {
        String asset = this.stockComboBox.getValue();
        if (!this.stockListView.getItems().contains(asset)) {
            this.stockListView.getItems().add(asset);
        }
    }

    @Override
    public void onCreateButtonClicked() {
        if (!this.validator.validate()) {
            return;
        }
        String name = this.nameField.getText();
        if (!this.simulation.getEntitiesManager().isIndexNameFree(name))
            return;
        var idx = this.simulation.getEntitiesManager().createNewStockIndex(name);
        for (var stockAsset : this.stockListView.getItems()) {
            idx.addCompany(((StockData) this.simulation.getAssetManager().getAssetData(stockAsset)).getCompany());
        }
        this.mainController.newIndexAdded(name);
    }

    @Override
    public void onRandomizeButtonClicked() {
        this.stockListView.getItems().clear();
        var rand = RandomService.getInstance();
        this.nameField.setText(
                rand.yieldRandomString(4).toUpperCase() + (rand.yieldRandomInteger(100) + 10)
        );
        int numberOfAssets = rand.yieldRandomInteger(7) + 3;
        for (int i = 0; i < numberOfAssets; i++) {
            String asset = (String) rand.sampleElement(this.stockComboBox.getItems().toArray());
            if (!this.stockListView.getItems().contains(asset) && asset != null) {
                this.stockListView.getItems().add(asset);
            }
        }
    }

    @Override
    public void passSimulationReference(Simulation simulation) {
        super.passSimulationReference(simulation);
        this.stockComboBox.getItems().addAll(this.simulation.getAssetManager().getAssetsByCategory(AssetCategory.STOCK));
    }
}
