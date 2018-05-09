package sample;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Constructs the GUI components and performs events for displaying and
 * manipulating the binary search tree.
 *
 * @author Gal Shure
 * @version 1.0
 */

public class Controller {

    public StringBuilder stringBuilder = new StringBuilder(" ");
    public Pane whiteGhost;
    public TextArea textArea; //Stay
    public TextField valField;
    private GraphicTree graphicTree = new GraphicTree();
    private ArrayList<Integer> checkDup = new ArrayList<>();
    private boolean postOrderFlag = false;
    private boolean inOrderFlag = false;
    private boolean preOrderFlag = false;
    private boolean builtTree = false;

    public enum Traversals {PostOrder, InOrder, preOrder;}

    /**
     * Performs the action when the insert button is clicked.
     */
    public void insertButton() {
        //
        try {
            int saveVal = Integer.parseInt(valField.getText());
        } catch (NumberFormatException e) {
            alertBox("Invalid input!\nEnter only numbers");
            valField.clear();
            return;
        }

        if (graphicTree.binary_tree.maxDepth(graphicTree.binary_tree.getRoot()) > 5) {
            clearButton();
            alertBox("Can not apply tree with more then 5 depth!\nPlease fill again");
            return;
        }

        if (builtTree) {
            alertBox("Can not add number to a built-in Tree!\nPlease clean up first ");
            valField.clear();
            return;
        }
        int saveVal = Integer.parseInt(valField.getText());
        String saveStringVal = Integer.toString(saveVal);
        for (int i = 0; i < graphicTree.helperStructures.keyForCircles.size(); i++)
            if (Objects.equals(saveStringVal, graphicTree.helperStructures.keyForCircles.get(i))) {
                valField.clear();
                return;
            }
        graphicTree.binary_tree.insert(saveVal);
        graphicTree.helperStructures.keyForCircles.add(saveStringVal);
        valField.clear();
    }

    /**
     * Performs the load when the insert button is clicked.
     */
    public void loadButton() {

        if (graphicTree.binary_tree != null) {
            clearButton();
        }
        FileChooser fileChooser = new FileChooser();
        Stage fileDialog = new Stage();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(fileDialog);
        if (file != null) {
            readFile(file);
        }
    }


    /**
     * Performs the load when the build button is clicked.
     */
    public void buildButton() {
        if (graphicTree.binary_tree.getRoot() == null || builtTree)
            return;
        stringBuilder.append("Numbers" + "🌳" + " : ");
        for (int i = 0; i < graphicTree.helperStructures.keyForCircles.size(); i++) {
            graphicTree.helperStructures.circleAndNumMap.put(new Text(graphicTree.helperStructures.keyForCircles.get(i)), new Circle());
            stringBuilder.append(graphicTree.helperStructures.keyForCircles.get(i) + "  ");
        }
        textArea.setText(stringBuilder.toString());

        graphicTree.locateNodeDown();
        for (Map.Entry<Text, Circle> e : graphicTree.helperStructures.circleAndNumMap.entrySet())
            whiteGhost.getChildren().addAll(e.getValue(), e.getKey());

        graphicTree.drawTree(graphicTree.binary_tree.getRoot(), graphicTree.helperStructures.circleAndNumMap, 700, 120, graphicTree.getSplitTreeArea());
        graphicTree.locateLinesOnTree(graphicTree.helperStructures.lines, graphicTree.binary_tree.getRoot(), 700, 140, 210, graphicTree.getDivLineLenghtX(), graphicTree.getDivLineLenghtY(), graphicTree.getDivXdrawLine());
        switchSecondsForDisplayLines();
        builtTree = true;
    }

    /**
     * Performs the search when the build button is clicked.
     */
    public void searchButton() {

        for (Map.Entry<Text, Circle> g : graphicTree.helperStructures.circleAndNumMap.entrySet()) {
            if (Objects.equals(g.getKey().getText(), valField.getText())) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0.5), e -> g.getValue().setStrokeWidth(6)),
                        new KeyFrame(Duration.seconds(1.0), e -> g.getValue().setStroke(Color.YELLOW)),
                        new KeyFrame(Duration.seconds(1.0), e -> g.getValue().setStrokeWidth(1))
                );
                timeline.setCycleCount(4);
                timeline.play();
                return;
            }

        }
        valField.clear();

        alertBox("Node doesn't exist");

    }

    /**
     * Performs the clearTree! when the build button is clicked.
     */
    public void clearButton() {
        for (Map.Entry<Text, Circle> e : graphicTree.helperStructures.circleAndNumMap.entrySet()) {
            whiteGhost.getChildren().removeAll(e.getKey(), e.getValue());
        }
        for (int i = 0; i < graphicTree.helperStructures.lines.size(); i++)
            whiteGhost.getChildren().remove(graphicTree.helperStructures.lines.get(i));

        graphicTree.helperStructures.lines.clear();
        graphicTree.setLinesCounter(0);
        graphicTree.helperStructures.keyForCircles.clear();
        graphicTree.helperStructures.circleAndNumMap.clear();
        stringBuilder.setLength(0);
        textArea.clear();
        valField.clear();
        graphicTree.binary_tree.deleteTree(graphicTree.binary_tree.getRoot());
        postOrderFlag = false;
        preOrderFlag = false;
        inOrderFlag = false;
        builtTree = false;
    }

    /**
     * Performs the preOrder when the build button is clicked.
     */
    public void preOrderButton() {
        if (!preOrderFlag)
            displayTraversals(Traversals.preOrder);
        else
            alertBox("PreOrder already displayed for this Tree");
    }

    /**
     * Performs the inOrder when the build button is clicked.
     */
    public void inOrderButton() {
        if (!inOrderFlag)
            displayTraversals(Traversals.InOrder);
        else
            alertBox("InOrder already displayed for this Tree");
    }

    /**
     * Performs the PostOrder when the build button is clicked.
     */
    public void postOrderButton() {
        if (!postOrderFlag)
            displayTraversals(Traversals.PostOrder);
        else
            alertBox("PostOrder already displayed for this Tree");

    }

    /**
     * An helper function for loading, inserts the numbers from the text file into the tree
     */
    private void readFile(File file) {
        BufferedReader bufferedReader = null;
        boolean dupNumInFile = false;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                try {
                    int saveVal = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    alertBox("Invalid file!\n Make sure that only numbers are entered\n Also each number in a new row");
                    clearButton();
                    return;
                }
                int saveVal = Integer.parseInt(text);
                if (graphicTree.binary_tree.maxDepth(graphicTree.binary_tree.getRoot()) > 5) {
                    alertBox("Can not apply tree with more then 5 depth");
                    clearButton();
                }
                String saveStringVal = Integer.toString(saveVal);

                for (int i = 0; i < graphicTree.helperStructures.keyForCircles.size(); i++)
                    if (Objects.equals(saveStringVal, graphicTree.helperStructures.keyForCircles.get(i))) {
                        graphicTree.helperStructures.keyForCircles.remove(i);
                        dupNumInFile = true;
                    }

                graphicTree.helperStructures.keyForCircles.add(saveStringVal);
                if (!dupNumInFile) {
                    graphicTree.binary_tree.insert(saveVal);
                } else
                    dupNumInFile = false;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return;
    }

    /**
     * An helper function that calls the alerts window if necessary
     */
    public void alertBox(String text) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);//gonna block any use of the window until take care of this one
        window.setMinWidth(250);
        window.setTitle("Note");

        Label label = new Label(text);
        Button Closebutton = new Button("Close this window");
        Closebutton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, Closebutton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 350, 100);
        window.setScene(scene);
        window.showAndWait();// gonna show the window and wait to event before he return to program
    }

    /**
     * A function that helps to schedule the occurrence of the lines according to the depth of the tree
     */
    public final void switchSecondsForDisplayLines() {

      //Get the depth of the tree
        int depth = graphicTree.binary_tree.maxDepth(graphicTree.binary_tree.getRoot());
        switch (depth) {
            case 2:
                Timeline timeline2 = new Timeline(
                        new KeyFrame(Duration.seconds(3.5), e -> displayLines(graphicTree.helperStructures.lines))
                );
                timeline2.play();
                break;

            case 3:
                Timeline timeline3 = new Timeline(
                        new KeyFrame(Duration.seconds(5), e -> displayLines(graphicTree.helperStructures.lines))
                );
                timeline3.play();
                break;
            case 4:
                Timeline timeline4 = new Timeline(
                        new KeyFrame(Duration.seconds(6.5), e -> displayLines(graphicTree.helperStructures.lines))
                );
                timeline4.play();
                break;
            case 5:
                Timeline timeline5 = new Timeline(
                        new KeyFrame(Duration.seconds(8), e -> displayLines(graphicTree.helperStructures.lines))
                );
                timeline5.play();
                break;
        }
    }

    /**
     * Perform the display of the lines on the screen
     */
    public void displayLines(ArrayList<Line> lines) {
        for (int i = 0; i < graphicTree.helperStructures.lines.size(); i++)
            whiteGhost.getChildren().add(graphicTree.helperStructures.lines.get(i));
    }

    /**
     * Perform the display of the Traversals according the button that was clicked.
     */
    public void displayTraversals(Traversals traversals) {
        switch (traversals) {
            case InOrder:
                inOrderFlag = true;
                graphicTree.binary_tree.inOrder(graphicTree.binary_tree.getRoot());
                stringBuilder.append(" | " + "  InOrder " + "🌳" + " : ");
                break;
            case preOrder:
                preOrderFlag = true;
                graphicTree.binary_tree.preOrder(graphicTree.binary_tree.getRoot());
                stringBuilder.append(" | " + "  PreOrder " + "🌳" + " : ");
                break;
            case PostOrder:
                postOrderFlag = true;
                graphicTree.binary_tree.postOrder(graphicTree.binary_tree.getRoot());
                stringBuilder.append(" | " + "  PostOrder " + "🌳" + " : ");
                break;
        }
        for (int i = 0; i < graphicTree.helperStructures.keyForCircles.size(); i++)
            stringBuilder.append(graphicTree.binary_tree.saveValForDisplay.get(i) + "  ");

        textArea.setText(stringBuilder.toString());
        graphicTree.binary_tree.saveValForDisplay.clear();
    }


    private class JavaFX_OpenFile {
    }
}



