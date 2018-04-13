package code.examples.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import code.examples.R;
import code.examples.ui.ActivityPlate;


public class DialogNumberPickerWeight extends DialogFragment {

    public int weightValue;
    private OnFragmentInteractionListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mListener = (ActivityPlate)getActivity();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_number_picker_weight,null);
        NumberPicker num100 = dialogView.findViewById(R.id.numberPicker_hundreds);
        NumberPicker num10  = dialogView.findViewById(R.id.numberPicker_tens);
        NumberPicker num1   = dialogView.findViewById(R.id.numberPicker_one);
        num100.setMaxValue(9); num100.setMinValue(0);
        num10.setMaxValue(9); num10.setMinValue(0);
        num1.setMaxValue(9); num1.setMinValue(0);

        Bundle args = getArguments();
        weightValue = args.getInt("weight", 100);
        num1.setValue(weightValue % 10); weightValue/=10;
        num10.setValue(weightValue% 10); weightValue/=10;
        num100.setValue(weightValue % 10);

        
        builder.setTitle(getResources().getString(R.string.dialog_edit_weight))
                .setView(dialogView)
                .setNegativeButton(getResources().getString(R.string.dialog_calcel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(getResources().getString(R.string.dialog_set), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mListener!=null) {
                            String strWeight = String.valueOf(
                                    num100.getValue()) +
                                    num10.getValue() +
                                    num1.getValue();
                            mListener.onFragmentInteraction(
                                    Uri.fromParts("edit", "edit weight on picker", strWeight));
                        }
                    }
                });
        return builder.create();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
