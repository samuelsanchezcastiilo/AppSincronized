package com.apps.poultryapp.Login.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.poultryapp.R;

public class AdapterCorrals  extends RecyclerView.Adapter<AdapterCorrals.ExpenseViewHolder> {

    private Cursor cursor;
    private Context context;

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_lotes, viewGroup, false);
        return new AdapterCorrals.ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int i) {
        cursor.moveToPosition(i);
        String lote;
        String galpon;
        String fecha;
        String corral;

        lote = cursor.getString(1);
        galpon = cursor.getString(2);
        fecha = cursor.getString(3);
        corral = cursor.getString(4);
        holder.lote.setText(lote);
        holder.corral.setText(corral);
        holder.fecha.setText(fecha);
        holder.galpon.setText(galpon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        private TextView lote;
        private TextView galpon;
        private TextView fecha;
        private TextView corral;

        public ExpenseViewHolder(View v) {
            super(v);
            lote = v.findViewById(R.id.text_code_galpon);
            galpon  = v.findViewById(R.id.code_galpon);
            fecha  = v.findViewById(R.id.date_text_galpon);
            corral  = v.findViewById(R.id.code_corral);
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
