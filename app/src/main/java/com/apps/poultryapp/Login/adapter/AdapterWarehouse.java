package com.apps.poultryapp.Login.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.poultryapp.Login.DetallesItems;
import com.apps.poultryapp.Login.Provider.DataLocalHelper;
import com.apps.poultryapp.R;

public class AdapterWarehouse extends RecyclerView.Adapter<AdapterWarehouse.ExpenseViewHolder> {
    private Cursor cursor;
    private Context context;
    String lote ;

    public static  class ExpenseViewHolder extends RecyclerView.ViewHolder{

        public TextView lote;
        public TextView galpon;
        public TextView fecha;
        public ImageButton viewData;

        public ExpenseViewHolder(View v) {
            super(v);
            lote = v.findViewById(R.id.text_data_code);
            galpon = v.findViewById(R.id.code_galpon);
            fecha = v.findViewById(R.id.date_text_galpon);
            viewData = v.findViewById(R.id.view_data);
        }
    }

    public AdapterWarehouse(Context context) {
        this.context = context;
    }


    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }


    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_galpones,parent,false);

        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        cursor.moveToPosition(position);

        DataLocalHelper dataLocalHelper = new DataLocalHelper(context);
        SQLiteDatabase sql = dataLocalHelper.getWritableDatabase();

       String id = cursor.getString(2);
       // System.out.println("antes de la consulta" + id);
        Cursor c = sql.rawQuery("SELECT name FROM batches  WHERE idRemota = '"+id+"' ",null);

        try {
            if (c.moveToFirst()){
                System.out.println("ene el metodo de consulta");
                lote = c.getString(0);
                System.out.println("-----" + lote);
                System.out.println(c.getString(0));
            }
        }catch (Exception e){
            System.out.println(e);
        }


        final String idRemota;
        String galpon;
        String fecha;

        galpon = cursor.getString(1);
        fecha = cursor.getString(5);
        idRemota = cursor.getString(6);

        holder.lote.setText(lote);
        holder.fecha.setText(fecha);
        holder.galpon.setText(galpon);
        holder.viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetallesItems.class);
                intent.putExtra("origen","2");
                intent.putExtra("idRemota",idRemota);
                context.startActivity(intent);
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
