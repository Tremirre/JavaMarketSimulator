package application.driver;

import application.panels.ConfigurationPanelController;
import application.panels.creative.CreativePanelController;
import application.panels.informative.AssetInfoPanelController;
import application.panels.plot.MultiAssetPanelController;
import application.util.format.DecimalDisplayFormat;
import application.util.format.SimulationRunner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.WindowEvent;
import simulation.asset.AssetCategory;
import simulation.core.Simulation;
import simulation.core.SimulationConfig;
import simulation.holders.strategies.NaiveInvestmentStrategy;
import simulation.holders.strategies.QualitativeAssessmentStrategy;
import simulation.util.Constants;
import simulation.util.DataExporter;

import java.util.*;

public class MainController {
    private static Simulation simulation;
    private static SimulationRunner simRunner;
    private final WindowsManager windowsManager = new WindowsManager(this);
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
    @FXML
    private Button startButton;
    @FXML
    private ToggleButton pauseButton;
    @FXML
    private Label simulationDayLabel;
    @FXML
    private Label entitiesCountLabel;
    @FXML
    private Label totalFundsLabel;
    @FXML
    private Label assetPriceLabel;
    @FXML
    private Label naiveCountLabel;
    @FXML
    private Label qualitativeCountLabel;
    @FXML
    private Label momentumCountLabel;
    @FXML
    private Label processedLabel;
    @FXML
    private Label updatedLabel;
    @FXML
    private Label removedLabel;
    @FXML
    private Button marketAddButton;
    @FXML
    private Button currencyAddButton;
    @FXML
    private Button commodityAddButton;
    @FXML
    private Button stockAddButton;
    @FXML
    private Button smiAddButton;
    @FXML
    private Button investorAddButton;
    @FXML
    private Button generateMarketsButton;
    @FXML
    private Button simulationConfigButton;
    @FXML
    private Button resetButton;
    @FXML
    private MenuItem priceExportMenuItem;
    @FXML
    private Slider timeMultiplierSlider;

    private HashMap<AssetCategory, ListView<String>> categoryMapping;

    private void setListViewEvents() {
        ArrayList<ListView<String>> listViews = new ArrayList<>();
        ArrayList<Map.Entry<String, String>> windowsData = new ArrayList<>();
        Collections.addAll(
                listViews,
                this.marketListView,
                this.commodityListView,
                this.currencyListView,
                this.stockListView,
                this.smiListView,
                this.investorListView
        );
        Collections.addAll(
                windowsData,
                new AbstractMap.SimpleEntry<>("market_info_panel.fxml", "Market Info Panel"),
                new AbstractMap.SimpleEntry<>("commodity_asset_info_panel.fxml", "Commodity Info Panel"),
                new AbstractMap.SimpleEntry<>("currency_asset_info_panel.fxml", "Currency Info Panel"),
                new AbstractMap.SimpleEntry<>("company_info_panel.fxml", "Stock (Company) Info Panel"),
                new AbstractMap.SimpleEntry<>("stock_index_info_panel.fxml", "Stock Index Info Panel"),
                new AbstractMap.SimpleEntry<>("investor_info_panel.fxml", "Investor Info Panel")
        );
        for (int i = 0; i < listViews.size(); i++) {
            var currentListView = listViews.get(i);
            var fileName = windowsData.get(i).getKey();
            var title = windowsData.get(i).getValue();
            currentListView.setOnMouseClicked( me -> {
                        if (me.getButton() == MouseButton.PRIMARY) {
                            int index = currentListView.getSelectionModel().getSelectedIndex();
                            if (index >= 0) {
                                var id = currentListView.getItems().get(index);
                                this.onListViewClicked(fileName, title, id);
                            }
                        }
                    }
            );
            currentListView.setOnKeyPressed(ke  -> {
                        if (ke.getCode() == KeyCode.ENTER) {
                            int index = currentListView.getSelectionModel().getSelectedIndex();
                            if (index >= 0) {
                                var id = currentListView.getItems().get(index);
                                this.onListViewClicked(fileName, title, id);
                            }
                        }
                    }
            );
        }
    }

    public void initialize() {
        simulation = new Simulation();
        simRunner = new SimulationRunner(this, simulation);
        this.categoryMapping = new HashMap<>();
        this.categoryMapping.put(AssetCategory.CURRENCY, this.currencyListView);
        this.categoryMapping.put(AssetCategory.COMMODITY, this.commodityListView);
        this.categoryMapping.put(AssetCategory.STOCK, this.stockListView);
        this.pauseButton.setDisable(true);

        this.setListViewEvents();
        this.timeMultiplierSlider.valueProperty().addListener((observable, oldValue, newValue) ->
            SimulationConfig.getInstance().setTimeMultiplier(1/(Double) newValue));
        this.refreshListViews();
    }
    private void disableSimulationModifyingElements(boolean disable) {
        this.marketAddButton.setDisable(disable);
        this.currencyAddButton.setDisable(disable);
        this.commodityAddButton.setDisable(disable);
        this.smiAddButton.setDisable(disable);
        this.simulationConfigButton.setDisable(disable);
        this.resetButton.setDisable(disable);
        this.priceExportMenuItem.setDisable(disable);
        this.disableAddingNewEntities(disable
                        || simulation.getEntitiesManager().getTotalNumberOfEntities() >= Constants.MAX_THREADS
                );
    }

    private void refreshMarketView() {
        this.marketListView.getItems().clear();
        for (var market : simulation.getMarkets())
            this.marketListView.getItems().add(market.getName());
    }

    private void refreshSMIView() {
        this.smiListView.getItems().clear();
        for (var idx : simulation.getEntitiesManager().getStockMarketIndexes())
            this.smiListView.getItems().add(idx.getName());
    }

    private void refreshAssetView(AssetCategory category) {
        ListView<String> listView = this.categoryMapping.get(category);
        listView.getItems().clear();
        listView.getItems().addAll(simulation.getAssetManager().getAssetsByCategory(category));
    }

    private void refreshListViews() {
        for (var category : AssetCategory.class.getEnumConstants())
            this.refreshAssetView(category);
        this.refreshMarketView();
        this.refreshInvestorView();
        this.refreshSMIView();
    }

    public void refreshInvestorView() {
        this.investorListView.getItems().clear();
        for (var investor : simulation.getEntitiesManager().getInvestors()) {
            this.investorListView.getItems().add(
                    investor.getFirstName() + ' ' + investor.getLastName() + " (" + investor.getID() + ')'
            );
        }
        this.disableAddingNewEntities(
                simulation.getEntitiesManager().getTotalNumberOfEntities() >= Constants.MAX_THREADS
        );
    }

    public void disableAddingNewEntities(boolean value) {
        this.generateMarketsButton.setDisable(value);
        this.stockAddButton.setDisable(value);
        this.investorAddButton.setDisable(value);
    }

    public void onPauseButtonClicked() {
        this.disableSimulationModifyingElements(!this.pauseButton.isSelected());
        if (this.pauseButton.isSelected()) {
            simRunner.pause();
        }
        else {
            simulation.getEntitiesManager().runIdleEntities();
            simRunner.unpause();
        }
    }

    public void onStartButtonClicked() {
        simRunner.start();
        this.startButton.setDisable(true);
        this.pauseButton.setDisable(false);
        this.disableSimulationModifyingElements(true);
    }

    public void onAssetPriceHistoryExport() {
        if (!simulation.hasStarted())
            return;
        var dataEx = new DataExporter();
        dataEx.exportLabeledData(simulation.getAssetManager().getAllAssetsPriceHistory(), "prices.csv");
    }

    public void onGenerateRandomMarketSelected() {
        for (var category : AssetCategory.class.getEnumConstants())
            simulation.setupRandomMarket(category);
        this.refreshListViews();
        this.refreshSimulationData();
    }

    public void onResetButtonClicked() {
        ensureSimulationStop();
        simulation = new Simulation();
        simRunner = new SimulationRunner(this, simulation);
        this.windowsManager.passReferenceToAllWindows(simulation);
        this.refreshListViews();
        this.refreshSimulationData();
        this.startButton.setDisable(false);
        this.pauseButton.setSelected(false);
        this.pauseButton.setDisable(true);
    }

    public void onPriceTrackerButtonClicked() {
        var source = MultiAssetPanelController.class.getResource("multi_asset_panel.fxml");
        this.windowsManager.openMultiAssetWindow(source, simulation);
    }

    public void onAddMarketButtonClicked() {
        var source = CreativePanelController.class.getResource("market_creation_panel.fxml");
        this.windowsManager.openNewCreativeWindow(source, "Market Creation Panel", simulation);
    }

    public void onAddCommodityButtonClicked() {
        var source = CreativePanelController.class.getResource("commodity_asset_creation_panel.fxml");
        this.windowsManager.openNewCreativeWindow(source, "Commodity Creation Panel", simulation);
    }

    public void onAddCurrencyButtonClicked() {
        var source = CreativePanelController.class.getResource("currency_asset_creation_panel.fxml");
        this.windowsManager.openNewCreativeWindow(source, "Currency Creation Panel", simulation);
    }

    public void onAddStockButtonClicked() {
        var source = CreativePanelController.class.getResource("company_creation_panel.fxml");
        this.windowsManager.openNewCreativeWindow(source, "Company Creation Panel", simulation);
    }

    public void onAddSMIButtonClicked() {
        var source = CreativePanelController.class.getResource("stock_index_creation_panel.fxml");
        this.windowsManager.openNewCreativeWindow(source, "Stock Index Creation Panel", simulation);
    }

    public void onAddInvestorButtonClicked() {
        var source = CreativePanelController.class.getResource("investor_creation_panel.fxml");
        this.windowsManager.openNewCreativeWindow(source, "Investor Creation Panel", simulation);
    }

    public void onSimulationConfigButtonClicked() {
        var source = ConfigurationPanelController.class.getResource("config_panel.fxml");
        this.windowsManager.openNewConfigWindow(source, simulation);
    }

    public void onListViewClicked(String fxmlFileName, String title, String id) {
        var source = AssetInfoPanelController.class.getResource(fxmlFileName);
        this.windowsManager.openNewInfoWindow(source, title, simulation, id);
    }

    public void newIndexAdded(String name) {
        this.smiListView.getItems().add(name);
    }

    public void newMarketAdded(String marketName) {
        this.marketListView.getItems().add(marketName);
        this.refreshSimulationData();
        this.refreshInvestorView();
    }

    public void newAssetAdded(String assetName, AssetCategory category) {
        this.categoryMapping.get(category).getItems().add(assetName);
        this.refreshSimulationData();
        this.disableAddingNewEntities(
                simulation.getEntitiesManager().getTotalNumberOfEntities() >= Constants.MAX_THREADS
        );
    }

    public void refreshSimulationData() {
        Platform.runLater(() -> {
            var decimal = new DecimalDisplayFormat(3);
            double funds = 0;
            for (var inv : simulation.getEntitiesManager().getInvestors()) {
                funds += inv.getInvestmentBudget();
            }
            double averagePrice = 0;
            var assetPrices = new HashMap<>(simulation.getAssetManager().getAssetOpeningPrices());
            for (var entry : assetPrices.entrySet()) {
                averagePrice += entry.getValue();
            }
            averagePrice /= assetPrices.size();
            int naiveCount = 0;
            int qualitativeCount = 0;
            int allCount = this.simulation.getEntitiesManager().getInvestors().size();
            for (var investor : this.simulation.getEntitiesManager().getInvestors()) {
                var strategy = investor.getStrategy();
                if (strategy instanceof NaiveInvestmentStrategy)
                    naiveCount++;
                else if (strategy instanceof QualitativeAssessmentStrategy)
                    qualitativeCount++;
            }

            int processed = 0;
            int removed = 0;
            int updated = 0;

            for (var market : this.simulation.getMarkets()) {
                processed += market.getTransactionsCount();
                removed += market.getRemovedCount();
                updated += market.getUpdatedCount();
            }

            this.processedLabel.setText(String.valueOf(processed));
            this.updatedLabel.setText(String.valueOf(updated));
            this.removedLabel.setText(String.valueOf(removed));
            this.naiveCountLabel.setText(String.valueOf(naiveCount));
            this.qualitativeCountLabel.setText(String.valueOf(qualitativeCount));
            this.momentumCountLabel.setText(String.valueOf(allCount - naiveCount - qualitativeCount));
            this.simulationDayLabel.setText(String.valueOf(simulation.getSimulationDay()));
            this.entitiesCountLabel.setText(String.valueOf(simulation.getEntitiesManager().getTotalNumberOfEntities()));
            this.totalFundsLabel.setText(decimal.format(funds));
            this.assetPriceLabel.setText(decimal.format(averagePrice));
            this.windowsManager.refreshAllWindows();
        });
    }

    public static void ensureSimulationStop() {
        simRunner.stopRunning();
    }

    public void setOnCloseEvent() {
        this.startButton.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
            e -> this.windowsManager.closeAllWindows());
    }

    public WindowsManager getWindowsManager() {
        return this.windowsManager;
    }
}
