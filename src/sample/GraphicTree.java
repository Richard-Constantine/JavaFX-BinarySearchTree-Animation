package sample;

import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Draws the tree and updates the graphics to display according to the location
 * @author Gal Shure
 * @version 1.0
 */


public class GraphicTree {
    private double splitTreeArea;
    private double divXdrawLine;
    private double divLineLenghtX;
    private double divLineLenghtY;
    private int linesCounter;
    public HelperStructures helperStructures = new HelperStructures();
    public Binary_Tree binary_tree = new Binary_Tree();

    /**
     * C'tor - define start points
     */
    public GraphicTree() {
        this.splitTreeArea = 240; // Give the right initial space
        this.divXdrawLine = 240; //Responsible for positioning on the X axis when drawing lines
        this.divLineLenghtX = 225; //Responsible for the lenght of the line when drawing lines
        this.divLineLenghtY = 15;
        this.linesCounter = 0; //index for ArrayList line in HelperStructures Class
    }

    /**
     * Placing the nodes first at the bottom of the screen
     */
    public void locateNodeDown() {
        int coordinate = 0;
        int locationXCircle, locationXnum;
        for (Map.Entry<Text, Circle> e : helperStructures.circleAndNumMap.entrySet()) {
            if (helperStructures.keyForCircles.size() <= 15) {
                locationXCircle = 1130 - coordinate;
                locationXnum = 1120;
                locateNodesBottom(e, locationXCircle, locationXnum, coordinate);
                coordinate += 62;

            } else {
                locationXCircle = 1300 - coordinate;
                locationXnum = 1290;
                locateNodesBottom(e, locationXCircle, locationXnum, coordinate);
                coordinate += 55;
            }
        }


    }

    /**
     * Recursive function that Draw the tree , pick up the nodes from the bottom up to the tree
     */
    public void drawTree(Binary_Tree.Node binary_TreeNode, Map<Text, Circle> circleAndNumMap, double endX, int endY, double splitTreeArea) {
        if (binary_TreeNode == null)
            return;

        String newRootText = Integer.toString(binary_TreeNode.getData());
        for (Map.Entry<Text, Circle> e : circleAndNumMap.entrySet()) {
            if (Objects.equals(e.getKey().getText(), newRootText)) {
                locateNodesUp(e, Integer.parseInt(e.getValue().getId()), endX, endY);
            }
        }

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.5), e -> drawTree(binary_TreeNode.getLeft(), circleAndNumMap, endX - (splitTreeArea), endY + 125, splitTreeArea / 2.1)),
                new KeyFrame(Duration.seconds(1.5), e -> drawTree(binary_TreeNode.getRight(), circleAndNumMap, endX + (splitTreeArea), endY + 125, splitTreeArea / 2.1))
        );
        timeline.play();
    }


    /**
     * helper function for drawTree
     */
    public void locateNodesUp(Map.Entry<Text, Circle> e, int startX, double endX, int endY) {

        Line linePath1 = new Line(startX, 630, endX, endY);

        PathTransition path = new PathTransition();
        PathTransition path2 = new PathTransition();

        path.setNode(e.getValue());
        path.setDuration(Duration.seconds(2));
        path.setPath(linePath1);

        path2.setNode(e.getKey());
        path2.setDuration(Duration.seconds(2));
        path2.setPath(linePath1);

        ParallelTransition pr = new ParallelTransition(path, path2);
        pr.play();


    }

    /**
     * Placing the node in the bottom of the screen
     */
    public void locateNodesBottom(Map.Entry<Text, Circle> e, int locationXCircle, int locationXnum, int coordinate) {

        e.getValue().setCenterX(locationXCircle);
        e.getValue().setCenterY(630);
        e.getValue().setRadius(25);
        e.getValue().setStroke(Color.YELLOW);
        e.getValue().setId(Integer.toString(locationXCircle)); //In order we can pull the node up from its exact position

        //fit the text to the circles graphic
        e.getKey().setFont(new Font(20));
        e.getKey().setBoundsType(TextBoundsType.VISUAL);
        e.getKey().setX(locationXnum - coordinate);
        e.getKey().setY(640);
        e.getKey().setFill(Color.WHITE);

    }

    /**
     * Recursive function that Placing the lines according to the tree
     */
    public void locateLinesOnTree(ArrayList<Line> linesAdd, Binary_Tree.Node root, double startX, double startY, double endY, double divLineLenghtX, double divLineLenghtY, double divXdrawLine) {
        if (root == null)
            return;
        if (root.getLeft() != null) {
            drawLine(linesAdd, startX - 15, startY, startX - divLineLenghtX, endY + divLineLenghtY);

        }
        if (root.getRight() != null) {
            drawLine(linesAdd, startX + 15, startY, startX + divLineLenghtX, endY + divLineLenghtY);
        }

        locateLinesOnTree(linesAdd, root.getLeft(), startX - divXdrawLine, startY + 125, startY + divLineLenghtY + 195, divLineLenghtX / 2.25, divLineLenghtY / 2.0, divXdrawLine / 2.1);
        locateLinesOnTree(linesAdd, root.getRight(), startX + divXdrawLine, startY + 125, startY + divLineLenghtY + 195, divLineLenghtX / 2.25, divLineLenghtY / 2.0, divXdrawLine / 2.1);

    }

    /**
     * Placing the line , adding each line to an arrayList of lines
     */
    public void drawLine(ArrayList<Line> linesAdd, double startX, double startY, double endX, double endY) {

        linesAdd.add(linesCounter, new Line());
        linesAdd.get(linesCounter).setStartX(startX);
        linesAdd.get(linesCounter).setStartY(startY);
        linesAdd.get(linesCounter).setEndX(endX);
        linesAdd.get(linesCounter).setEndY(endY);
        linesAdd.get(linesCounter).setStrokeWidth(2);
        linesAdd.get(linesCounter).setStroke(Color.WHITE);

        linesCounter++;
    }


    public double getSplitTreeArea() {
        return splitTreeArea;
    }


    public double getDivXdrawLine() {
        return divXdrawLine;
    }


    public double getDivLineLenghtX() {
        return divLineLenghtX;
    }


    public double getDivLineLenghtY() {
        return divLineLenghtY;
    }


    public int getLinesCounter() {
        return linesCounter;
    }

    public void setLinesCounter(int linesCounter) {
        this.linesCounter = linesCounter;
    }



}


