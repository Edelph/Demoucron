package edelph.jhon.vue.controller;

import edelph.jhon.shapes.Demoucron;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class TableController {

    public static Pane build(Demoucron demoucron){
        return build(demoucron,-1);
    }
    public static Pane build(Demoucron demoucron, int i){
        List<List<Integer>> matrice = demoucron.getMatrice();

        GridPane table = new GridPane();
        table.getStyleClass().add("table");
        for (int r = 0; r < matrice.size(); r++) {
            List<Integer> list = matrice.get(r);
            for (int c = 0; c < list.size(); c++) {
                table.add(getLabel(list.get(c)),c,r);
            }
        }
        table.setHgap(1);
        table.setVgap(1);
        table.setGridLinesVisible(true);

        if(i>0){
            VBox vbox = new VBox();
            Label label = new Label("K = "+(i+1));
            vbox.getChildren().add(label);
            vbox.getChildren().add(table);
            vbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setSpacing(10);
            return vbox;
        }
        return table;
    }

    private static Label getLabel(Integer value){
        if(value>=0) return new Label(Integer.toString(value));
        return new Label("~");
    }
}
