package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.FoodActivity;
import com.restorapos.waiters.databinding.DesignDashboardItemBinding;
import com.restorapos.waiters.model.dashboardModel.DashboardDatum;
import com.bumptech.glide.Glide;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private final List<DashboardDatum> items;
    private final Context context;

    //SpotsDialog progressDialog;
    public DashboardAdapter(Context context, List<DashboardDatum> itemArrayList) {
        this.context = context;
        this.items = itemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_dashboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int pos) {
        holder.binding.categoryName.setText(items.get(pos).getName());

        String url = items.get(pos).getCategoryimage();
        if (url != null) {
            Glide.with(context).load(url).into(holder.binding.categoryImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodActivity.class);
                intent.putExtra("CATEGORYID", items.get(pos).getCategoryID());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final DesignDashboardItemBinding binding;
        public ViewHolder(View view) {
            super(view);
            binding = DesignDashboardItemBinding.bind(view);
        }
    }
}

