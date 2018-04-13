package code.examples.plate;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import code.examples.elements.IFamily;
import code.examples.elements.IPlate;
import code.examples.elements.IPlateElem;
import code.examples.interfaces.IDataPlate;
import code.examples.interfaces.PlateOperation;
import code.examples.interfaces.TypeMeal;


public class PlatePresenter implements IPresenterPlate, Observer {

    private IDataPlate dataPlate;
    private IViewPlate viewPlate;

    @Inject
    public PlatePresenter(IDataPlate dataPlate){
        this.dataPlate = dataPlate;

        if(this.dataPlate instanceof Observable) {
            ((Observable) this.dataPlate).addObserver(this);
        }
    }


    @Override
    public void setView(IViewPlate viewPlate){
        this.viewPlate = viewPlate;
    }

    @Override
    public void onInitialize() {
        for (IPlateElem plateElem : dataPlate.getPlateElements()) {
            viewPlate.addElem(plateElem);
        }
    }

    @Override
    public void onAddElem(IPlateElem IPlateElem) {
        dataPlate.addElement(IPlateElem);
    }

    @Override
    public void onDelElem(IPlateElem IPlateElem) {
        dataPlate.delElement(IPlateElem);
    }

    @Override
    public void onDelElem(IFamily family) {
        for (IPlateElem plateElem : dataPlate.getPlateElements()){
            if(plateElem.getFamily().equals(family)){
                dataPlate.delElement(plateElem);
                return;
            }
        }
    }

    @Override
    public void onSavePlate(Date date, TypeMeal typeMeal) {
        dataPlate.savePlate(date, typeMeal);
    }

    @Override
    public void onLoadPlate(int idPlate) {
        dataPlate.loadPlate(idPlate);
    }

    @Override
    public void onClearPlate() {
        dataPlate.clearPlate();
        viewPlate.clearPlate();
    }

    @Override
    public void onSetActiveElem(IPlateElem plateElem) {
        dataPlate.setActiveElement(plateElem, PlateOperation.SET_ACTIVE);
    }

    @Override
    public void onEditElemEnable() {
        dataPlate.setActiveElement(dataPlate.getActivePlateElem(), PlateOperation.SET_ACTIVE);
    }

    @Override
    public boolean consistFamily(IFamily family) {

        for(IPlateElem plateElem : dataPlate.getPlateElements()){
            if(plateElem.getFamily().equals(family))
                return true;
        }
        return false;
    }

    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof IDataPlate){
            
            if(o instanceof Object[])
            {
                PlateOperation operation = (PlateOperation)((Object[])o)[1];
                IPlateElem plateElem = (IPlateElem)((Object[])o)[0];
                if(plateElem==null)
                    return;

                if(operation==PlateOperation.SET_ACTIVE)
                    viewPlate.setActiveElem(plateElem);
                else if(operation==PlateOperation.ADD)
                {
                    viewPlate.addElem(plateElem);
                    viewPlate.setActiveElem(plateElem);
                }
                else if(operation==PlateOperation.DELETE)
                {
                    viewPlate.delElem(plateElem);
                }
                else if(operation==PlateOperation.REPLACE){
                    viewPlate.editElem(plateElem);
                }
            }else if(o instanceof IPlate){
                // load plate
                viewPlate.clearPlate();
                for (IPlateElem plateElem: ((IPlate)o).getPlateElements()) {
                    viewPlate.addElem(plateElem);
                }

            }
        }
    }
}
