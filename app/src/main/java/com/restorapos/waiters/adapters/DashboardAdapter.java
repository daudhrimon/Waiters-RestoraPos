package com.restorapos.waiters.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.FoodItemActivity;
import com.restorapos.waiters.model.dashboardModel.DashboardDatum;
import com.bumptech.glide.Glide;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private List<DashboardDatum> items;
    private Context context;

    //SpotsDialog progressDialog;
    public DashboardAdapter(Context applicationContext, List<DashboardDatum> itemArrayList) {
        this.context = applicationContext;
        this.items = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_dashboard_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.categoryName.setText(items.get(i).getName());

        String url = items.get(i).getCategoryimage();
        if (url != null) {
            Glide.with(context).load(url).into(viewHolder.categoryImage);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;
        LinearLayout categoryLay;

        public ViewHolder(View view) {
            super(view);
            categoryImage = view.findViewById(R.id.categoryImage);
            categoryName = view.findViewById(R.id.categoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, FoodItemActivity.class);
                    intent.putExtra("CATEGORYID", items.get(pos).getCategoryID());
                    intent.putExtra("CATEGORYNAME", items.get(pos).getName());
                    intent.putExtra("CATEGORYIMAGE", items.get(pos).getCategoryimage());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    //Toast.makeText(context, items.get(pos).getName(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}

