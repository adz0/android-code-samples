package code.examples.plate;

import code.examples.elements.IPlateElem;


public interface IViewPlate {

    void addElem(IPlateElem IPlateElem);
    void delElem(IPlateElem IPlateElem);
    void editElem(IPlateElem IPlateElem);
    void setActiveElem(IPlateElem IPlateElem);

    void clearPlate();
}
