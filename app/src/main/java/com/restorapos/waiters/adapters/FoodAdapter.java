package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignFoodsItemBinding;
import com.restorapos.waiters.interfaces.FoodDialogInterface;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.utils.SharedPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private final List<Foodinfo> foodItems;
    private final Context context;
    private final FoodDialogInterface foodDialogInterface;


    public FoodAdapter(Context context, List<Foodinfo> foodItems, FoodDialogInterface foodDialogInterface) {
        SharedPref.init(context);
        this.context = context;
        this.foodItems = foodItems;
        this.foodDialogInterface = foodDialogInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_foods_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {

        String url = foodItems.get(pos).getProductImage();
        if (url != null) {
            Glide.with(context).load(url).into(holder.fBinding.categoryImage);
        }

        holder.fBinding.categoryName.setText(foodItems.get(pos).getProductName());
        holder.fBinding.price.setText(SharedPref.read("CURRENCY", "") + " " + foodItems.get(pos).getPrice());
        holder.fBinding.notes.setText(foodItems.get(pos).getDestcription());
        holder.fBinding.varient.setText(foodItems.get(pos).getVariantName());

        if (foodItems.get(pos).getOfferIsavailable() != null) {
            if (foodItems.get(pos).getOfferIsavailable().equals("1") && isOfferAvailable2(foodItems.get(pos).getOfferstartdate(), foodItems.get(pos).getOfferendate())) {
                holder.fBinding.offerLay.setVisibility(View.VISIBLE);
                holder.fBinding.offerRate.setText(foodItems.get(pos).getOffersRate());
            }
        }


        holder.itemView.setOnClickListener(v -> {

            if (foodItems.get(pos).getPrice().equals("") || foodItems.get(pos).getPrice().equals("0") || foodItems.get(pos).getPrice().equals("0.0")) {

                Toasty.warning(context, "You can't add this Food to Cart", Toasty.LENGTH_SHORT, true).show();
            } else {
                if (holder.fBinding.selectedMark.getVisibility() == View.VISIBLE) {
                    holder.fBinding.selectedMark.setVisibility(View.GONE);
                } else {
                    holder.fBinding.selectedMark.setVisibility(View.VISIBLE);
                    foodDialogInterface.onFoodItemClick(context, foodItems.get(pos), holder.fBinding.selectedMark);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final DesignFoodsItemBinding fBinding;
        public ViewHolder(View view) {
            super(view);
            fBinding = DesignFoodsItemBinding.bind(view);
        }
    }


    private boolean isOfferAvailable2(String offerstartdate, String offerendate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        Date endDate = null;
        try {
            strDate = sdf.parse(offerstartdate);
            endDate = sdf.parse(offerendate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() >= strDate.getTime() &&
                System.currentTimeMillis() <= endDate.getTime()) {
            Log.wtf("TRUE", "");
            return true;
        } else {
            Log.wtf("False", "");
            return false;
        }
    }
}
