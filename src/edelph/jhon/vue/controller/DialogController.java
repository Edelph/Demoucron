package edelph.jhon.vue.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogController {

    @FXML
    private Button btnOK;

    @FXML
    private TextField input;

    @FXML
    private Label textContent;
    private Stage stage;
    private boolean isOk = false;

    @FXML
    void btnOkClicked(ActionEvent event) {
        isOk = true;
        this.stage.close();
    }
    public String getInputValue(){
        if(isOk) {
            isOk = false;
            return input.getText();
        }
        return null;
    }
    public void setContent(String content){
        textContent.setText(content);
    }
    public boolean isOk(){
        return isOk;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void setDefaultValue(String value){
        input.setText(value);
    }
}
