package application.driver;

import application.panels.MarketCreationPanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import simulation.core.Simulation;

import java.io.IOException;

public class Controller {
    private Simulation simulation;

    @FXML
    private Button addMarketButton;
    @FXML
    private Button addCompanyButton;
    @FXML
    private Button addAssetButton;

    public void onAddMarketButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MarketCreationPanelController.class.getResource("market_creation_panel.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load());
        MarketCreationPanelController controller = (MarketCreationPanelController) fxmlLoader.getController();
        controller.passSimulationReference(simulation);
        Stage stage = new Stage();
        stage.setTitle("Market Creation Panel");
        stage.setScene(mainScene);
        stage.getIcons().add(new Image(Application.class.getResource("icon.ico").toString()));
        stage.show();
    }
}
