package com.restorapos.waiters.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.model.viewOrderModel.IteminfoItem;
import com.restorapos.waiters.utils.SharedPref;
import java.util.List;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolder> {
    private List<IteminfoItem> items;
    private Context context;
    private String status;

    public ViewOrderAdapter(Context applicationContext, List<IteminfoItem> itemArrayList,String status) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.status = status;
        SharedPref.init(context);
    }

    public ViewOrderAdapter(Context applicationContext, List<IteminfoItem> itemArrayList) {
        this.context = applicationContext;
        this.items = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_food_view_order_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        if (items.get(i).getAddons()==1){
            viewHolder.productName.setText(items.get(i).getProductName());

            double x=0;
            for (int p=0;p<items.get(i).getAddonsinfo().size();p++){
                if (Integer.parseInt(items.get(i).getAddonsinfo().get(p).getAddOnQty())>0){
                    //viewHolder.addOnName.setText(items.get(i).getAddonsinfo().get(p).getAddonsName());
                    viewHolder.addOnName.append(items.get(i).getAddonsinfo().get(p).getAddonsName()+" X "+items.get(i).getAddonsinfo().get(p).getAddOnQty()+",\n");
                    x=x+Double.parseDouble(items.get(i).getAddonsinfo().get(p).getPrice())*Double.parseDouble(
                            items.get(i).getAddonsinfo().get(p).getAddOnQty());}
            }
        }
        else {
            viewHolder.productName.setText(items.get(i).getProductName());
        }
        if (items.get(i).getStatus().equals("Ready")){
            viewHolder.status.setBackgroundResource(R.drawable.selector_green_button);
        }

        viewHolder.status.setText(status);
        viewHolder.sizeTv.setText("Size: "+items.get(i).getVarientname());
        viewHolder.qty.setText("Quantity: "+String.valueOf(items.get(i).getItemqty()));
        try {
            double price = Double.parseDouble(items.get(i).getPrice()) ;
            double total = price * Double.parseDouble(items.get(i).getItemqty());
            viewHolder.totalPrice.setText("Total Price: " +total+SharedPref.read("CURRENCY", ""));
        }
        catch (Exception e){
            viewHolder.totalPrice.setText("Total Price: "+"0"+SharedPref.read("CURRENCY", ""));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, qty, totalPrice, sizeTv,status,addOnName;
        public ViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productNameId);
            qty = view.findViewById(R.id.quantityId);
            totalPrice = view.findViewById(R.id.totalPriceId);
            sizeTv = view.findViewById(R.id.sizeId);
            addOnName = view.findViewById(R.id.addOnNameId);
            status = view.findViewById(R.id.status);

        }
    }
}

