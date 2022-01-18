package application.driver;

import application.panels.ConfigurationPanelController;
import application.panels.ReferencingController;
import application.panels.creative.CreativePanelController;
import application.panels.informative.InfoPanelController;
import application.panels.plot.PlotPanelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simulation.core.Simulation;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WindowsManager {
    private final Map<String, ReferencingController> controllers = new HashMap<>();
    private final MainController mainController;

    WindowsManager(MainController mainController) {
        this.mainController = mainController;
    }

    private ReferencingController openNewReferencingWindow(URL source, String title, Simulation simulation, String windowID) {
        var controller = this.controllers.get(windowID);
        if (controller != null) {
            controller.getStage().setAlwaysOnTop(true);
            controller.getStage().setAlwaysOnTop(false);
            return controller;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(source);
            Scene mainScene = new Scene(fxmlLoader.load());
            controller = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(mainScene);
            stage.getIcons().add(new Image(Application.class.getResource("icon.ico").toString()));
            stage.show();
            controller.passMainControllerReference(mainController);
            controller.passSimulationReference(simulation);
            this.controllers.put(windowID, controller);
            controller.getStage().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
                    e -> this.controllers.remove(windowID));
        } catch(IOException e) {
            System.out.println("Failed to open window " + title);
            System.out.println(e.getMessage());
        }
        return controller;
    }

    public ConfigurationPanelController openNewConfigWindow(URL source, Simulation simulation) {
        return (ConfigurationPanelController) this.openNewReferencingWindow(
                source,
                "Configuration Panel",
                simulation,
                "CONFIG");
    }

    public CreativePanelController openNewCreativeWindow(URL source, String title, Simulation simulation) {
        return (CreativePanelController) this.openNewReferencingWindow(source,title,simulation, title);
    }

    public PlotPanelController openNewPlotWindow(URL source, String title, Simulation simulation, String assetID) {
        var controller = (PlotPanelController) this.openNewReferencingWindow(source, title, simulation, title+assetID);
        controller.passAssetID(assetID);
        return controller;
    }

    public InfoPanelController openNewInfoWindow(URL source, String title, Simulation simulation, String id) {
        var controller = (InfoPanelController) this.openNewReferencingWindow(source, title, simulation, title+id);
        controller.passID(id);
        return controller;
    }

    public void refreshAllWindows() {
        for (var entry : this.controllers.entrySet()) {
            entry.getValue().refresh();
        }
    }

    public void closeAllWindows() {
        for (var entry : this.controllers.entrySet())
            entry.getValue().getStage().close();
    }


}
