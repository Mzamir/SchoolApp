package com.example.mahmoudsamir.schoolappand.parent_flow.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.ParentProfileActivity;
import com.example.mahmoudsamir.schoolappand.parent_flow.account.view.ParentSignInActivity;
import com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.view.AddHelperActivity;
import com.example.mahmoudsamir.schoolappand.utils.PrefUtils;
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_ACTIVITY;

public class ParentHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String TAG = ParentHomeActivity.class.getSimpleName();

    FragmentManager fragmentManager = getSupportFragmentManager();
    Toolbar toolbar;

    @BindView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setIcon(R.mipmap.school_ico);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager.beginTransaction().replace(R.id.frameLayout, new ParentHomeFragment()).commit();
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
        Intent intent;
        switch (id) {
            case R.id.nav_home:
                fragmentManager.beginTransaction().replace(R.id.frameLayout, new ParentHomeFragment()).commit();
                break;
            case R.id.nav_add_helper:
                intent = new Intent(ParentHomeActivity.this, AddHelperActivity.class);
                intent.putExtra(PARENT_ACTIVITY, ParentHomeActivity.class.getSimpleName());
                startActivity(intent);
                break;
            case R.id.nav_setting:
                intent = new Intent(ParentHomeActivity.this, ParentProfileActivity.class);
                intent.putExtra(PARENT_ACTIVITY, ParentHomeActivity.class.getSimpleName());
                startActivity(intent);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(ParentHomeActivity.this, ParentSignInActivity.class));
                UserSettingsPreference.getUserSettingsSharedPreferences(MyApplication.getMyApplicationContext()).edit().clear().commit();
                PrefUtils.getPrefUtilsSharedPreferences(MyApplication.getMyApplicationContext()).edit().clear().commit();
                finish();
                break;
            default:
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
