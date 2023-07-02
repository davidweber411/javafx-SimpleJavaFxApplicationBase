package com.wedasoft.simpleJavaFxApplicationBase.jfxComponents.diagram;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DiagramComponent<CLASS_OF_SECTION, GETTER_RETURN_TYPE> extends StackPane {

    private final List<SectionGroup<CLASS_OF_SECTION, GETTER_RETURN_TYPE>> sectionGroups;

    public DiagramComponent(List<CLASS_OF_SECTION> elements, Function<CLASS_OF_SECTION, GETTER_RETURN_TYPE> getter, DiagramType diagramType, String diagramTitleDescription) {
        super();
        sectionGroups = createSectionGroups(elements, getter);
        createUiForDiagram(diagramType, diagramTitleDescription);
    }

    private List<SectionGroup<CLASS_OF_SECTION, GETTER_RETURN_TYPE>> createSectionGroups(
            List<CLASS_OF_SECTION> elements,
            Function<CLASS_OF_SECTION, GETTER_RETURN_TYPE> getter) {
        return new ArrayList<>(elements).stream()
                .collect(Collectors.groupingBy(getter))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> new SectionGroup<>(entry.getValue(), elements.size(), entry.getKey())))
                .entrySet().stream().sorted((o1, o2) -> {
                    if (o1.getValue().getPercentageInDiagram() == o2.getValue().getPercentageInDiagram()) return 0;
                    return o1.getValue().getPercentageInDiagram() > o2.getValue().getPercentageInDiagram() ? -1 : 1;
                })
                .map(Map.Entry::getValue)
                .toList();
    }

    private void createUiForDiagram(DiagramType diagramType, String diagramTitleDescription) {
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setWidth(USE_COMPUTED_SIZE);
        this.setHeight(USE_COMPUTED_SIZE);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new Label(diagramTitleDescription));

        if (DiagramType.BAR_DIAGRAM_SIMPLE == diagramType) {
            borderPane.setCenter(createUiForBarDiagramSimple());
        } else if (DiagramType.BAR_DIAGRAM_HORIZONTAL == diagramType) {
            borderPane.setCenter(createUiForBarDiagramHorizontal());
        } else {
            borderPane.setCenter(new Label("No diagram type selected."));
        }

        this.getChildren().add(borderPane);
    }

    private Node createUiForBarDiagramHorizontal() {
        return null;
    }

    private VBox createUiForBarDiagramSimple() {
        VBox center = new VBox();
        center.setPadding(new Insets(20, 0, 0, 0));
        center.setSpacing(20);
        center.getChildren().addAll(sectionGroups.stream()
                .map(sectionGroup -> {
                    Label leftLabel = new Label(sectionGroup.getGroupedByValueString());
                    Region spacingRegion = new Region();
                    HBox.setHgrow(spacingRegion, Priority.ALWAYS);
                    Label rightLabel = new Label(sectionGroup.getInfoAboutOccupancyString());
                    HBox labelHbox = new HBox(leftLabel, spacingRegion, rightLabel);

                    ProgressBar progressBar = new ProgressBar(sectionGroup.getPercentageInDiagram() / 100);
                    progressBar.setMaxWidth(Double.MAX_VALUE);

                    VBox vbox = new VBox();
                    vbox.getChildren().addAll(labelHbox, progressBar);
                    return vbox;
                })
                .toList());
        return center;
    }

}
