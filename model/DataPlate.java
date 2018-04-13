package code.examples.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import code.examples.elements.IIngredient;
import code.examples.elements.IPlate;
import code.examples.elements.IPlateElem;
import code.examples.elements.IWeight;
import code.examples.factories.ElementsFabric;
import code.examples.interfaces.IDAO;
import code.examples.interfaces.IDataAnalyze;
import code.examples.interfaces.IDataDiary;
import code.examples.interfaces.IDataPlate;
import code.examples.interfaces.IPlateListener;
import code.examples.interfaces.PlateOperation;
import code.examples.interfaces.TypeMeal;

import static code.examples.interfaces.PlateOperation.*;


public class DataPlate implements IDataPlate, IPlateListener {

    private IPlate plate;
    private List<IPlateElem> plateElementList = new ArrayList<>();
    private IPlateElem activePlateElem;

    private IDataAnalyze dataAnalyze;
    private IDataDiary dataDiary;

    @Inject public DataPlate(IDataAnalyze dataAnalyze, IDataDiary dataDiary){
        this.dataAnalyze = dataAnalyze;
        this.dataDiary = dataDiary;

        plate = ElementsFabric.getPlate();
        dataDiary.setPlateListener(this);
    }

    @Override
    public boolean addElement(IPlateElem plateElem) {
        if(plateElementList.add(plateElem)) {
            setActiveElement(plateElem, ADD);
            return true;
        }else
            return false;
    }

    @Override
    public boolean delElement(IPlateElem plateElem) {
        if(plateElementList.remove(plateElem)) {

            if(plateElementList.size()>0){
                setActiveElement(plateElementList.get(0), SET_ACTIVE);
            }else{
                setActiveElement(null, SET_ACTIVE);
            }

            return true;
        }else
            return false;
    }

    @Override
    public void replaceElement(IIngredient newIngredientElem)
    {
        activePlateElem.setIngreDish(newIngredientElem);
        setActiveElement(activePlateElem, REPLACE);
    }

    @Override
    public List<IPlateElem> getPlateElements() {
        return plateElementList;
    }
    public IPlateElem getActivePlateElem() {
        return activePlateElem;
    }

    @Override
    public void savePlate(Date date, TypeMeal typeMeal) {
        plate.setPlateElements(getPlateElements());
        plate.setDate(date);
        plate.setTypeMeal(typeMeal);
        dataDiary.savePlate(plate);
    }

    @Override
    public void loadPlate(int idPlate) {
        dataDiary.getPlate(idPlate);
    }

    @Override
    public void clearPlate() {
        plateElementList.clear();
        plate.setPlateElements(plateElementList);
    }

    @Override
    public void setActiveElement(IPlateElem plateElem, PlateOperation operation) {
        activePlateElem = plateElem;

        plate.setPlateElements(getPlateElements());
        dataAnalyze.setPlate(plate);
        dataAnalyze.setPlateElem(plateElem);
    }


    @Override
    public void editBaseWeight(float weight) {
        activePlateElem.setWeightInBaseType(weight);
        setActiveElement(activePlateElem, EDIT_WEIGHT);
    }

    @Override
    public void editAlternativeWeightType(IWeight weightType) {
        activePlateElem.setAlternativeWeightType(weightType);
    }

    @Override
    public void onLoadPlate(IPlate plate) {
        if(plate!=null) {
            this.plate = plate;
        }
    }
}
