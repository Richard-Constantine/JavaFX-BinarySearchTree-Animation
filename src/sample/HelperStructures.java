package sample;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * These structures will help to organize the data correctly
 * @author Gal Shure
 * @version 1.0
 */
public class HelperStructures {
    public Map<Text, Circle> circleAndNumMap = new HashMap<Text, Circle>(); // will contain the text and the circle Shape
    public ArrayList<String> keyForCircles = new ArrayList<>(); //wil contain list of keys that will go into the key map
    public  ArrayList<Line> lines = new ArrayList<>();// contain array of lines according to the tree

}
