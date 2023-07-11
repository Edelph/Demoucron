import edelph.jhon.vue.controller.MainWindowCtrl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new  FXMLLoader(Objects.requireNonNull(getClass().getResource("/edelph/jhon/vue/fxml/mainWindow.fxml")));
        Parent root = loader.load();
        MainWindowCtrl controller =  loader.getController();
        controller.setStage(primaryStage);
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/edelph/jhon/resources/shapeStyle.css")).toExternalForm());
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Algorithm Demmoucron");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}