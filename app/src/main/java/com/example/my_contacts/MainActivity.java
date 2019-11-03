package com.example.my_contacts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.my_contacts.adapters.ViewPagerAdapter;
import com.example.my_contacts.fragments.Fragment_Calls;
import com.example.my_contacts.fragments.Fragment_Favourite;
import com.example.my_contacts.fragments.FragmentsContacts;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toogle;
    NavigationView navigationView;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth mAuth;
    private  final int[] icons = {R.drawable.ic_person,R.drawable.ic_star};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toast.makeText(MainActivity.this, "Firebase connection success", Toast.LENGTH_LONG).show();*/
        mAuth = FirebaseAuth.getInstance();

        askPermission();
        addToolBar();
        displayTabs();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            sendToStartPage();
        }
    }
    private void addToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toogle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        navigationView = findViewById(R.id.nav_panel);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void sendToStartPage() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayTabs(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentsContacts(),"Contacts");
        //adapter.addFragment(new Fragment_Calls(),"Calls");
        adapter.addFragment(new Fragment_Favourite(),"Favourite");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(android.R.color.white,android.R.color.white);
        for (int i=0; i<tabLayout.getTabCount();i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setIcon(icons[i]);
        }
    }

    public void askPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG},1);
        }
    }
    public void askCallPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_log_out) {
            FirebaseAuth.getInstance().signOut();
            sendToStartPage();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        FragmentsContacts fragmentsContacts = new FragmentsContacts();
        boolean import_contact = false;

        if (id == R.id.nav_import) {
            Log.e("Hello", "hi");
        } else if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_manage) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
