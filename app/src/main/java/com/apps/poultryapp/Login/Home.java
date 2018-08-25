package com.apps.poultryapp.Login;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apps.poultryapp.Login.Dialogs.NewCorral;
import com.apps.poultryapp.Login.Dialogs.NewGalpon;
import com.apps.poultryapp.Login.Dialogs.NewLote;
import com.apps.poultryapp.Login.Fragment.Corrales;
import com.apps.poultryapp.Login.Fragment.Galpones;
import com.apps.poultryapp.Login.Fragment.Lotes;
import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.Login.Login.View.Login;
import com.apps.poultryapp.Login.Sync.SyncAdapter;
import com.apps.poultryapp.Login.adapter.FragmenAdapter;
import com.apps.poultryapp.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private DrawerLayout drawer;
    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private FloatingActionButton fab_main_galpones;
    private FloatingActionButton fab_main_corrales;


    private TabLayout mTabLayout;
    private RelativeLayout relative_main;
    private ImageView img_page_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViewPager();
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_header = headerView.findViewById(R.id.nav_header);
        nav_header.setOnClickListener(this);

        fab = findViewById(R.id.fab_main);
        fab_main_corrales = findViewById(R.id.fab_main_corrales);
        fab_main_galpones = findViewById(R.id.fab_main_galpones);

        fab_main_corrales.hide();
        fab_main_galpones.hide();
        fab.show();

        fab.setOnClickListener(this);
        fab_main_galpones.setOnClickListener(this);
        fab_main_corrales.setOnClickListener(this);

        relative_main  =  findViewById(R.id.relative_main);
        img_page_start = findViewById(R.id.img_page_start);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_main_1:

                SyncAdapter.sincronizarAhora(this, false);
                Toast.makeText(getApplicationContext(),"Sincronizando..",Toast.LENGTH_SHORT).show();
                //SessionPref.get(getApplicationContext()).logOut();
                //startActivity(new Intent(Home.this, Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
                SessionPref.get(getApplicationContext()).logOut();
                startActivity(new Intent(Home.this, Login.class));
                break;
            case R.id.nav_about:
                Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
            break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViewPager() {
    mTabLayout = findViewById(R.id.tab_layout_main);
    mViewPager = findViewById(R.id.view_pager_main);

        List<String> titles = new ArrayList<>();
        titles.add("Lotes");
        titles.add("Galpones");
        titles.add("Corrales");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Lotes());
        fragments.add(new Galpones());
        fragments.add(new Corrales());

        mViewPager.setOffscreenPageLimit(2);

        FragmenAdapter mFragmentAdapter = new FragmenAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapter);

        mViewPager.addOnPageChangeListener(pageChangeListener);



    }
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {

            switch (position){
                case 0:
                    fab_main_corrales.hide();
                    fab_main_galpones.hide();
                    fab.show();
                    break;
                case 1:
                    fab.hide();
                    fab_main_corrales.hide();
                    fab_main_galpones.show();
                    break;
                case 2:
                    fab.hide();
                    fab_main_galpones.hide();
                    fab_main_corrales.show();
                    break;
            }


        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_main:
                DialogFragment dialogFragment =  new NewLote();
                dialogFragment.show(getSupportFragmentManager(),"lote");
                /*Snackbar.make(v, getString(R.string.app_name), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.code_text), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();*/
                break;
            case R.id.fab_main_galpones:
                DialogFragment dialogGalpon = new NewGalpon();
                dialogGalpon.show(getSupportFragmentManager(),"galpon");
                break;
            case R.id.fab_main_corrales:
                DialogFragment dialogCorral = new NewCorral();
                dialogCorral.show(getSupportFragmentManager(),"corral");
                break;

        }
    }
}
