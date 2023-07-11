package edelph.jhon.vue.controller;

import edelph.jhon.shapes.Arrow;
import edelph.jhon.shapes.Demoucron;
import edelph.jhon.shapes.Round;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class MainWindowCtrl implements Initializable {
    @FXML
    private VBox titre;

    @FXML
    private HBox contentTab;

    private VBox listTabContent;
    private ScrollPane scrollPane;

    @FXML
    private Button btn_select;

    @FXML
    private Button btn_arrow, btnCalculer;

    @FXML
    private Button btn_circle;

    @FXML
    private Pane canvas_container;

    private Arrow newArrow;
    private Stage stage;

    private EventHandler<MouseEvent> mouseEntered, mouseDragged, mouseExited;


    @FXML
    void btn_arrow_clicked(ActionEvent event) {
        addArrowListener();
    }

    @FXML
    void btnCalculer_onclicked(ActionEvent event) {
        if( Round.roundList == null || Round.roundList.size() == 0) return;
        if(scrollPane != null ) contentTab.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        listTabContent = new VBox();
        scrollPane.setContent(listTabContent);

        Round.initialize();
        Arrow.initialize();

        Demoucron demoucron = new Demoucron(Round.getMatrice());
        List<List<Integer>> matrice = demoucron.getMatrice();
        listTabContent.getChildren().add(TableController.build(demoucron));

        for (int i = 0; i < matrice.size(); i++) {
            if(demoucron.isCalculable(i)){
                demoucron.calculateAllWij(i);
                listTabContent.getChildren().add(TableController.build(demoucron,i));
            }
        }
        listTabContent.setSpacing(10);
        scrollPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/edelph/jhon/resources/table.css")).toExternalForm());
        contentTab.getChildren().add(scrollPane);
        scrollPane.setMaxHeight(347);
        scrollPane.setPannable(true);
//        listTabContent.setPadding(new Insets(10, 5, 0, 0));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(false);
        stage.sizeToScene();
        Round.setChemin(demoucron.cheminMin());
    }

    void showTable(Demoucron demoucron) {
        listTabContent = new VBox();
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
        if(Round.roundList == null || Round.roundList.size() == 0) return;
        mouseEntered = evt -> {
            newArrow = new Arrow();
            Optional<Round> round = getRound(evt.getX(), evt.getY());
            if(round.isEmpty()) {
                newArrow = null;
                return;
            }
            newArrow.setRoundOut(round.get());
            canvas_container.getChildren().add(newArrow);
        };
        mouseDragged = evt->{
            if( newArrow !=null){
                newArrow.setEndX(evt.getX());
                newArrow.setEndY(evt.getY());
            }
        };
        mouseExited = evt->{
            Optional<Round> round = getRound(evt.getX(), evt.getY());
            if((round.isEmpty() && newArrow!=null)
                    || (round.isPresent() && round.get() == newArrow.getRoundOut())
            ) {
                newArrow.getRoundOut().removeRecentArrowOut();
                canvas_container.getChildren().remove(newArrow);
                return;
            }
            if(newArrow!= null) {
                newArrow.setRoundIn(round.get());
                newArrow.addList();
                newArrow = null;
            };
        };
        canvas_container.addEventFilter(MouseEvent.MOUSE_PRESSED,mouseEntered);
        canvas_container.addEventFilter( MouseEvent.MOUSE_DRAGGED,mouseDragged);
        canvas_container.addEventFilter(MouseEvent.MOUSE_RELEASED,mouseExited);
    }

    void addCircleListener(){
        remouveALLEvent();
        mouseEntered = evt->{
            Round round = new Round(evt.getX(), evt.getY());
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
        Label titleLabel = new Label("Algorithm Demoucron");
        titre.getChildren().add(titleLabel);
        Arrow.arrowList = new ArrayList<>();
        Round.roundList = new ArrayList<>();
        Arrow.parent = canvas_container;
    }
    private Optional<Round> getRound(double px, double py){
        for (Round round : Round.roundList) {
            if(round.isInArea(px, py)) return Optional.of(round);
        }
        return Optional.empty();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
