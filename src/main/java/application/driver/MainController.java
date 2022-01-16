package application.driver;

import application.panels.ConfigurationPanelController;
import application.panels.ReferencingController;
import application.panels.Refreshable;
import application.panels.creative.CreativePanelController;
import application.panels.informative.AssetInfoPanelController;
import application.panels.informative.InfoPanelController;
import application.panels.plot.PlotPanelController;
import application.util.SimulationRunner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simulation.asset.AssetCategory;
import simulation.core.Simulation;
import simulation.core.SimulationConfig;
import simulation.util.DataExporter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController {
    private static Simulation simulation;
    private static SimulationRunner simRunner;
    private final HashSet<Refreshable> refreshableControllers = new HashSet<>();
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
            SimulationConfig.getInstance().setTimeMultiplier((Double) newValue));
    }
    private void disableSimulationModifyingElements(boolean disable) {
        this.marketAddButton.setDisable(disable);
        this.currencyAddButton.setDisable(disable);
        this.commodityAddButton.setDisable(disable);
        this.stockAddButton.setDisable(disable);
        this.smiAddButton.setDisable(disable);
        this.generateMarketsButton.setDisable(disable);
        this.simulationConfigButton.setDisable(disable);
        this.resetButton.setDisable(disable);
        this.priceExportMenuItem.setDisable(disable);
    }

    public void openNewPlotWindow(URL source, String title, String assetID) {
        var controller = (PlotPanelController) this.openNewWindow(source, title);
        controller.getStage().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
                e -> this.refreshableControllers.remove(controller));
        controller.passAssetID(assetID);
        this.refreshableControllers.add(controller);
    }

    public void openNewInfoWindow(URL source, String title, String id) {
        var controller = (InfoPanelController) this.openNewWindow(source, title);
        controller.getStage().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
                e -> this.refreshableControllers.remove(controller));
        controller.passID(id);
        this.refreshableControllers.add(controller);
    }

    public ReferencingController openNewWindow(URL source, String title) {
        ReferencingController controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(source);
            Scene mainScene = new Scene(fxmlLoader.load());
            controller = fxmlLoader.getController();
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
        return controller;
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

    private void refreshInvestorView() {
        this.investorListView.getItems().clear();
        for (var investor : simulation.getEntitiesManager().getInvestors()) {
            this.investorListView.getItems().add(
                    investor.getFirstName() + ' ' + investor.getLastName() + " (" + investor.getID() + ')'
            );
        }
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
    }

    public void onResetButtonClicked() {
        ensureSimulationStop();
        simulation = new Simulation();
        simRunner = new SimulationRunner(this, simulation);
        this.refreshListViews();
        this.refreshSimulationData();
        this.startButton.setDisable(false);
        this.pauseButton.setSelected(false);
        this.pauseButton.setDisable(true);
    }

    public void onAddMarketButtonClicked() {
        var source = CreativePanelController.class.getResource("market_creation_panel.fxml");
        this.openNewWindow(source, "Market Creation Panel");
    }

    public void onAddCommodityButtonClicked() {
        var source = CreativePanelController.class.getResource("commodity_asset_creation_panel.fxml");
        this.openNewWindow(source, "Commodity Creation Panel");
    }

    public void onAddCurrencyButtonClicked() {
        var source = CreativePanelController.class.getResource("currency_asset_creation_panel.fxml");
        this.openNewWindow(source, "Commodity Creation Panel");
    }

    public void onAddStockButtonClicked() {
        var source = CreativePanelController.class.getResource("company_creation_panel.fxml");
        this.openNewWindow(source, "Company Creation Panel");
    }

    public void onAddSMIButtonClicked() {
        var source = CreativePanelController.class.getResource("stock_index_creation_panel.fxml");
        this.openNewWindow(source, "Stock Index Creation Panel");
    }

    public void onSimulationConfigButtonClicked() {
        var source = ConfigurationPanelController.class.getResource("config_panel.fxml");
        this.openNewWindow(source, "Configuration Panel");
    }

    public void onListViewClicked(String fxmlFileName, String title, String id) {
        var source = AssetInfoPanelController.class.getResource(fxmlFileName);
        this.openNewInfoWindow(source, title, id);
    }

    public void newIndexAdded(String name) {
        this.smiListView.getItems().add(name);
    }

    public void newMarketAdded(String marketName) {
        this.marketListView.getItems().add(marketName);
        this.refreshSimulationData();
    }

    public void newAssetAdded(String assetName, AssetCategory category) {
        this.categoryMapping.get(category).getItems().add(assetName);
        this.refreshInvestorView();
        this.refreshSimulationData();
    }

    public void refreshSimulationData() {
        Platform.runLater(() -> {
            this.simulationDayLabel.setText("Simulation Day: " + simulation.getSimulationDay());
            this.entitiesCountLabel.setText("Number of Entities: " + simulation.getEntitiesManager().getTotalNumberOfEntities());
            for (var controller : this.refreshableControllers)
                controller.refresh();
        });
    }

    public static void ensureSimulationStop() {
        simRunner.stopRunning();
    }

    public void setOnCloseEvent() {
        this.startButton.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
            e -> {
                for (var controller : this.refreshableControllers)
                    controller.getStage().close();
            });
    }
}
