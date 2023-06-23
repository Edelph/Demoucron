package edelph.jhon.shapes;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Arrow extends Group {
    public static List<Arrow> arrowList;
    public static Pane parent;
    private final Line line;
    private Line arrow1;
    private Label text;
    private double height, width;
    private Round roundOut , roundIn;
    private Line arrow2;
    private static ContextMenu menu;

    public Arrow() {
        this(new Line(), new Line(), new Line(), new Label("label"));
    }

    private static final double arrowLength = 7;
    private static final double arrowWidth = 3;

    private Arrow(Line line, Line arrow1, Line arrow2,Label label) {
        super(line, arrow1, arrow2, label);
        this.line = line;
        this.arrow1 = arrow1;
        this.arrow2 = arrow2;
        this.text = label;
        createContextMenu();
        addEventMenu();
        event();

        InvalidationListener updater = o -> {
            double ex = getEndX();
            double ey = getEndY();
            double sx = getStartX();
            double sy = getStartY();

            arrow1.setEndX(ex);
            arrow1.setEndY(ey);
            arrow2.setEndX(ex);
            arrow2.setEndY(ey);

            if (ex == sx && ey == sy) {
//                 arrow parts of length 0
                arrow1.setStartX(ex);
                arrow1.setStartY(ey);
                arrow2.setStartX(ex);
                arrow2.setStartY(ey);
            } else {
                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

                // part in direction of main line
                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                // part ortogonal to main line
                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;

                arrow1.setStartX(ex + dx - oy);
                arrow1.setStartY(ey + dy + ox);
                arrow2.setStartX(ex + dx + oy);
                arrow2.setStartY(ey + dy - ox);
                if(roundOut != null){
                   setText();
                }
            }
        };


        // add updater to properties
        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
        updater.invalidated(null);
    }

    // start/end properties

    public final void setStartX(double value) {
        line.setStartX(value);
        line.setEndX(value);
    }

    private void setText() {
        text.layoutXProperty().bind(line.startXProperty().add(width/2));
        text.layoutYProperty().bind(line.startYProperty().add(height/2));
    }

    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    public final void setStartY(double value) {
        line.setStartY(value);
        line.setEndY(value);
    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final void setEndX(double value) {
        line.setEndX(value);
    }

    public final double getEndX() {
        return line.getEndX();
    }

    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    public final void setEndY(double value) {
        line.setEndY(value);
    }

    public final double getEndY() {
        return line.getEndY();
    }

    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }

    public void showPos(){
        System.out.println("start:");
        System.out.println("x : "+ getStartX()+ " y : "+ getStartY());
        System.out.println("end:");
        System.out.println("x : "+ getEndX()+ " y : "+ getEndY());
    }
    public void selected(){
        line.setStroke(Color.RED);
        arrow1.setStroke(Color.RED);
        arrow2.setStroke(Color.RED);
    }
    private void event(){
        this.setOnMouseClicked(evt->selected());
    }

    public Round getRoundOut() {
        return roundOut;
    }

    public void setRoundOut(Round roundOut) {
        roundOut.setArrowOut(this);
        setStartX(roundOut.getCenterX());
        setStartY(roundOut.getCenterY());
        this.roundOut = roundOut;
    }

    public Round getRoundIn() {
        return roundIn;
    }

    public void setRoundIn(Round roundIn) {
        roundIn.setArrowIn(this);
        this.roundIn = roundIn;
        setEndY(roundIn.getCenterY());
        setEndX(roundIn.getCenterX());
        update();
    }
    private void update(){
        height = line.getEndY() - line.getStartY();
        width = line.getEndX() - line.getStartX();
        double length = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));

        double subtractWidth = Round.getRadius() * width / length;
        double subtractHeight = Round.getRadius() * height / length;
        setLength(subtractWidth, subtractHeight);
    }
    private void setLength(double subtractWidth, double subtractHeight){
        if(roundOut.getCenterX()>roundIn.getCenterX()) {
            if (line.getStartX() > line.getEndX()) {
                line.setEndX(roundIn.getCenterX() - subtractWidth);
                line.setStartX(roundOut.getCenterX() + subtractWidth);
            } else {
                line.setEndX(roundIn.getCenterX() + subtractWidth);
                line.setStartX(roundOut.getCenterX() - subtractWidth);
            }

        }else{
            if (line.getStartX() > line.getEndX()) {
                line.setEndX(roundIn.getCenterX() + subtractWidth);
                line.setStartX(roundOut.getCenterX() - subtractWidth);
            } else {
                line.setEndX(roundIn.getCenterX() - subtractWidth);
                line.setStartX(roundOut.getCenterX() + subtractWidth);
            }

        }
        if(roundOut.getCenterY() > roundIn.getCenterY()) {
            if (line.getStartY() < line.getEndY()) {
                line.setEndY(roundIn.getCenterY() + subtractHeight);
                line.setStartY(roundOut.getCenterY() - subtractHeight);
            } else {
                line.setEndY(roundIn.getCenterY() - subtractHeight);
                line.setStartY(roundOut.getCenterY() + subtractHeight);
            }
        }else {
            if(line.getStartY() < line.getEndY()){
                line.setEndY(roundIn.getCenterY() - subtractHeight);
                line.setStartY(roundOut.getCenterY() + subtractHeight);
            }
            else{
                line.setEndY(roundIn.getCenterY() + subtractHeight);
                line.setStartY(roundOut.getCenterY() - subtractHeight);
            }
        }
    }
    private void addEventMenu(){
        menu.getItems().get(0).setOnAction(event-> {
            Optional<String> value = DialogaGetValue.buid("valeur fleche","valeur ??",text.getText());
            value.ifPresent(s -> text.setText(s.trim()));
        });
        menu.getItems().get(1).setOnAction(event-> {
            revomeShapes();
        });
    }
    private void createContextMenu(){
        menu = new ContextMenu();
        MenuItem deleteMenu = new MenuItem("supprimer");
        MenuItem editMenu = new MenuItem("modifier");
        menu.getItems().addAll(editMenu, deleteMenu);
        text.setContextMenu(menu);
    }
    public void revomeShapes(){
        if(roundOut!=null){
            roundOut.removeArrowOut(this);
        }
        if(roundIn!=null){
            roundIn.removeArrowIn(this);
        }
        arrowList.remove(this);
        parent.getChildren().remove(this);
    }

    public void addList() {
        arrowList.add(this);
    }
    public String getValue(){
        return text.getText();
    }
}