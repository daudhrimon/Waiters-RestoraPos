package com.restorapos.waiters.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.completeCancelOrder.OrderinfoItem;
import com.restorapos.waiters.utils.SharedPref;
import java.util.List;

public class CompleteCancelOrderAdapter extends RecyclerView.Adapter<CompleteCancelOrderAdapter.ViewHolder> {
    private List<OrderinfoItem> items;
    private Context context;
    private ViewInterface viewInterface;
    public CompleteCancelOrderAdapter(Context applicationContext, List<OrderinfoItem> itemArrayList,ViewInterface viewInterface) {
        this.context = applicationContext;
        this.items = itemArrayList;
        SharedPref.init(context);
        this.viewInterface = viewInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_complete_cancel_order_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.orderId.setText(items.get(i).getOrderId());
        if (items.get(i).getCustomerName().length() == 1) {
            viewHolder.customerName.setText("0000" + items.get(i).getCustomerName());
        } else if (items.get(i).getCustomerName().length() == 2) {
            viewHolder.customerName.setText("000" + items.get(i).getCustomerName());
        } else if (items.get(i).getCustomerName().length() == 3) {
            viewHolder.customerName.setText("00" + items.get(i).getCustomerName());
        } else if (items.get(i).getCustomerName().length() == 4) {
            viewHolder.customerName.setText("0" + items.get(i).getCustomerName());
        } else {
            viewHolder.customerName.setText(items.get(i).getCustomerName());
        }
        viewHolder.table.setText(items.get(i).getTableName());
        viewHolder.date.setText(items.get(i).getOrderDate());
        viewHolder.amount.setText(SharedPref.read("CURRENCY", "") + items.get(i).getTotalAmount());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, customerName, table, date, amount;
        LinearLayout action;

        public ViewHolder(View view) {
            super(view);
            orderId = view.findViewById(R.id.orderId);
            customerName = view.findViewById(R.id.customerId);
            table = view.findViewById(R.id.tableTv);
            date = view.findViewById(R.id.dateId);
            amount = view.findViewById(R.id.amountId);
            action = view.findViewById(R.id.actionId);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    viewInterface.viewOrder(items.get(pos).getOrderId());
                }
            });
        }
    }
}

