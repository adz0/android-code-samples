package code.examples.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import code.examples.R;
import code.examples.dao.DAOFacade;
import code.examples.dialog.DialogNumberPickerWeight;
import code.examples.dialog.DialogPlateSave;
import code.examples.elements.IGroupElement;
import code.examples.elements.INutrientReport;
import code.examples.elements.IPlateElem;
import code.examples.elements.ItemChartOfRelations;
import code.examples.elements.ItemPieChart;
import code.examples.factories.ElementsFabric;
import code.examples.interfaces.TypeMeal;
import code.examples.interfaces.TypeNutrient;
import code.examples.plate.IPresenterPlate;
import code.examples.plate.IViewPlate;
import code.examples.widgets.IPresenterWidget;
import code.examples.widgets.IViewWidget;
import code.examples.widgets.PlateRoundViewGroup;
import code.examples.widgets.WidgetCalorieMan;
import code.examples.widgets.WidgetChartOfRelationsView;
import code.examples.widgets.WidgetChartOfRelationsViewGroup;
import code.examples.widgets.WidgetChartWeightChangeForPeriodView;
import code.examples.widgets.WidgetChartWeightChangeForPeriodViewGroup;
import code.examples.widgets.WidgetCircleView;
import code.examples.widgets.WidgetFlowerVMGroup;
import code.examples.widgets.WidgetProgressBarVertical;


public class PlateFragment extends Fragment
implements IViewPlate
        , IViewWidget
{

    private IPresenterPlate presenterPlate;
    private IPresenterWidget presenterWidgetPFC;

    private ArrayAdapter<IPlateElem> plateElemAdapter;
    private List<IPlateElem> listPlateElem;
    private IPlateElem activeElem;

    // View descendants
    private TextView textView;
    private TextView widgetPFCtext;
    private PlateRoundViewGroup plateImageView;
    // analytics widgets
    private WidgetCircleView rounderProtein, rounderCarbohydrate, rounderLipid;
    private WidgetCalorieMan widgetCalorieMan;


    private OnFragmentInteractionListener mListener;

    public PlateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlateFragment.
     */
    public static PlateFragment newInstance()
    {
        PlateFragment fragment = new PlateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listPlateElem = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_plate, container, false);
        
		textView = (TextView) currentView.findViewById(R.id.txt_plate_active_elem);
		
        plateImageView = currentView.findViewById(R.id.plate_image_view);
        plateImageView.setOnCurrentItemChoicedListener(new PlateRoundViewGroup.OnCurrentItemChangedListener() {
            @Override
            public void onCurrentItemEdit(int currentItem) {
                presenterPlate.onSetActiveElem(listPlateElem.get(currentItem));
                mListener.onFragmentInteraction(Uri.fromParts("plate","edit enabled","plate fragment"));
            }

            @Override
            public void onCurrentItemActive(int currentItem) {
                presenterPlate.onSetActiveElem(listPlateElem.get(currentItem));
            }
        });

        return currentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenterPlate.onInitialize();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        presenterPlate = ((ActivityPlate)getActivity()).getPresenterPlate();
        presenterPlate.setView(this);

        presenterWidgetPFC = ((ActivityPlate)getActivity()).getPresenterWidgetPFC();
        presenterWidgetPFC.setView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //region IViewPlate
    @Override
    public void clearPlate() {
        listPlateElem.clear();
        plateImageView.setElemList(listPlateElem, null);
    }

    @Override
    public void addElem(IPlateElem plateElem) {
        Log.d("IViewPlate", "Add plate element " + plateElem.getFamily().getName());
        listPlateElem.add(plateElem);

        plateImageView.setElemList(listPlateElem, plateElem);
    }

    @Override
    public void delElem(IPlateElem plateElem) {
        listPlateElem.remove(listPlateElem.lastIndexOf(plateElem));
    }


    @Override
    public void setActiveElem(IPlateElem plateElem) {
        activeElem = plateElem;
        textView.setText(plateElem.toString());
    }//endregion IViewPlate

    //region IViewWidget
    @Override
    public void setParam(Map<TypeNutrient, INutrientReport> paramList, boolean isActiveElement) {
        
		if(isActiveElement) {
            // pass parameter to widget, that show state of all elements

        }else {
            // pass parameter to widget, that show state of active element
        }

    }
    //endregion IViewWidget

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
