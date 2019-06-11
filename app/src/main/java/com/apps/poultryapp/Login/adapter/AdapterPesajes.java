package com.apps.poultryapp.Login.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.poultryapp.Login.InsertPesaje;
import com.apps.poultryapp.Login.Provider.DataLocalHelper;
import com.apps.poultryapp.R;

public class AdapterPesajes extends RecyclerView.Adapter<AdapterPesajes.ExpenseViewHolder> {
    private Cursor cursor;
    private Context context;

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nameCorral;
        public TextView namePesaje;
        public TextView edad;
        public TextView fecha;



        public ExpenseViewHolder(View v) {
            super(v);
            nameCorral = (TextView) v.findViewById(R.id.name_corral);
            namePesaje = (TextView) v.findViewById(R.id.code_pesaje);
            edad =       (TextView) v.findViewById(R.id.age_pesaje);
            fecha =      (TextView) v.findViewById(R.id.date_text_galpon);


        }
    }

    public AdapterPesajes(Context context) {
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
                .inflate(R.layout.card_pesajes, viewGroup, false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder viewHolder, final int i) {
        cursor.moveToPosition(i);

        String nameCorral ="";
        String namepesaje;
        String edad;
        String fecha;
        DataLocalHelper dataLocalHelper = new DataLocalHelper(context);
        SQLiteDatabase sql = dataLocalHelper.getWritableDatabase();
        System.out.println(cursor.getString(5));
        Cursor c = sql.rawQuery("SELECT name FROM corrals  WHERE idRemota = '"+cursor.getString(5)+"' ",null);
        try {
            if (c.moveToFirst()){
                System.out.println("ene el metodo de consulta");
                nameCorral = c.getString(0);
                System.out.println(c.getString(0));
            }
        }catch (Exception e){
            System.out.println(e);
        }


        namepesaje =  cursor.getString(1);
        edad =  cursor.getString(3);
        fecha = cursor.getString(7);



        viewHolder.nameCorral.setText(nameCorral);
        viewHolder.namePesaje.setText(namepesaje);
        viewHolder.edad.setText(edad);
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
