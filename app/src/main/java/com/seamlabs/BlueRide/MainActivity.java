package com.seamlabs.BlueRide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.mentor_home.view.MentorHomeFragment;
import com.seamlabs.BlueRide.mentor_home.view.MentorPendingFragment;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.view.ParentHomeFragment;
import com.seamlabs.BlueRide.parent_flow.account.view.ParentSignInActivity;
import com.seamlabs.BlueRide.parent_flow.add_helper.view.AddHelperActivity;
import com.seamlabs.BlueRide.parent_flow.profile.view.EditProfileFragment;
import com.seamlabs.BlueRide.parent_flow.profile.view.ParentProfileFragment;
import com.seamlabs.BlueRide.parent_flow.tracking_helper.view.TrackingHelpersFragment;
import com.seamlabs.BlueRide.utils.MessageEvent;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.ARABIC;
import static com.seamlabs.BlueRide.utils.Constants.ENGLISH;
import static com.seamlabs.BlueRide.utils.Constants.EVENT_PICTURE_CHANGED;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_ACTIVITY;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.SHARED_PENDING_STUDENTS;
import static com.seamlabs.BlueRide.utils.Constants.TEACHER_USER_TYPE;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.getUserType;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.setUserType;

public class MainActivity extends MyActivity
        implements NavigationView.OnNavigationItemSelectedListener, ParentProfileFragment.onEditProfileClickListener, MyFragment.onNavigationIconClickListener {

    String TAG = MainActivity.class.getSimpleName();

    FragmentManager fragmentManager = getSupportFragmentManager();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.edit_profile)
    ImageView edit_profile;

    @BindView(R.id.parent_toolbar_layout)
    LinearLayout parent_toolbar_layout;
    @BindView(R.id.profile_toolbar_layout)
    LinearLayout profile_toolbar_layout;
    @BindView(R.id.track_helper_toolbar_layout)
    LinearLayout track_helper_toolbar_layout;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbar_subtitle;
    @BindView(R.id.toolbar_logo)
    SimpleDraweeView toolbar_logo;
    String userType;

    UserResponseModel userProfileModel;

    TextView nav_header_username;
    TextView nav_header_email;
    SimpleDraweeView nav_header_icon;
    Button nav_header_switchaccount;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_parent_home);
        ButterKnife.bind(this);
//        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setIcon(R.mipmap.school_ico);
        userProfileModel = UserSettingsPreference.getSavedUserProfile(this);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.setHomeAsUpIndicator(null);
        toggle.syncState();
        userType = getUserType(this);
        navigationView = findViewById(R.id.nav_view);
        nav_header_switchaccount = navigationView.getHeaderView(0).findViewById(R.id.switch_account);
        nav_header_email = navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        nav_header_username = navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        nav_header_icon = navigationView.getHeaderView(0).findViewById(R.id.nav_header_icon);
        nav_header_email.setText(userProfileModel.getEmail());
        nav_header_username.setText(userProfileModel.getName());
        if (userProfileModel.getImages().size() > 0) {
            nav_header_icon.setImageURI(Uri.parse(userProfileModel.getImages().get(0).getPath()));
        }
        nav_header_switchaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userProfileModel.getRoles().size(); i++) {
                    String type = userProfileModel.getRoles().get(i).getName();
                    if (userType.equals(PARENT_USER_TYPE))
                        if (type.equals(MENTOR_USER_TYPE)) {
                            setUserType(MainActivity.this, MENTOR_USER_TYPE);
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            return;
                        }
                    if (userType.equals(MENTOR_USER_TYPE))
                        if (type.equals(PARENT_USER_TYPE)) {
                            setUserType(MainActivity.this, PARENT_USER_TYPE);
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            return;
                        }
                }
                Toast.makeText(MainActivity.this, getResources().getString(R.string.you_dont_have_another_account), Toast.LENGTH_SHORT).show();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        controlTheNavigationViewItems(navigationView.getMenu());
        showInitialFragment();
        customizeToolbarBasedOnUserType(userType);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileFragment();
            }
        });
        Log.i("TOKEN", "User token " + PrefUtils.getApiKey(this));
    }

    MyFragment fragment = null;

    private void showInitialFragment() {
        parent_toolbar_layout.setVisibility(View.VISIBLE);
        track_helper_toolbar_layout.setVisibility(View.GONE);
        profile_toolbar_layout.setVisibility(View.GONE);
        if (userType.equals(PARENT_USER_TYPE) || userType.equals(HELPER_USER_TYPE)) {
            fragment = new ParentHomeFragment();
        } else if (userType.equals(MENTOR_USER_TYPE) || userType.equals(TEACHER_USER_TYPE)) {
            fragment = new MentorHomeFragment();
        }
        fragment.setNavigationIconClickListener(this);
        Log.i(TAG, userType);
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private void showTrackingHelperFragment() {
        parent_toolbar_layout.setVisibility(View.GONE);
        profile_toolbar_layout.setVisibility(View.GONE);
        track_helper_toolbar_layout.setVisibility(View.VISIBLE);
        Log.i(TAG, userType);
        fragment = new TrackingHelpersFragment();
        fragment.setNavigationIconClickListener(this);
        replaceFragment(fragment);
    }

    private void showSettingFragment() {
        if (userProfileModel.getImages() != null)
            if (userProfileModel.getImages().size() > 0) {
                Uri uri = Uri.parse(userProfileModel.getImages().get(0).getPath());
                user_profile_picture.setImageURI(uri);
            }
        user_profile_name.setText(userProfileModel.getName());
        edit_profile.setVisibility(View.VISIBLE);
        parent_toolbar_layout.setVisibility(View.GONE);
        track_helper_toolbar_layout.setVisibility(View.GONE);
        profile_toolbar_layout.setVisibility(View.VISIBLE);
        fragment = new ParentProfileFragment();
        ((ParentProfileFragment) fragment).setOnEditProfileClickListener(this);
        fragment.setNavigationIconClickListener(this);
        replaceFragment(fragment);
    }

    private void showEditProfileFragment() {
        if (userProfileModel.getImages() != null)
            if (userProfileModel.getImages().size() > 0) {
                Uri uri = Uri.parse(userProfileModel.getImages().get(0).getPath());
                user_profile_picture.setImageURI(uri);
            }
        user_profile_name.setText(userProfileModel.getName());
        edit_profile.setVisibility(View.INVISIBLE);
        parent_toolbar_layout.setVisibility(View.GONE);
        track_helper_toolbar_layout.setVisibility(View.GONE);
        profile_toolbar_layout.setVisibility(View.VISIBLE);

        fragment = new EditProfileFragment();
        fragment.setNavigationIconClickListener(this);
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment, EditProfileFragment.class.getSimpleName())
                .addToBackStack(null).commit();

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
        MenuItem pendingStudents = menu.findItem(R.id.nav_pending_students);
        MenuItem trackingHelper = menu.findItem(R.id.nav_tracking);
        if (!userType.equals(PARENT_USER_TYPE)) {
            if (userType.equals(MENTOR_USER_TYPE)) {
                pendingStudents.setVisible(true);
            }
            add_helper.setVisible(false);
            trackingHelper.setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            if (UserSettingsPreference.getUserLanguage(this).equals(ENGLISH))
            drawer.closeDrawer(GravityCompat.START);
//            else if (UserSettingsPreference.getUserLanguage(this).equals(ARABIC))
//                drawer.closeDrawer(GravityCompat.END);
        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
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
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
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
            case R.id.nav_tracking:
                showTrackingHelperFragment();
                break;
            case R.id.nav_notification:
                break;
            case R.id.nav_setting:
                showSettingFragment();
                break;
            case R.id.nav_pending_students:
                fragment = new MentorPendingFragment();
                fragment.setNavigationIconClickListener(this);
                replaceFragment(fragment);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(MainActivity.this, ParentSignInActivity.class));
                UserSettingsPreference.getUserSettingsSharedPreferences(MyApplication.getMyApplicationContext()).edit().clear().commit();
                PrefUtils.getPrefUtilsSharedPreferences(MyApplication.getMyApplicationContext()).edit().clear().commit();
                SharedPreferences sharedPreferences =
                        getSharedPreferences(SHARED_PENDING_STUDENTS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear().commit();
                finish();
                break;
            default:
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (UserSettingsPreference.getUserLanguage(this).equals(ENGLISH))
        drawer.closeDrawer(GravityCompat.START);
//        else if (UserSettingsPreference.getUserLanguage(this).equals(ARABIC))
//            drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void replaceFragment(final MyFragment finalFragment) {
        MyFragment currentFragment = (MyFragment) fragmentManager.findFragmentById(R.id.frameLayout);
        if (currentFragment != null) {
            if (currentFragment.getClass().equals(finalFragment.getClass()))
                return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager.beginTransaction().replace(R.id.frameLayout, finalFragment).addToBackStack(null).commit();
            }
        }, 320);
    }

    @Override
    public void onEditProfileClick() {
        showEditProfileFragment();
    }

    @Override
    public void onNavigationIconClick() {
//        if (UserSettingsPreference.getUserLanguage(this).equals(ENGLISH))
        drawer.openDrawer(GravityCompat.START);
//        else if (UserSettingsPreference.getUserLanguage(this).equals(ARABIC))
//            drawer.openDrawer(GravityCompat.END);
//        drawer.openDrawer(Gravity.LEFT);
    }

    @Subscribe
    public void onEvent(MessageEvent event) {
        try {
            userProfileModel = UserSettingsPreference.getSavedUserProfile(this);
            if (event.getMessage().equals(EVENT_PICTURE_CHANGED)) {
                if (userProfileModel.getImages().size() > 0) {
                    nav_header_icon.setImageURI(Uri.parse(userProfileModel.getImages().get(0).getPath()));
                }
                nav_header_email.setText(userProfileModel.getEmail());
                nav_header_username.setText(userProfileModel.getName());
            }
        } catch (Exception e) {
            Log.i(TAG, "Change picture" + e.getMessage().toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EventBus.getDefault().isRegistered(this) == false)
            EventBus.getDefault().register(this);
    }
}
