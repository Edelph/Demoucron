package edelph.jhon.shapes;

import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Round extends Group{
    public static ArrayList<Round> roundList;
    private static double radius = 15;
    private double centerX;
    private double centerY;
    private String name = "x1";
    private Text text;
    private Circle circle;
    private List<Arrow> arrowOut;
    private List<Arrow> arrowIn;
    private ContextMenu menu;
    public Round(double cx, double cy) {
        this(new Circle(), new Text());
        setName(roundList.size()+1);
        centerX = cx;
        centerY = cy;
        propertyCircle();
        posText();
        event();
        createContextMenu();
        roundList.add(this);
        this.setOnMouseClicked(this::handleMousePressed);
        addEventMenu();
    }
    private Round(Circle circle, Text text){
        super(circle, text);
        this.circle = circle;
        this.text = text;
    }
    private void posText(){

        double height = text.getBoundsInLocal().getCenterY();
        double width = text.getBoundsInLocal().getCenterX();

        text.setLayoutX(circle.getCenterX()-(width));
        text.setLayoutY(circle.getCenterY()-(height));
        System.out.println("eee");
    }

    private void propertyCircle(){
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadius(radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1d);
    }

    public void setRadius(double radius){
        Round.radius=radius;
        circle.setRadius(Round.radius);
    }
    public void setName(int id){
        name = "X"+id;
        this.text.setText(name);
    }
    public boolean isInArea(double px, double py){
        double x = circle.getCenterX();
        double y = circle.getCenterY();
        double r = circle.getRadius();
        return (px >= x-r && px <= x+r && py >= y-r && py <= y+r);
    }

    public void event(){
        this.setOnMouseClicked(evt->{
            setActive();
        });
        this.setOnMouseEntered(evt->{
            setActive();
        });
        this.setOnMouseExited(evt->{
            setNonActive();
        });
    }
    private void setActive(){
        circle.setStroke(Color.BLUE);
        text.setFill(Color.BLUE);
    }
    private void setNonActive(){
        circle.setStroke(Color.BLACK);
        text.setFill(Color.BLACK);
    }

    public static double getRadius() {
        return radius;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }
    public void setArrowOut(Arrow arrowOut){
        if(this.arrowOut == null) this.arrowOut = new ArrayList<>();
        this.arrowOut.add(arrowOut);
        arrowOut.setStartX(circle.getCenterX());
        arrowOut.setStartY(circle.getCenterY());
    }
    public void setArrowIn(Arrow arrowIn){
        if(this.arrowIn == null) this.arrowIn = new ArrayList<>();
        this.arrowIn.add(arrowIn);
        arrowIn.setEndX(circle.getCenterX());
        arrowIn.setEndY(circle.getCenterY());
    }
    public void removeRecentArrowOut(){
        if(arrowOut != null && arrowOut.size() > 0){
            arrowOut.remove(arrowOut.size()-1);
        }
    }
    private void createContextMenu(){
        menu = new ContextMenu();
        MenuItem deleteMenu = new MenuItem("supprimer");
        MenuItem editMenu = new MenuItem("modifier");
        menu.getItems().addAll(editMenu, deleteMenu);
    }

    protected void handleMousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            menu.show((Node)e.getSource(), Side.RIGHT, 3, 5);
            e.consume();
        }
    }
    private void addEventMenu(){
        menu.getItems().get(0).setOnAction(event-> {
            Optional<String> value = DialogaGetValue.buid("Cercle","valeur ??",text.getText());
            value.ifPresent(s -> text.setText(s.trim()));
        });
        menu.getItems().get(1).setOnAction(event-> {
            revomeShapes();
        });
    }

    public void revomeShapes(){
        if(arrowIn!=null && arrowIn.size()>0) {
            for (Arrow a: arrowIn) {
                Round round = a.getRoundOut();
                round.removeArrowOut(a);
            }
            Arrow.arrowList.removeAll(arrowIn);
            Arrow.parent.getChildren().removeAll(arrowIn);
        }
        if(arrowOut!=null && arrowOut.size()>0) {
            for (Arrow a: arrowOut) {
                Round round = a.getRoundIn();
                round.removeArrowIn(a);
            }
            Arrow.arrowList.removeAll(arrowOut);
            Arrow.parent.getChildren().removeAll(arrowOut);
        }
        roundList.remove(this);
        Arrow.parent.getChildren().remove(this);
    }

    public void removeArrowIn(Arrow arrow) {
        arrowIn.remove(arrow);
    }
    public void removeArrowOut(Arrow arrow) {
        arrowOut.remove(arrow);
    }
    private Optional<Arrow> getArrowOut(Round round) {
        if(arrowOut!=null) {
            for (Arrow a : arrowOut) {
                if (a.getRoundIn().getValue().equalsIgnoreCase(round.getValue())) return Optional.of(a);
            }
        }
        return Optional.empty();
    }
    private Optional<Arrow> getArrowIn(Round round) {
        if(arrowIn!=null) {
            for (Arrow a : arrowIn) {
                if (a.getRoundOut() == round) return Optional.of(a);
            }
        }
        return Optional.empty();
    }

    public static Integer[][] getMatrice(){
        int dimension = roundList.size();
        Integer[][] matrice = new Integer[dimension][dimension];

        for (int r = 0; r < dimension; r++) {
            Round roundout = roundList.get(r);
            for (int c = 0; c < dimension; c++) {
                Round roundIn = roundList.get(c);
                Optional<Arrow> value = roundout.getArrowOut(roundIn);
                if(value.isPresent()) {
                    System.out.println(value.get().getValue());
                    matrice[r][c] = Integer.parseInt(value.get().getValue());
                }
            }
        }
        return matrice;
    }
    public String getValue(){
        return text.getText().trim();
    }



}
