package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.interfaces.SumInterface;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.utils.SharedPref;
import java.text.DecimalFormat;
import java.util.List;

public class FoodCartsAdaptersNew extends RecyclerView.Adapter<FoodCartsAdaptersNew.ViewHolder> {

    private List<Foodinfo> items;
    private Context context;
    double total = 0.0;
    SumInterface sumInterface;

    public FoodCartsAdaptersNew(Context applicationContext, List<Foodinfo> itemArrayList, SumInterface sumInterface) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.sumInterface = sumInterface;
        SharedPref.init(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_food_cart_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "RecyclerView"})
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Log.d("ASDas", "onBindViewHolder: ");
        if (items.get(i).getAddOnsName() == null) {
            viewHolder.productName.setText(items.get(i).getProductName());
        } else {
            viewHolder.productName.setText(items.get(i).getProductName());
            try {
                for (int j = 0; j < items.get(i).getAddonsinfo().size(); j++) {
                    try {
                        int p = items.get(i).getAddonsinfo().get(j).addonsquantity;
                        Log.d("poisdfsd", "onBindViewHolder: " + p);
                        if (p > 0) {
                            viewHolder.productName.append("," + items.get(i).getAddonsinfo().get(j).getAddOnName());
                        }
                    } catch (Exception ign) {
                        Log.d("poisdfsd", "onBindViewHolder: " + ign.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
            }
        }
        viewHolder.unitPrice.setText(SharedPref.read("CURRENCY", "") + items.get(i).getPrice());
        viewHolder.sizeTv.setText(items.get(i).getVariantName());
        viewHolder.qty.setText(String.valueOf(items.get(i).quantitys));
        viewHolder.varientId.setText(items.get(i).getVariantName());

        double x = 0;
        try {
            for (int p = 0; p < items.get(i).getAddonsinfo().size(); p++) {
                items.get(i).setAddons(1);
                Log.d("TAG", "onBindViewHolder: " + items.get(i).getAddonsinfo().get(p).getAddOnName());
            }
        } catch (Exception ignored) {
        }
        Log.d("OK", "onBindViewHolder: " + x);
        int counts = items.get(i).quantitys;
        items.get(i).setQuantity(counts);
        total = Double.parseDouble(items.get(i).getPrice()) * counts + Double.valueOf(items.get(i).getAddOnsTotal());
        viewHolder.totalPrice.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));
        viewHolder.itemNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                items.get(i).itemNote = String.valueOf(editable);
            }
        });
        viewHolder.notesShowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.itemNotes.getVisibility()==View.GONE){
                    viewHolder.notesShowTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_drop_up_24,0);
                    viewHolder.itemNotes.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.notesShowTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_drop_down_24,0);
                    viewHolder.itemNotes.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.plus.setOnClickListener(v -> {
            int count = Integer.parseInt(String.valueOf(viewHolder.qty.getText()));
            count++;
            viewHolder.qty.setText(String.valueOf(count));
            items.get(i).quantitys = count;
            items.get(i).setQuantity(count);
            total = Double.parseDouble(items.get(i).getPrice()) * count + Double.valueOf(items.get(i).getAddOnsTotal());
            viewHolder.totalPrice.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));

            sumInterface.addedSum(items.get(i));

        });
        viewHolder.minus.setOnClickListener(v -> {
            int count = Integer.parseInt(String.valueOf(viewHolder.qty.getText()));
            if (count > 0) {
                count--;
                viewHolder.qty.setText(String.valueOf(count));
                items.get(i).quantitys = count;
                items.get(i).setQuantity(count);
                total = Double.parseDouble(items.get(i).getPrice()) * count + Double.valueOf(items.get(i).getAddOnsTotal());
                if (items.get(i).quantitys == 0) {
                    total = total - Double.valueOf(items.get(i).getAddOnsTotal());
                }
                viewHolder.totalPrice.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));

                sumInterface.divideSum(items.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, unitPrice, qty, totalPrice, plus, minus, sizeTv,notesShowTv,varientId;
        EditText itemNotes;

        public ViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productNameId);
            itemNotes = view.findViewById(R.id.notesId);
            unitPrice = view.findViewById(R.id.unitPriceId);
            qty = view.findViewById(R.id.quantityId);
            totalPrice = view.findViewById(R.id.totalPriceId);
            plus = view.findViewById(R.id.plusId);
            minus = view.findViewById(R.id.minusId);
            sizeTv = view.findViewById(R.id.sizeId);
            notesShowTv = view.findViewById(R.id.notesShow);
            varientId = view.findViewById(R.id.varientNAME);

        }
    }
}

