package com.wedasoft.simpleJavaFxApplicationBase.jfxComponents.diagram;

import java.util.List;

public class SectionGroup<CLASS_OF_SECTION, GETTER_RETURN_TYPE> {

    private final List<CLASS_OF_SECTION> elementsOfSectionGroup;
    private final int sizeOfAllElementsOfAllSections;
    private final double percentageInDiagram;
    private final GETTER_RETURN_TYPE groupedByValue;

    public SectionGroup(List<CLASS_OF_SECTION> elementsOfSectionGroup, int sizeOfAllElementsOfAllSections, GETTER_RETURN_TYPE groupedByValue) {
        this.groupedByValue = groupedByValue;
        this.elementsOfSectionGroup = elementsOfSectionGroup;
        this.sizeOfAllElementsOfAllSections = sizeOfAllElementsOfAllSections;
        this.percentageInDiagram = calcPercentageInDiagram();
    }


    private double calcPercentageInDiagram() {
        return 100d / sizeOfAllElementsOfAllSections * elementsOfSectionGroup.size();
    }

    public List<CLASS_OF_SECTION> getElementsOfSectionGroup() {
        return elementsOfSectionGroup;
    }

    public double getPercentageInDiagram() {
        return percentageInDiagram;
    }

    public String getInfoAboutOccupancyString() {
        String ratioPart = elementsOfSectionGroup.size() + "/" + sizeOfAllElementsOfAllSections;
        String percentagePart = "(~" + percentageInDiagram + "%)";
        return "  " + ratioPart + "  " + percentagePart;
    }

    public String getGroupedByValueString() {
        return groupedByValue.toString();
    }
}
