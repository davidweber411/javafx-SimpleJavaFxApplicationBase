package com.wedasoft.simpleJavaFxApplicationBase.jfxUtil;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class JfxUtil {

    private static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

}
