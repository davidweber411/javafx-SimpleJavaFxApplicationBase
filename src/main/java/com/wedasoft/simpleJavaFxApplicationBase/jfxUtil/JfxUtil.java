package com.wedasoft.simpleJavaFxApplicationBase.jfxUtil;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import jdk.jfr.Experimental;

/**
 * <b style="color:red;">BE AWARE IF YOU USE THIS CLASS. THIS CLASS IS NOT TESTED.</b>
 */
@Experimental
public class JfxUtil {

    /**
     * <b style="color:red;">BE AWARE IF YOU USE THIS METHOD. THIS METHOD IS NOT TESTED.</b>
     */
    private static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

}
