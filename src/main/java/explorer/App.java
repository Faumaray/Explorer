package explorer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    final static String rootPath = "..\\";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("primary.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle(rootPath);
        stage.setScene(scene);
        PrimaryController controller=fxmlLoader.<PrimaryController>getController();
        controller.initData(rootPath);
        stage.show();
    }
    public static void main(String[] args) 
    {
        launch();

    }
}