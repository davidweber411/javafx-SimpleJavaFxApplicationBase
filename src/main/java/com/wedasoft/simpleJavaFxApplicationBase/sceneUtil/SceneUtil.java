package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * @author davidweber411
 */
public class SceneUtil {

    /**
     * Gets the {@link Stage} on which the node is located, which fired the {@link ActionEvent}. For example a {@link Button}.
     *
     * @param event The {@link ActionEvent}.
     * @return The {@link Stage}.
     */
    public static Stage getStageByActionEvent(ActionEvent event) {
        return getStageByChildNode(((Node) event.getSource()));
    }

    /**
     * Gets the {@link Stage} on which the {@link Node} is located on.
     *
     * @param node The {@link Node}.
     * @return The {@link Stage} containing the {@link Node}.
     */
    public static Stage getStageByChildNode(Node node) {
        return getStageByScene(node.getScene());
    }

    /**
     * Gets the {@link Stage} on which the {@link Scene} is located on.
     *
     * @param scene The {@link Scene}.
     * @return The {@link Stage} containing the {@link Scene}.
     */
    public static Stage getStageByScene(Scene scene) {
        return (Stage) scene.getWindow();
    }

    /**
     * The {@link Consumer} parameter of this method is used to execute a method of the new controller after everything is done.
     * The {@link Consumer} method can be used for passing values to the new controller or for initializing something in the new controller.<br><br>
     * How to use: <br>
     * <pre><code>
     * switchSceneRoot(
     *     stage,
     *     getClass().getResource("/com/example/project/views/new-view.fxml"),
     *     (Consumer&lt;NewViewController&gt;) newViewController -> newViewController.init(a,b,c,...));</code></pre>
     *
     * @param stage                  The stage containing the scene.
     * @param absoluteFxmlFileUrl    The absolute url of the new fxml file.
     * @param initMethodOfController A method of the new controller which shall be executed when it is loaded completely.
     * @throws IOException On error.
     */
    public static void switchSceneRoot(
            Stage stage,
            URL absoluteFxmlFileUrl,
            @SuppressWarnings("rawtypes") Consumer initMethodOfController)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(absoluteFxmlFileUrl);
        stage.getScene().setRoot(loader.load());
        Object viewController = loader.getController();
        if (initMethodOfController != null) {
            //noinspection unchecked
            initMethodOfController.accept(viewController);
        }
    }

}
