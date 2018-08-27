package com.apps.poultryapp.Login.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.poultryapp.R;

public class AdapterBatches extends RecyclerView.Adapter<AdapterBatches.ExpenseViewHolder> {
    private Cursor cursor;
    private Context context;

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView name;
        public TextView fecha;



        public ExpenseViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.text_data_code);
            fecha = (TextView) v.findViewById(R.id.date_text);


        }
    }

    public AdapterBatches(Context context) {
        this.context= context;

    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_lotes, viewGroup, false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String name;
        String fecha;

        name =  cursor.getString(1);
        fecha = cursor.getString(4);



        viewHolder.name.setText(name);
        viewHolder.fecha.setText(fecha);
        //viewHolder.fecha.setText(fecha);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"se presiono",Toast.LENGTH_SHORT ).show();
            }
        });
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
