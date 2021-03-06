package com.rafiki.wits.sdp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static View view;
    private Resources r;
    private TextView nameView;
    private boolean exit = false;
    public InteractionListAdapter rsa;
    private RecyclerView recyclerView;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeNavLayout();
        setRecyclerView();
        view = findViewById(android.R.id.content);
        r = getBaseContext().getResources();
    }

    public boolean setRecyclerView() {
        if(LoginActivity.interactionList != null) {
            recyclerView = findViewById(R.id.questionList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            rsa = new InteractionListAdapter(this, LoginActivity.interactionList);
            recyclerView.setAdapter(rsa);
            return true;
        }
        return true;
    }

    public boolean makeNavLayout() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tv = findViewById(R.id.toolbarTitle);
        tv.setText("Tutor Interactions");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView header = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        nameView = navigationView.getHeaderView(0).findViewById(R.id.name);
//        gradeView = navigationView.getHeaderView(0).findViewById(R.id.grade);
        nameView.setText(LoginActivity.studentNum);
        Picasso.get().load(R.mipmap.header_background).into(header);

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!exit) {
                exit = true;
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 2000);
            } else {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sessions) {
            Intent intent = new Intent(MainActivity.this, StudentScheduleActivity.class);
            startActivity(intent);
        } else if (id == R.id.scan) {
            Intent intent = new Intent(MainActivity.this, QRActivity.class);
            startActivity(intent);
            //startAnimationFromBackgroundThread();
        } else if (id == R.id.record) {
            QuestionSubmitter questionSubmitter = new QuestionSubmitter(MainActivity.this, this.getLayoutInflater());
            questionSubmitter.getCourseSelection();
        } else if (id == R.id.subscribe) {
            SubscriptionAdder sa = new SubscriptionAdder(MainActivity.this,getLayoutInflater(), getWindowManager());
        }
        else if(id == R.id.logout){
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.context);
            mEditor = mSharedPreferences.edit();
            mEditor.putString("userData","empty");
            mEditor.commit();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.announcement){
            Intent intent = new Intent(MainActivity.this,AnnouncementActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}  
