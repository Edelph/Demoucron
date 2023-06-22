package edelph.jhon.vue.controller;

import edelph.jhon.shapes.Arrow;
import edelph.jhon.shapes.Round;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowCtrl implements Initializable {

    @FXML
    private Button btn_select;

    @FXML
    private Button btn_arrow;

    @FXML
    private Button btn_circle;

    @FXML
    private Pane canvas_container;

    private List<Arrow> arrowList;
    private List<Round> roundList;

    private EventHandler<MouseEvent> mouseEntered, mouseDragged, mouseExited;


    @FXML
    void btn_arrow_clicked(ActionEvent event) {
        addArrowListener();
    }

    @FXML
    void btn_clircle_clicked(ActionEvent event) {
        addCircleListener();
    }

    @FXML
    void btn_select_onClicked(ActionEvent event) {
        remouveALLEvent();
    }
    void addArrowListener(){
        remouveALLEvent();
        if(roundList == null || roundList.size() == 0) return;

        mouseEntered = evt -> {
            Arrow arrow = new Arrow();
            Optional<Round> round = getRound(evt.getX(), evt.getY());
            if(round.isEmpty()) {
                return;
            }
            arrow.setRoundOut(round.get());
            arrowList.add(arrow);
            canvas_container.getChildren().add(arrow);

        };
        mouseDragged = evt->{
            Arrow arrow = arrowList.get(arrowList.size() - 1);
            arrow.setEndX(evt.getX());
            arrow.setEndY(evt.getY());
        };
        mouseExited = evt->{
            Optional<Round> round = getRound(evt.getX(), evt.getY());
            Arrow arrow = arrowList.get(arrowList.size() - 1);
            if(round.isEmpty()) {
                arrow.getRoundOut().removeRecentArrowOut();
                canvas_container.getChildren().remove(arrow);
                arrowList.remove(arrow);
                return;
            }
            arrow.setRoundIn(round.get());
        };
        canvas_container.addEventFilter(MouseEvent.MOUSE_PRESSED,mouseEntered);
        canvas_container.addEventFilter( MouseEvent.MOUSE_DRAGGED,mouseDragged);
        canvas_container.addEventFilter(MouseEvent.MOUSE_RELEASED,mouseExited);
    }

    void addCircleListener(){
        remouveALLEvent();
        mouseEntered = evt->{
            int id = roundList.size()+1;
            Round round = new Round(evt.getX(), evt.getY(),id);
            roundList.add(round);
            canvas_container.getChildren().add(round);
        };
        canvas_container.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEntered);
    }
    void remouveALLEvent(){
        if(mouseEntered != null) canvas_container.removeEventFilter(MouseEvent.MOUSE_CLICKED,mouseEntered);
        if(mouseEntered != null) canvas_container.removeEventFilter(MouseEvent.MOUSE_PRESSED,mouseEntered);
        if(mouseDragged != null) canvas_container.removeEventFilter(MouseEvent.MOUSE_DRAGGED,mouseDragged);
        if(mouseExited != null) canvas_container.removeEventFilter(MouseEvent.MOUSE_RELEASED,mouseExited);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.arrowList = new ArrayList<>();
        this.roundList = new ArrayList<>();
    }
    private Optional<Round> getRound(double px, double py){
        for (Round round : roundList) {
            if(round.isInArea(px, py)) return Optional.of(round);
        }
        return Optional.empty();
    }
}
