//package dungeon.gui;
//
//import dungeon.engine.gameEngine;
//import dungeon.engine.map;
//import javafx.fxml.FXML;
//import javafx.scene.control.Cell;
//import javafx.scene.layout.GridPane;
//
//public class Controller {
//    @FXML
//    private GridPane gridPane;
//    private map map;
//
//    gameEngine engine;
//
//    @FXML
//    public void initialize() {
//        engine = new gameEngine();
//        updateGui();
//    }
//
//    private void updateGui() {
//        Clear old GUI grid pane
//        gridPane.getChildren().clear();
//
//        //Loop through map board and add each cell into grid pane
//        for(int i = 0; i < map.getSize(); i++) {
//            for (int j = 0; j < map.getSize(); j++) {
//                Cell cell = map.getMap()[i][j];
//                gridPane.add(cell, j, i);
//            }
//        }
//        gridPane.setGridLinesVisible(true);
//    }
//
//}
