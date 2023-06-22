package edelph.jhon.shapes;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Round extends Group{
    private static double radius = 15;
    private double centerX;
    private double centerY;
    private String name = "x1";
    private Text text;
    private Circle circle;
    private List<Arrow> arrowOut;
    private List<Arrow> arrowIn;
    public Round(double cx, double cy, int name) {
        this(new Circle(), new Text());
        setName(name);
        centerX = cx;
        centerY = cy;
        propertyCircle();
        posText();
        event();
    }
    private Round(Circle circle, Text text){
        super(circle, text);
        this.circle = circle;
        this.text = text;
    }
    private void posText(){
        double height = text.getBoundsInLocal().getCenterY();
        double width = text.getBoundsInLocal().getCenterX();
        text.setX(circle.getCenterX()-(width));
        text.setY(circle.getCenterY()-(height));
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

}
