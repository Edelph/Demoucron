package edelph.jhon.shapes;

import edelph.jhon.vue.controller.DialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;

public class DialogaGetValue {
    private static DialogController controller;
    private static Stage stage;
    public static Optional<String> buid(String title, String contentText,String defaultValue){
        if(controller == null || stage == null) createController();
        controller.setDefaultValue(defaultValue);
        controller.setContent(contentText);
        stage.showAndWait();
        if(controller.isOk()) return Optional.of(controller.getInputValue());
        return Optional.empty();
    }
    public static void createController(){
        try {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(DialogaGetValue.class.getResource("/edelph/jhon/vue/fxml/dialog-get-value.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
