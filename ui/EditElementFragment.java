package code.examples.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.transition.AutoTransition;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import code.examples.R;
import code.examples.adapter.EditElementAdapter;
import code.examples.adapter.ElementsWeightTypeAdapter;
import code.examples.dao.ConfigApp;
import code.examples.dialog.DialogNumberPickerWeight;
import code.examples.elements.IIngredient;
import code.examples.elements.IPlateElem;
import code.examples.elements.IWeight;
import code.examples.elements.IWeightRelationships;
import code.examples.factories.ElementsFabric;
import code.examples.plate.IPresenterEditElem;
import code.examples.plate.IPresenterEditWeight;
import code.examples.plate.IViewEditElem;
import code.examples.plate.IViewEditWeight;


public class EditElementFragment extends Fragment
implements IViewEditElem, IViewEditWeight
    , View.OnClickListener
    , SeekBar.OnSeekBarChangeListener
    , AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener

{

    private OnFragmentInteractionListener mListener;

    private IPresenterEditElem presenterEditElem;
    private IPresenterEditWeight presenterEditWeight;

    // image active group from list elements fragment
    private ImageView buttonBackGroup;

    private ListView familyElements;
    private List<IIngredient> listEditElements;
    private LinearLayout editElemLinear, editAllFragmentLinear;
    private ConstraintLayout editWeightLinear;

    // edit weight variables region
    private boolean editWeightOpen=false;
    private boolean editWeightMoveBegin;
    private List<IWeightRelationships> weightRelationshipsList;

    private TextView weightInBaseType;
    private TextView weightInAlternativeType, weightAlternativeTypeName;
    private IWeight weightType, weightAlternativeType;
    private int currentWeight;

    private SeekBar seekBar;
    private int deltaSeekBar;
    //1 - stop tracking; 2 - tracking; 3 - progress tracking
    private boolean progressSeekBarTrackingTouch;



    public EditElementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditElementFragment.
     */
    public static EditElementFragment newInstance() {
        EditElementFragment fragment = new EditElementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_edit_element_constr, container, false);

        // set height frame dependent of screen orientation
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.id_fragment_list_elements_frame_height);
        if(ConfigApp.getInstance().orientationScreen == Configuration.ORIENTATION_LANDSCAPE) {
            TypedValue tv = new TypedValue();
            if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                frameLayout.setMinimumHeight(
                        TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics())
                );

            }
        }
        else {
            frameLayout.setMinimumHeight(0);
        }

        editAllFragmentLinear = view.findViewById(R.id.linear_elements);
        editElemLinear = (LinearLayout)view.findViewById(R.id.edit_elem_linear);
        editWeightLinear = view.findViewById(R.id.edit_weight_linear);

        buttonBackGroup = (ImageView) view.findViewById(R.id.btn_edit_element_back_to_list);
        buttonBackGroup.setOnClickListener(this);

        familyElements = (ListView)view.findViewById(R.id.list_edit_elements);
        familyElements.setOnItemClickListener(this);

        View viewEditWeightOpen = view.findViewById(R.id.btn_edit_weight_open);
        viewEditWeightOpen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
				
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        editWeightMoveBegin=true;
                        break;
                    case MotionEvent.ACTION_UP:
                        editWeightMoveBegin=false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(editWeightMoveBegin){
                            if(editWeightOpen && motionEvent.getY(motionEvent.getPointerCount()-1)>40){
                                editWeightOpen=false;
                                editWeightMoveBegin=false;

                                Scene scene = new Scene(editAllFragmentLinear);
                                Transition transition = new AutoTransition();
                                TransitionManager.beginDelayedTransition(editAllFragmentLinear,transition);

                                LinearLayout.LayoutParams paramsWeight =
                                        (LinearLayout.LayoutParams)editWeightLinear.getLayoutParams();
                                paramsWeight.weight=1;
                                editWeightLinear.setLayoutParams(paramsWeight);

                                LinearLayout.LayoutParams paramsList =
                                        (LinearLayout.LayoutParams)editElemLinear.getLayoutParams();
                                paramsList.weight=3;
                                editElemLinear.setLayoutParams(paramsList);

                                transition.removeTarget(familyElements);
                                TransitionManager.go(scene);

                            }else if(!editWeightOpen && motionEvent.getY(motionEvent.getPointerCount()-1)<40){
                                editWeightOpen=true;
                                editWeightMoveBegin=false;

                                Scene scene = new Scene(editAllFragmentLinear);
                                Transition transition = new ChangeBounds();
                                TransitionManager.beginDelayedTransition(editAllFragmentLinear,transition);

                                LinearLayout.LayoutParams paramsWeight =
                                        (LinearLayout.LayoutParams)editWeightLinear.getLayoutParams();
                                paramsWeight.weight=3;
                                editWeightLinear.setLayoutParams(paramsWeight);

                                LinearLayout.LayoutParams paramsList =
                                        (LinearLayout.LayoutParams)editElemLinear.getLayoutParams();
                                paramsList.weight=1;
                                editElemLinear.setLayoutParams(paramsList);

                                TransitionManager.go(scene);

                            }
                        }

                    break;
                }
                return true;
            }
        });

        weightInBaseType = (TextView)view.findViewById(R.id.txt_weight_in_base_type);
        weightInAlternativeType = (TextView)view.findViewById(R.id.txt_weight_and_icon_alternative);

        weightInBaseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNumberPickerWeight dialogFragment = new DialogNumberPickerWeight();
                Bundle args = new Bundle();
                args.putInt("weight", currentWeight);
                dialogFragment.setArguments(args);
                dialogFragment.show(getFragmentManager(),"dialog weight");
            }
        });

        seekBar = (SeekBar)view.findViewById(R.id.edit_weight_seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        // for drawer panel don't slide while seekbar move
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenterEditElem.onInitialize();
        presenterEditWeight.onInitialize();
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

        presenterEditElem = ((ActivityPlate)getActivity()).getPresenterEditElem();
        presenterEditElem.setView(this);
        presenterEditWeight = ((ActivityPlate)getActivity()).getPresenterEditWeight();
        presenterEditWeight.setView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //region IViewEditElem implementation
    @Override
    public void updateList(IPlateElem activePlateElem, List<IPlateElem> plateElems) {

        if(activePlateElem==null){
            if(mListener!=null) {
                mListener.onFragmentInteraction(Uri.fromParts("edit", "edit disabled", "edit fragment"));
            }
            return;
        }

        EditElementAdapter editElementAdapter = new EditElementAdapter(this
                , activePlateElem
                , plateElems
        );
        familyElements.setAdapter(editElementAdapter);

        int indexChoosenElem=0;
        for(int i=0; i<activePlateElem.getFamily().getFamilyElements().size(); i++){
            if(activePlateElem.getFamily().getFamilyElements().get(i).equals(activePlateElem.getIngreDish())){
                indexChoosenElem=i;
                break;
            }
        }
        familyElements.setSelection(indexChoosenElem);
    }

    @Override
    public void setElemList(List<IIngredient> elemList) {
        listEditElements = elemList;
    }//endregion IViewEditElem

    //region IViewEditWeight implementation
    @Override
    public void changeWeight(float weightInBaseType, float weightInAlternativeType) {
        // set text view with weight in actual value
        currentWeight = (int)weightInBaseType;
        String baseWeightText = "" + weightInBaseType + " - " + weightType.getName();
        this.weightInBaseType.setText(baseWeightText);
        if(!weightAlternativeType.equals(weightType)) {
            this.weightInAlternativeType.setVisibility(TextView.VISIBLE);

            float altWeight=weightInBaseType;
            for(IWeightRelationships elem : weightRelationshipsList){
                if(elem.getWeightType().equals(weightAlternativeType)) {
                    altWeight/=elem.getKoef();
                    break;
                }
            }
            String altWeightText = String.format(Locale.getDefault(),"%.1f",altWeight);
            String iconNameAltWeight = weightAlternativeType.getIconName();
            this.weightInAlternativeType.setText(altWeightText);

            if(iconNameAltWeight!=null) {
                Context context = getContext();
                Drawable icon = ElementsFabric.getIconForElement(context, iconNameAltWeight);
                if(icon!=null) {
                    int w = context.getResources().getDimensionPixelSize(R.dimen.weight_alternative_icon_height);
                    icon.setBounds(0, 0, w, w);
                    this.weightInAlternativeType.setCompoundDrawables(icon, null, null, null);
                }

            }
        }
        else
            this.weightInAlternativeType.setVisibility(TextView.INVISIBLE);


        // set up seek bar position
        if(!progressSeekBarTrackingTouch) {
            if(weightInBaseType-50>0) // max always equal 100
                deltaSeekBar = Math.round(weightInBaseType-50);
            else
                deltaSeekBar=0;

            int progress;
            if(deltaSeekBar>0){
                // set progress on half way on
                progress = Math.round(weightInBaseType - deltaSeekBar);
            }else{
                progress = Math.round(weightInBaseType);
            }

            if(progress!=seekBar.getProgress()) {
                ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", progress);
                animation.setDuration(500); // 0.5 second
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            }
        }

    }

    @Override
    public void changeWeightType(IWeight weightType, IWeight weightAlternativeType) {
        // set weight text value in alternative weight type
        // ensure altenative weight equal parameter weightType

        this.weightType = weightType;
        this.weightAlternativeType = weightAlternativeType;

    }


    @Override
    public void setWeightTypes(List<IWeightRelationships> weightTypesList) {

        // set active weight type
        weightRelationshipsList = weightTypesList;

    }//endregion IViewEditWeight

    //region OnClickListener implementation
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_edit_element_back_to_list:
                if(mListener!=null){
                    mListener.onFragmentInteraction(Uri.fromParts("edit","edit disabled","edit fragment"));
                }
                break;
        }

    }//endregion

    //region OnItemClickListener implementation for family elements
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView checkedTextView = (CheckedTextView)view.findViewById(R.id.checked_text_elem);
        if(checkedTextView.isChecked()) {

            presenterEditElem.onDelElem();
        }
        else {
            presenterEditElem.onReplaceElem(listEditElements.get(i));
        }

    }//endregion OnItemClickListener implementation


    //region OnItemSelectedListener implementation for Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        presenterEditWeight.onChangeAlternativeWeightType(weightRelationshipsList.get(i).getWeightType());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }//endregion OnItemSelectedListener implementation


    //region OnSeekBarChangeListener implementation for SeekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        if(fromUser)
            presenterEditWeight.onChangeWeight(i+deltaSeekBar);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        progressSeekBarTrackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progressSeekBarTrackingTouch = false;
        presenterEditWeight.onChangeWeight(seekBar.getProgress()+deltaSeekBar);
    }//endregion OnSeekBarChangeListener implementation

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
