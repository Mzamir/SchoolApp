package com.example.mahmoudsamir.schoolappand;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.mentor_home.view.MentorHomeFragment;
import com.example.mahmoudsamir.schoolappand.network.response.UserResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.view.ParentHomeFragment;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.model.UserProfileModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.view.ParentProfileActivity;
import com.example.mahmoudsamir.schoolappand.parent_flow.account.view.ParentSignInActivity;
import com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.view.AddHelperActivity;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.view.ParentProfileFragment;
import com.example.mahmoudsamir.schoolappand.utils.PrefUtils;
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.HELPER_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.MENTOR_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_ACTIVITY;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference.getUserType;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String TAG = MainActivity.class.getSimpleName();

    FragmentManager fragmentManager = getSupportFragmentManager();
    Toolbar toolbar;

    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;

    @BindView(R.id.parent_toolbar_layout)
    LinearLayout parent_toolbar_layout;
    @BindView(R.id.profile_toolbar_layout)
    LinearLayout profile_toolbar_layout;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;
    @BindView(R.id.toolbar_logo)
    SimpleDraweeView toolbar_logo;
    String userType;

    UserResponseModel userProfileModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_parent_home);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setIcon(R.mipmap.school_ico);
        userProfileModel = UserSettingsPreference.getSavedUserProfile(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.setHomeAsUpIndicator(null);
        toggle.syncState();
        userType = getUserType(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        controlTheNavigationViewItems(navigationView.getMenu());
        showInitialFragment();
        customizeToolbarBasedOnUserType(userType);
    }


    private void showInitialFragment() {
        parent_toolbar_layout.setVisibility(View.VISIBLE);
        profile_toolbar_layout.setVisibility(View.GONE);
        Fragment fragment = null;
        if (userType.equals(PARENT_USER_TYPE) || userType.equals(HELPER_USER_TYPE)) {
            fragment = new ParentHomeFragment();
        } else if (userType.equals(MENTOR_USER_TYPE)) {
            fragment = new MentorHomeFragment();
        }
        Log.i(TAG, userType);
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private void customizeToolbarBasedOnUserType(String currentUserType) {
        if (currentUserType.equals(PARENT_USER_TYPE) || currentUserType.equals(HELPER_USER_TYPE)) {
            toolbar_subtitle.setVisibility(View.VISIBLE);
            toolbar_title.setText("Schools");
            toolbar_logo.setActualImageResource(R.mipmap.school_ico);
        } else if (userType.equals(MENTOR_USER_TYPE)) {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(true);
            toolbar_subtitle.setVisibility(View.GONE);
            toolbar_title.setText(userProfileModel.getName());
            Uri uri = Uri.parse(userProfileModel.getImages().get(0).getPath());
            toolbar_logo.setImageURI(uri);
            toolbar_logo.getHierarchy().setRoundingParams(roundingParams);
        }
    }

    private void controlTheNavigationViewItems(Menu menu) {
        MenuItem add_helper = menu.findItem(R.id.nav_add_helper);
        if (userType.equals(MENTOR_USER_TYPE) || userType.equals(PARENT_USER_TYPE)) {
            add_helper.setVisible(false);
        }
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
                showInitialFragment();
                break;
            case R.id.nav_add_helper:
                intent = new Intent(MainActivity.this, AddHelperActivity.class);
                intent.putExtra(PARENT_ACTIVITY, MainActivity.class.getSimpleName());
                startActivity(intent);
                break;
            case R.id.nav_setting:
                if (userProfileModel.getImages().get(0) != null) {
                    Uri uri = Uri.parse(userProfileModel.getImages().get(0).getPath());
                    user_profile_picture.setImageURI(uri);
                }
                user_profile_name.setText(userProfileModel.getName());
                parent_toolbar_layout.setVisibility(View.GONE);
                profile_toolbar_layout.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().replace(R.id.frameLayout, new ParentProfileFragment()).commit();
//                intent = new Intent(MainActivity.this, ParentProfileActivity.class);
//                intent.putExtra(PARENT_ACTIVITY, MainActivity.class.getSimpleName());
//                intent.putExtra(USER_TYPE, userType);
//                startActivity(intent);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(MainActivity.this, ParentSignInActivity.class));
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
