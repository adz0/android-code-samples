package code.examples.plate;

import java.util.Date;

import code.examples.elements.IFamily;
import code.examples.elements.IPlateElem;
import code.examples.interfaces.TypeMeal;


public interface IPresenterPlate {

    void setView(IViewPlate viewPlate);

    void onInitialize();
    void onAddElem(IPlateElem plateElem);
    void onDelElem(IPlateElem plateElem);
    void onSetActiveElem(IPlateElem plateElem);
    void onEditElemEnable();

    // for check and delete plate element on adapter
    boolean consistFamily(IFamily family);
    void onDelElem(IFamily family);
    
    void onSavePlate(Date date, TypeMeal typeMeal);
    void onLoadPlate(int idPlate);
    void onClearPlate();
}
