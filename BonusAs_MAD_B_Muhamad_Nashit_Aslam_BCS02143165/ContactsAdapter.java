package com.example.dell.bonusassignment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dell on 12/26/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {

    ArrayList<Contacts> list;
    Context context;

    public ContactsAdapter(Context context, ArrayList<Contacts> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.phone_item_view_layout, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.number.setText(list.get(position).getNumber() + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class VH extends RecyclerView.ViewHolder {

        TextView name, number;

        public VH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            number = itemView.findViewById(R.id.txtNumber);
        }
    }
}
