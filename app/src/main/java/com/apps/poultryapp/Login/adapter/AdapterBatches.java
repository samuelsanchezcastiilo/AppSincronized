package com.apps.poultryapp.Login.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.poultryapp.Login.DetallesItems;
import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.R;

public class AdapterBatches extends RecyclerView.Adapter<AdapterBatches.ExpenseViewHolder> {
    private Cursor cursor;
    private Context context;


    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView name;
        public TextView fecha;
        public ImageButton viewData;
        //public CheckBox checkBox;



        public ExpenseViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.text_data_code);
            fecha = (TextView) v.findViewById(R.id.date_text);
            viewData = (ImageButton)v.findViewById(R.id.view_data);
           // checkBox = (CheckBox)v.findViewById(R.id.selected);
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
    public void onBindViewHolder(final ExpenseViewHolder viewHolder, final int i) {
        cursor.moveToPosition(i);

        String name;
        String fecha;
        final String idRemota;

        name =  cursor.getString(1);
        fecha = cursor.getString(4);
        idRemota = cursor.getString(6);
        System.out.println("id--------------------id----"+cursor.getString(6));

        viewHolder.name.setText(name);
        viewHolder.fecha.setText(fecha);
        viewHolder.viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetallesItems.class);
                intent.putExtra("origen","1");
                intent.putExtra("loteID",cursor.getInt(0));
                intent.putExtra("remota",idRemota);
                System.out.println("id" + cursor.getInt(0));
                System.out.println("remota" + idRemota);

                context.startActivity(intent);
            }
        });

       /* viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewHolder.noCheck();
                viewHolder.checkBox.setChecked(true);
                SessionPref.get(context).saveLote(cursor.getString(1));
                return true;
            }
        });*/





    }


    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
