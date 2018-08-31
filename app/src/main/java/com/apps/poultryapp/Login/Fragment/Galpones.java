package com.apps.poultryapp.Login.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.Login.Provider.ContratosData;
import com.apps.poultryapp.Login.Sync.SyncAdapter;
import com.apps.poultryapp.Login.adapter.AdapterWarehouse;
import com.apps.poultryapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Galpones extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdapterWarehouse adapterWarehouse;
    String lot;



    public Galpones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_galpones2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_galpones);
        layoutManager =  new LinearLayoutManager(getContext());
        adapterWarehouse = new AdapterWarehouse(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterWarehouse);
        getLoaderManager().initLoader(0,null,this);
        SyncAdapter.inicializarSyncAdapter(getContext());
        String lote = SessionPref.get(getContext()).getLote();
         lot = "191";
        System.out.println("------------------------------");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void Dataload(){
        adapterWarehouse = new AdapterWarehouse(getContext());
        recyclerView = new RecyclerView(getContext());
        if (adapterWarehouse != null){
            recyclerView.setAdapter(adapterWarehouse);

        }else {
            System.out.println("es nullll");
        }

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {


        CursorLoader cursorLoader = new  CursorLoader(getContext(), ContratosData.CONTENT_URI_WAREHOUSE,null,null,null,null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    adapterWarehouse.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapterWarehouse.swapCursor(null);

    }
}
