package com.example.pawel.gymcalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button buttonPW, buttonMW;
    SeekBar seekbar1, seekbar2;
    TextView textView1, textView2, RM1, RM5, RM6, RM8, RM10, RM12;
    int progress1 = 1;
    int progress2 = 1;
    int textsize = 30;
    double ciezar;
    int powt;
    double[] dziel = new double[] {1, 0.943, 0.906, 0.881, 0.856, 0.831, 0.807, 0.786, 0.765, 0.744, 0.723, 0.703};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // textView + seekbar from weight
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar1.setMax(299);
        seekbar1.setProgress(progress1);

        textView1 = findViewById(R.id.textView1);
        textView1.setTextSize(textsize);
        textView1.setText(progress1+" kg");

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress1=i;
                textView1.setText(progress1+1+" kg");

                licz();
            }

            @Override
            public void onStartTrackingTouch (SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch (SeekBar seekBar){}
        });

        // textView + seekbar from reps
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar2.setMax(11);
        seekbar2.setProgress(progress2);

        textView2 = findViewById(R.id.textView2);
        textView2.setTextSize(textsize);
        textView2.setText(progress2+" reps");

        RM1 = findViewById(R.id.RM1);
        RM5 = findViewById(R.id.RM5);
        RM6 = findViewById(R.id.RM6);
        RM8 = findViewById(R.id.RM8);
        RM10 = findViewById(R.id.RM10);
        RM12 = findViewById(R.id.RM12);

        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress2 = i;
                textView2.setText(progress2+1+" reps");
                licz();
            }
            @Override
            public void onStartTrackingTouch (SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch (SeekBar seekBar){}
        });

        // buttons
        buttonPW = findViewById(R.id.buttonPlusWeight);
        buttonMW = findViewById(R.id.buttonMinusWeight);

        buttonPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress1>299) {}
                    else progress1+= 1;
                seekbar1.setProgress(progress1);
                textView1.setText(progress1+1+" kg");
                licz();
            }
        });

        buttonMW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress1<2) {}
                    else progress1-= 1;
                seekbar1.setProgress(progress1);
                textView1.setText(progress1+1+" kg");
                licz();
            }
        });

        // Toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //method that counts the load on the bar
    private void licz() {
        long ciezarNa1;
        ciezar=seekbar1.getProgress()+1;
        powt=seekbar2.getProgress()+1;
        ciezarNa1=Math.round(ciezar / (dziel[powt - 1]) * 100) / 100;

        RM1.setText(String.valueOf((int)ciezarNa1) + " kg");
        if(powt == 5)RM5.setText(String.valueOf((int)ciezar) + " kg");
            else RM5.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[4]) * 100) / 100) + " kg");
        if(powt == 6)RM6.setText(String.valueOf((int)ciezar) + " kg");
            else RM6.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[5]) * 100) / 100) + " kg");
        if(powt == 8)RM8.setText(String.valueOf((int)ciezar) + " kg");
            else RM8.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[7]) * 100) / 100) + " kg");
        if(powt == 10)RM10.setText(String.valueOf((int)ciezar) + " kg");
            else RM10.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[9]) * 100) / 100) + " kg");
        if(powt == 12)RM12.setText(String.valueOf((int)ciezar) + " kg");
            else RM12.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[11]) * 100) / 100) + " kg");
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}