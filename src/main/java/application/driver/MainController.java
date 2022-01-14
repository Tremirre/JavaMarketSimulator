package application.driver;

import application.panels.CreationPanelController;
import application.panels.MarketCreationPanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import simulation.asset.AssetCategory;
import simulation.core.Simulation;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class MainController {
    private Simulation simulation;

    @FXML
    private ListView<String> stockListView;
    @FXML
    private ListView<String> commodityListView;
    @FXML
    private ListView<String> currencyListView;
    @FXML
    private ListView<String> marketListView;
    @FXML
    private ListView<String> investorListView;
    @FXML
    private ListView<String> smiListView;

    private HashMap<AssetCategory, ListView<String>> categoryMapping;

    public void initialize() {
        this.simulation = new Simulation();
        this.categoryMapping = new HashMap<>();
        this.categoryMapping.put(AssetCategory.CURRENCY, this.currencyListView);
        this.categoryMapping.put(AssetCategory.COMMODITY, this.commodityListView);
        this.categoryMapping.put(AssetCategory.STOCK, this.stockListView);
    }

    private void openNewWindow(URL source, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(source);
            Scene mainScene = new Scene(fxmlLoader.load());
            CreationPanelController controller = fxmlLoader.getController();
            controller.passSimulationReference(simulation);
            controller.passMainControllerReference(this);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(mainScene);
            stage.getIcons().add(new Image(Application.class.getResource("icon.ico").toString()));
            stage.show();

        } catch(IOException e) {
            System.out.println("Failed to open window " + title);
            System.out.println(e.getMessage());
        }
    }

    private void refreshMarketView() {
        this.marketListView.getItems().clear();
        for (var market : this.simulation.getMarkets())
            this.marketListView.getItems().add(market.getName());
    }

    private void refreshSMIView() {
        this.smiListView.getItems().clear();
        for (var idx : this.simulation.getEntitiesManager().getStockMarketIndexes())
            this.smiListView.getItems().add(idx.getName());
    }

    private void refreshAssetView(AssetCategory category) {
        ListView<String> listView = this.categoryMapping.get(category);
        listView.getItems().clear();
        listView.getItems().addAll(this.simulation.getAssetManager().getAssetsByCategory(category));
    }

    private void refreshListViews() {
        for (var category : AssetCategory.class.getEnumConstants())
            this.refreshAssetView(category);
        this.refreshMarketView();
        this.refreshInvestorView();
        this.refreshSMIView();
    }

    private void refreshInvestorView() {
        this.investorListView.getItems().clear();
        for (var investor : this.simulation.getEntitiesManager().getInvestors()) {
            this.investorListView.getItems().add(
                    investor.getFirstName() + ' ' + investor.getLastName() + " (" + investor.getID() + ')'
            );
        }
    }

    public void onRandomizeSetupMenuSelected() {
        for (var category : AssetCategory.class.getEnumConstants())
            this.simulation.setupRandomMarket(category,false);
        this.refreshListViews();
    }

    public void onClearSetupButtonClicked() {
        this.simulation = new Simulation();
        this.refreshListViews();
    }

    public void onAddMarketButtonClicked() {
        var source = MarketCreationPanelController.class.getResource("market_creation_panel.fxml");
        this.openNewWindow(source, "Market Creation Panel");
    }

    public void onAddCommodityButtonClicked() {
        var source = MarketCreationPanelController.class.getResource("commodity_asset_creation_panel.fxml");
        this.openNewWindow(source, "Commodity Creation Panel");
    }

    public void onAddCurrencyButtonClicked() {
        var source = MarketCreationPanelController.class.getResource("currency_asset_creation_panel.fxml");
        this.openNewWindow(source, "Commodity Creation Panel");
    }

    public void onAddStockButtonClicked() {
        var source = MarketCreationPanelController.class.getResource("company_creation_panel.fxml");
        this.openNewWindow(source, "Company Creation Panel");
    }

    public void onAddSMIButtonClicked() {

    }

    public void newMarketAdded(String marketName) {
        this.marketListView.getItems().add(marketName);
    }

    public void newAssetAdded(String assetName, AssetCategory category) {
        this.categoryMapping.get(category).getItems().add(assetName);
        this.refreshInvestorView();
    }
}
