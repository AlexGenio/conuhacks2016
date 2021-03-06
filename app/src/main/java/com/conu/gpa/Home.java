package com.conu.gpa;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.conu.gpa.fragments.CoursesFragment;
import com.conu.gpa.fragments.PeopleFragment;
import com.conu.gpa.networking.GPAAPI;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(Globals.user != null && Globals.user.pictureLink != null){

            GPAAPI.GetImage(getApplicationContext(),
                    ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView)),
                    Globals.MEDIA_URL + Globals.user.pictureLink);

            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.name)).setText(Globals.user.name);
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.school)).setText(Globals.user.schoolName);

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CoursesFragment()).commit();
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle("Courses");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            GPAAPI.Logout(getApplicationContext(), Globals.getToken(getApplicationContext(), this), this);
            Globals.setToken(getApplicationContext(), this, "");
            Globals.password = null;
            Globals.username = null;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new CoursesFragment()).commit();
            if(getSupportActionBar() != null)
                getSupportActionBar().setTitle("Courses");
        } else if (id == R.id.nav_slideshow) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new PeopleFragment()).commit();
            if(getSupportActionBar() != null)
                getSupportActionBar().setTitle("Find People");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
