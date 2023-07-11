package edelph.jhon.shapes;

import java.util.ArrayList;
import java.util.List;

public class Demoucron {
    private List<List<Integer>> matrice;

    public Demoucron(List<List<Integer>> matrice) {
        this.matrice = matrice;
    }
    public Demoucron (Integer[][] matrice){
        this.matrice = new ArrayList<>();
        int dim = matrice.length;
        for(int i=0; i< dim; i++){
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < dim ; j++) {
                list.add(matrice[i][j]);
            }
            this.matrice.add(list);
        }
    }

    private boolean isIn(int index) {
        for (int i = 0; i < matrice.size() ; i++) {
            List<Integer> line = matrice.get(i);
            if (line.get(index) >= 0) return true;
        }
        return false;
    }
    private boolean isOut(int index) {
        List<Integer> line = matrice.get(index);
        for (int i = 0; i < line.size() ; i++) {
            if (line.get(i) >= 0 ) return true;
        }
        return false;
    }

    private List<Integer> getIn(int index) {
        List<Integer> in = new ArrayList<Integer>();
        for (int i = 0; i < matrice.size() ; i++) {
            List<Integer> line = matrice.get(i);
            if (line.get(index) >= 0) in.add(i);
        }
        return in;
    }

    private  List<Integer> getOut(int index) {
        List<Integer> out = new ArrayList<Integer>();
        List<Integer> line = matrice.get(index);
        for (int i = 0; i < line.size() ; i++) {
            if (line.get(i) >= 0 ) out.add(i);
        }
        return out;
    }
    public void voir(){
        for (int i = 0; i <matrice.size(); i++) {
            if (isIn(i) && isOut(i))
                showInOut(i);

        }
    }
    public void calculate(){
        for (int i = 0; i <matrice.size() ; i++) {
            if (isIn(i) && isOut(i)){
                calculateAllWij(i);
            }

        }
        showChemin();
    }
    public boolean isCalculable(int index){
        return isIn(index) && isOut(index);
    }
    public void voir(int index){
        if (isIn(index) && isOut(index)){
            --index;
            showInOut(index);
            List<Integer> Wij = calculateAllWij(index);
            Wij.forEach(System.out::println);
        }
    }
    private Integer getIn(int index, int Vin){
        return matrice.get(Vin).get(index);
    }

    private  Integer getOut(int index, int Vout){
        return  matrice.get(index).get(Vout);
    }

    private void showInOut(int index) {
        List<Integer> out = getOut(index);
        List<Integer> in = getIn(index);

        System.out.println("\n\nindex : " +(index+1));
        System.out.println("entrer:");
        in.forEach(i-> System.out.print((i+1)+" " ));

        System.out.println("\nsortie:");
        out.forEach(i-> System.out.print((i+1)+" " ));
        System.out.println("");
    }

    private Integer calculateWij(int index, Integer Vin, Integer Vout ){
        return getIn(index, Vin ) + getOut(index, Vout);
    }
    public List<Integer> calculateAllWij(int index){
        List<Integer> out = getOut(index);

        List<Integer> in = getIn(index);
        List<Integer> Wij = new ArrayList<>();
        for (Integer Vin : in ) {
            for (Integer Vout : out ) {
                Wij.add(calculateWij(index,Vin, Vout));
                Integer minValue = min(calculateWij(index,Vin,Vout),matrice.get(Vin).get(Vout));
//                showMin(Vin , Vout , minValue);
                change(Vin , Vout, minValue);
            }
        }
        return Wij;
    }

    private Integer min(Integer number1, Integer number2){
        if (number1 < 0)
            return number2;
        if (number2 < 0)
            return number1;
        return number1 <= number2?number1:number2;
    }

    private void showMin(int Vin, int Vout, Integer min){
        System.out.println("MIN (W " + Vin + " "+ Vout + " , Vij) = " + min);
    }

    private void change(int Vin, int Vout, Integer value){
        matrice.get(Vin).set(Vout,value);
    }

    public void showMatrice(){
        for (List<Integer> line : matrice) {
            for (Integer value : line) {
                System.out.print(value + " " );
            }
            System.out.println("");
        }
    }

    public List<Integer> cheminMin(){
        int index = getIndexFinal();
        List<Integer> chemin = new ArrayList<>();
        chemin.add(index);
        for (int i = 0; i <matrice.size()-1 ; i++) {
            int preIndex = getIndexMin(index);
            chemin.add(preIndex);
            index = preIndex;
        }
        return chemin;
    }

    private void showChemin(){
        List<Integer> chemin = cheminMin();
        System.out.println("chemin : ");
        System.out.println("chemin = " + chemin.size());
        for (Integer index : chemin) {
            System.out.println(index +1);

        }
    }

    private int getIndexMin(int index) {
        int min = 0;
        for (int i = 1; i <matrice.size() ; i++) {
            if (matrice.get(i).get(index) >= 0){
                if (matrice.get(min).get(index) <0 )
                    min = i;
                else if  (matrice.get(i).get(index) < matrice.get(min).get(index)) min = i;
            }
        }
        return min;
    }
    public int getIndexFinal(){
        int index = 0;
        for (List<Integer> list : this.matrice ) {
            boolean isNull = true;
            for (Integer value : list) {
                if(value >= 0) {
                    isNull = false;
                    break;
                }
            }
            if (isNull) {
                index = matrice.indexOf(list);
                break;
            }
        }
        return index;
    }

    public List<List<Integer>> getMatrice(){
        return matrice;
    }

}