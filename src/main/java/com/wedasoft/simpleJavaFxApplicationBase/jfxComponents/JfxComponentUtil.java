package com.wedasoft.simpleJavaFxApplicationBase.jfxComponents;

import com.wedasoft.simpleJavaFxApplicationBase.jfxComponents.diagram.DiagramComponent;
import com.wedasoft.simpleJavaFxApplicationBase.jfxComponents.diagram.DiagramType;
import javafx.scene.Scene;

import java.util.List;
import java.util.function.Function;

public class JfxComponentUtil {

    public static <CLASS_OF_SECTION, GETTER_RETURN_TYPE> Scene createDiagramSceneComponent(
            List<CLASS_OF_SECTION> elements,
            Function<CLASS_OF_SECTION, GETTER_RETURN_TYPE> getter,
            DiagramType diagramType,
            String diagramTitleDescription) {
        return new Scene(new DiagramComponent<>(elements, getter, diagramType, diagramTitleDescription));
    }

}
