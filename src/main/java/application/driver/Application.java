package application.driver;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main_window.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load());
        stage.setTitle("Market Simulator");
        stage.setScene(mainScene);
        stage.getIcons().add(new Image(Application.class.getResource("icon.ico").toString()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        MainController.ensureSimulationStop();
    }
}