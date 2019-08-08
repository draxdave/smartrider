/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package ir.drax.smartrider.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.drax.smartrider.BuildConfig;
import ir.drax.smartrider.R;
import ir.drax.smartrider.data.network.model.GoogleDirectionResponse;
import ir.drax.smartrider.ui.base.BaseActivity;
import ir.drax.smartrider.ui.login.LoginActivity;
import ir.drax.smartrider.ui.main.navigation.GoogleNavigation;
import ir.drax.smartrider.ui.main.navigation.A2F_NavigationInteraction;
import ir.drax.smartrider.ui.main.navigation.F2A_NavigationInteraction;
import ir.drax.smartrider.utils.view.RoundedImageView;

/**
 * Created by janisharali on 27/01/17.
 */

public class MainActivity extends BaseActivity implements MainMvpView , F2A_NavigationInteraction {

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_view)
    DrawerLayout mDrawer;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.tv_app_version)
    TextView mAppVersionTextView;

    @BindView(R.id.swipe)
    SwipePlaceHolderView swipe;


    private TextView mNameTextView;

    private TextView mEmailTextView;

    private RoundedImageView mProfileImageView;

    private ActionBarDrawerToggle mDrawerToggle;

    private A2F_NavigationInteraction a2FNavigationInteraction;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();

        loadFragments();
    }

    private void loadFragments() {
        lockDrawer();

        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.frame, GoogleNavigation.newInstance(), "Navigation")
                .commit();


    }

    @Override
    public void onBackPressed() {

        if (a2FNavigationInteraction !=null){
            if (a2FNavigationInteraction.onBackPressed())super.onBackPressed();

        }else super.onBackPressed();
    }

    @Override
    public void updateAppVersion() {
        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        mAppVersionTextView.setText(version);
    }

    @Override
    public void updateUserName(String currentUserName) {
        mNameTextView.setText(currentUserName);
    }

    @Override
    public void updateUserEmail(String currentUserEmail) {
        mEmailTextView.setText(currentUserEmail);
    }

    @Override
    public void updateUserProfilePic(String currentUserProfilePicUrl) {
        //load profile pic url into ANImageView
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
            unlockDrawer();
        }
    }

    @Override
    public void lockDrawer() {
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer() {
        if (mDrawer != null)
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void setGoogleWaypoints(GoogleDirectionResponse googleDirectionResponse) {
        a2FNavigationInteraction.setGoogleWaypoints(googleDirectionResponse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        switch (item.getItemId()) {

            case R.id.action_share:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setUp() {
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        setupNavMenu();
        mPresenter.onNavMenuCreated();
        //setupCardContainerView();
        mPresenter.onViewInitialized();
    }

    void setupNavMenu() {
        View headerLayout = mNavigationView.getHeaderView(0);
        mProfileImageView = headerLayout.findViewById(R.id.iv_profile_pic);
        mNameTextView = headerLayout.findViewById(R.id.tv_name);
        mEmailTextView = headerLayout.findViewById(R.id.tv_email);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        mDrawer.closeDrawer(GravityCompat.START);
                        switch (item.getItemId()) {
                            case R.id.nav_item_logout:
                                mPresenter.onDrawerOptionLogoutClick();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void closeNavigationDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        a2FNavigationInteraction.onMapReady();

    }

    public void setA2FNavigationInteraction(A2F_NavigationInteraction a2FNavigationInteraction) {
        this.a2FNavigationInteraction = a2FNavigationInteraction;
    }

    @Override
    public void hideAppbar() {

        getSupportActionBar().hide();
    }

    @Override
    public void getGoogleDirection(String params) {

    }
}
