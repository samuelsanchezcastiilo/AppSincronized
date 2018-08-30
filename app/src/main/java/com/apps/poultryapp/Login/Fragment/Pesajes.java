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

import com.apps.poultryapp.Login.Provider.ContratosData;
import com.apps.poultryapp.Login.Sync.SyncAdapter;
import com.apps.poultryapp.Login.adapter.AdapterCorrals;
import com.apps.poultryapp.Login.adapter.AdapterPesajes;
import com.apps.poultryapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pesajes extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private LinearLayoutManager linearLayoutManager;
    private AdapterPesajes adapterPesajes;
    private RecyclerView recyclerView;


    public Pesajes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pesajes, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_pesajes);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapterPesajes =  new AdapterPesajes(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterPesajes);
        SyncAdapter.sincronizarAhora(getContext(), false);

        getLoaderManager().initLoader(0,null,this);
        SyncAdapter.inicializarSyncAdapter(getContext());


        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(), ContratosData.CONTENT_URI_WEIGHINGS,null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
         adapterPesajes.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapterPesajes.swapCursor(null);

    }
}
