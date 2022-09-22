package com.restorapos.waiters.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.CartActivity;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.pendingOrderModel.DataItem;
import com.restorapos.waiters.offlineDb.DatabaseClient;
import com.restorapos.waiters.utils.SharedPref;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder> {
    private List<DataItem> items;
    private Context context;
    ViewInterface viewInterface;

    public PendingOrderAdapter(Context applicationContext, List<DataItem> itemArrayList,ViewInterface viewInterface) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.viewInterface = viewInterface;
        SharedPref.init(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_pending_order_item, viewGroup, false);
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
        LinearLayout action,edit;

        public ViewHolder(View view) {
            super(view);
            orderId = view.findViewById(R.id.orderId);
            customerName = view.findViewById(R.id.customerId);
            table = view.findViewById(R.id.tableTv);
            date = view.findViewById(R.id.dateId);
            amount = view.findViewById(R.id.amountId);
            action = view.findViewById(R.id.actionId);
            edit = view.findViewById(R.id.editId);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    viewInterface.viewOrder(items.get(pos).getOrderId());
                    //Toast.makeText(context, items.get(pos).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, CartActivity.class);
                    SharedPref.write("ORDERID", items.get(pos).getOrderId());
                    intent.putExtra("ORDERID", items.get(pos).getOrderId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    deleteTable();
                    context.startActivity(intent);
                    //Toast.makeText(context, items.get(pos).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteTable() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .taskDao()
                        .deleteFoodTable();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}

