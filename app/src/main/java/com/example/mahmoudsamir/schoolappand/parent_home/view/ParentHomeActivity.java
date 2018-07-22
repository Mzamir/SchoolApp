package com.example.mahmoudsamir.schoolappand.parent_home.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.network.response.UserSchoolsResponse;
import com.example.mahmoudsamir.schoolappand.parent_account.presenter.ParentSignupPresenter;
import com.example.mahmoudsamir.schoolappand.parent_home.adapter.SchoolsRecyclerAdapter;
import com.example.mahmoudsamir.schoolappand.parent_home.model.SchoolModel;
import com.example.mahmoudsamir.schoolappand.parent_home.presenter.ParentHomeInteractor;
import com.example.mahmoudsamir.schoolappand.parent_home.presenter.ParentHomePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ParentHomeView {

    @BindView(R.id.schools_recyclerView)
    RecyclerView schools_recyclerView;
    ParentHomePresenter presenter;
    SchoolsRecyclerAdapter schoolsRecyclerAdapter;
    ArrayList<SchoolModel> schoolsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.school_ico);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        presenter = new ParentHomePresenter(this, new ParentHomeInteractor());
        presenter.getParentSchools();

        schoolsRecyclerAdapter = new SchoolsRecyclerAdapter(this, schoolsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        schools_recyclerView.setLayoutManager(layoutManager);
        schools_recyclerView.setItemAnimator(new DefaultItemAnimator());
        schools_recyclerView.setAdapter(schoolsRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_add_helper) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorGettingSchools(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessGettingSchool(ArrayList<SchoolModel> schoolList) {
        this.schoolsList = schoolList;
        schoolsRecyclerAdapter.notifyDataSetChanged();
    }
}