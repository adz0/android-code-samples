package code.examples.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import code.examples.R;
import code.examples.elements.IFamily;
import code.examples.elements.IGroupElement;
import code.examples.elements.IPlateElem;
import code.examples.factories.ElementsFabric;
import code.examples.plate.IPresenterPlate;


public class ElementsFamilyAdapter extends RecyclerView.Adapter<ElementsFamilyAdapter.ViewHolder> {

    private List<IFamily> elementList;
    private IPresenterPlate presenterPlate;
    private IGroupElement groupElement;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public ViewHolder(View view){
            super(view);
            context = view.getContext();
            this.textView =(TextView) view.findViewById(R.id.id_family_element_item);

            this.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    IFamily currentFamilyElem = elementList.get(getAdapterPosition());
                    IPlateElem plateElem = ElementsFabric.getPlateElement(
                            currentFamilyElem, currentFamilyElem.getMember(), groupElement);
                    presenterPlate.onAddElem(plateElem);
                    onBindViewHolder(ViewHolder.this,getAdapterPosition());

                }
            });

        }
    }


    public ElementsFamilyAdapter(IPresenterPlate presenterPlate, List<IFamily> elementList, IGroupElement groupElement){
        this.presenterPlate = presenterPlate;
        this.elementList = elementList;
        this.groupElement = groupElement;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(elementList.get(position).getName());
        String iconName = elementList.get(position).getIconName();
        int idRes = context.getResources().getIdentifier(iconName,"drawable", context.getPackageName());
        if(idRes!=0){
            float density = context.getResources().getDisplayMetrics().density;
            Drawable icon = ResourcesCompat.getDrawable(context.getResources(), idRes, null);
            Drawable check=null;
            if(icon!=null) {

                int w = context.getResources().getDimensionPixelSize(R.dimen.item_family_height);
                icon.setBounds(0, 0, w, w);

                if(presenterPlate.consistFamily(elementList.get(position))) {
                    check = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_check_edit_elem, null);
                    int wCheck = 24*(int)density;
                    if(check!=null)
                        check.setBounds(0,0,wCheck,wCheck);
                }

                holder.textView.setCompoundDrawables(
                        icon
                        , null
                        , check, null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return elementList.size();
    }

}
