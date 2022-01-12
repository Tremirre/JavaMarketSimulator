package application.driver;

import application.panels.MarketCreationPanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import simulation.core.Simulation;

import java.io.IOException;

public class MainController {
    private Simulation simulation;

    @FXML
    private ListView<String> assetListView;
    @FXML
    private ListView<String> companyListView;
    @FXML
    private ListView<String> marketListView;

    public void initialize() {
        this.simulation = new Simulation();
    }

    public void onAddMarketButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MarketCreationPanelController.class.getResource("market_creation_panel.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load());
        MarketCreationPanelController controller = fxmlLoader.getController();
        controller.passSimulationReference(simulation);
        controller.passMainControllerReference(this);
        Stage stage = new Stage();
        stage.setTitle("Market Creation Panel");
        stage.setScene(mainScene);
        stage.getIcons().add(new Image(Application.class.getResource("icon.ico").toString()));
        stage.show();
    }

    public void newMarketAdded(String marketName) {
        this.marketListView.getItems().add(marketName);
    }

    public void newAssetAdded(String assetName) {
        this.assetListView.getItems().add(assetName);
    }

    public void newCompanyAdded(String companyName) {
        this.companyListView.getItems().add(companyName);
    }
}
