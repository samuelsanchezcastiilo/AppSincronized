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
import android.widget.Toast;

import com.apps.poultryapp.Login.DetallesItems;
import com.apps.poultryapp.Login.InsertPesaje;
import com.apps.poultryapp.Login.Provider.DataLocalHelper;
import com.apps.poultryapp.R;

public class AdapterCorrals  extends RecyclerView.Adapter<AdapterCorrals.ExpenseViewHolder> {

    private Cursor cursor;
    private Context context;
    String galponName;

    public AdapterCorrals(Context context) {
        this.context = context;
    }

    @NonNull
    @Override

    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_corrales, viewGroup, false);
        return new AdapterCorrals.ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, final int i) {
        cursor.moveToPosition(i);
        DataLocalHelper dataLocalHelper = new DataLocalHelper(context);
        SQLiteDatabase sql = dataLocalHelper.getWritableDatabase();
        String id = cursor.getString(2);
        Cursor c = sql.rawQuery("SELECT name FROM warehouse  WHERE idRemota = '"+id+"' ",null);
        try {
            if (c.moveToFirst()){
                System.out.println("ene el metodo de consulta");
                galponName = c.getString(0);
                System.out.println(c.getString(0));
            }
        }catch (Exception e){
            System.out.println(e);
        }



        String galpon;
        String fecha;
        String corral;
        String age;
        final String idRemota ;

        galpon = galponName;
        fecha = cursor.getString(5);
        age = cursor.getString(3);
        corral = cursor.getString(1);
        idRemota = cursor.getString(7);

        holder.corral.setText(corral);
        holder.fecha.setText(fecha);
        holder.galpon.setText(galpon);
        holder.age.setText(age);
        holder.addDAta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InsertPesaje.class);
                intent.putExtra("corral",idRemota);
                context.startActivity(intent);
            }
        });
        holder.viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetallesItems.class);
                intent.putExtra("origen","3");
                intent.putExtra("idRemotaC",idRemota);
                context.startActivity(intent);
            }
        });





    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{

        private TextView age;
        private TextView galpon;
        private TextView fecha;
        private TextView corral;
        private ImageButton viewData;
        private ImageButton addDAta;

        public ExpenseViewHolder(View v) {
            super(v);
            galpon  = v.findViewById(R.id.code_galpon);
            fecha  = v.findViewById(R.id.date_text_galpon);
            corral  = v.findViewById(R.id.code_corral);
            age = v.findViewById(R.id.age_galpon);
            viewData = v.findViewById(R.id.view_data);
            addDAta = v.findViewById(R.id.add_pesaje);
        }
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
