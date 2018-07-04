package com.example.achess.nav;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import com.example.achess.R;
import com.example.achess.nav.TitleFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity_nav extends AppCompatActivity implements RecyclerAdapter.ItemClickChild{


    @BindView(R.id.recyclerView)
   RecyclerView recyclerView;


    @BindView(R.id.nav_view)
    NavigationView navView;


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    Constant constant_menu = new Constant();


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.frame)
    FrameLayout frame;

    TitleFragment fragment;


    @Optional
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        final ActionBar actionar = getSupportActionBar();
        actionar.setDisplayHomeAsUpEnabled(true);
        actionar.setHomeAsUpIndicator(R.drawable.ic_menu);

        List<TitleMenu> list = getList();
        RecyclerAdapter adapter = new RecyclerAdapter(this, list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        setFragment();
    }

    private void setFragment() {
        fragment = new TitleFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, "TitleFragment").commit();
    }

    private List<TitleMenu> getList() {
        List<TitleMenu> list = new ArrayList<>();


        // Получаем набор элементов
        Set<Map.Entry<String, List<String>>> set =  constant_menu.menumap.entrySet();

        for (Map.Entry<String, List<String>> me : set) {
            List<SubTitle> subTitles = new ArrayList<>();
            List<String> subnamesarr = me.getValue();
            for (String subname : subnamesarr) {
                SubTitle subTitle = new SubTitle(subname);
                subTitles.add(subTitle);
            }
            TitleMenu model = new TitleMenu(me.getKey(), subTitles, null);
            list.add(model);
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChildClick(int position) {
        // Получаем набор элементов
        //Set<Map.Entry<String, List<String>>> set =  constant_menu.menumap.entrySet();

        //String name = set.;
        drawerLayout.closeDrawers();
        //fragment.setTitle(name);
    }
}
