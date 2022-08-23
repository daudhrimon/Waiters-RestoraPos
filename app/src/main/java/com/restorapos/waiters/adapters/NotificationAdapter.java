package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.model.notificationModel.OrderinfoItem;
import com.restorapos.waiters.utils.NotificationInterface;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<OrderinfoItem> items;
    private Context context;
    NotificationInterface notificationInterface;

    public NotificationAdapter(Context applicationContext, List<OrderinfoItem> itemArrayList, NotificationInterface notificationInterface) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.notificationInterface = notificationInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.design_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.orderNo.setText(items.get(i).getOrderid());
        viewHolder.custName.setText(items.get(i).getCustomer());
        viewHolder.amount.setText(items.get(i).getAmount());
        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationInterface.acceptOrder(items.get(i).getOrderid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderNo)
        TextView orderNo;
        @BindView(R.id.custName)
        TextView custName;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.accept)
        TextView accept;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

