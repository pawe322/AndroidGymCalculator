package com.pawe322dev.gymcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView RM1,RM5,RM6,RM8,RM10,RM12;
    SeekBar seekbar1,seekbar2;
    int progress1 = 1;
    int progress2 = 1;
    int textsize = 30;
    double ciezar;
    int powt;
    double[] dziel = new double[] {1, 0.943, 0.906, 0.881, 0.856, 0.831, 0.807, 0.786, 0.765, 0.744, 0.723, 0.703};
    String unit = " kg";

    private Tracker mTracker;
    private static final String TAG = "MainActivity";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this,"ca-app-pub-8851289925888038~7921459180");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Google analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // textView + seekbar from WEIGHT
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar1.setMax(299);
        seekbar1.setProgress(progress1);
        final TextView textView1 = findViewById(R.id.textView1);
        textView1.setTextSize(textsize);
        textView1.setText(progress1+unit);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress1=i;
                textView1.setText(progress1+1+unit);

                licz();
            }
            @Override
            public void onStartTrackingTouch (SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch (SeekBar seekBar){}
        });

        // textView + seekbar from REPS
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar2.setMax(11);
        seekbar2.setProgress(progress2);
        final TextView textView2 = findViewById(R.id.textView2);
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
        Button buttonPW = findViewById(R.id.buttonPlusWeight);
        buttonPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress1>seekbar1.getMax()-1) {}
                    else progress1+= 1;
                seekbar1.setProgress(progress1);
                textView1.setText(progress1+1+unit);
                licz();
            }
        });
        Button buttonMW = findViewById(R.id.buttonMinusWeight);
        buttonMW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress1<1) {}
                    else progress1-= 1;
                seekbar1.setProgress(progress1);
                textView1.setText(progress1+1+unit);
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

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ciezar=seekbar1.getProgress()+1;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.MetricsUnit) {
            SetMenuTracker("Action","Metrics Unit");
            unit=" kg";
            seekbar1.setMax(299);
            seekbar1.setProgress((int) (ciezar/2.205));
            licz();
        } else if (id == R.id.ImperialUnit) {
            SetMenuTracker("Action","Imperial Unit");
            unit=" lbs";
            seekbar1.setMax(599);
            seekbar1.setProgress((int) (ciezar*2.205));
            licz();
        } else if (id == R.id.nav_share) {
            SetMenuTracker("Action","Share");
            ShareApp();
        } else if (id == R.id.nav_send) {
            SetMenuTracker("Action","Feedback");
            CreateDialogFeedbackView();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SetMenuTracker(String Category, String Action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(Category)
                .setAction(Action)
                .build());
    }

    private void ShareApp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Have a precise measurements! https://play.google.com/store/apps/details?id=com.pawe322dev.gymcalculator";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "GymCalculator");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    //method that counts the load on the bar
    private void licz() {
        long ciezarNa1;
        ciezar=seekbar1.getProgress()+1;
        powt=seekbar2.getProgress()+1;
        ciezarNa1=Math.round(ciezar / (dziel[powt - 1]) * 100) / 100;

        RM1.setText(String.valueOf((int)ciezarNa1) + unit);
        if(powt == 5)RM5.setText(String.valueOf((int)ciezar) + unit);
        else RM5.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[4]) * 100) / 100) + unit);
        if(powt == 6)RM6.setText(String.valueOf((int)ciezar) + unit);
        else RM6.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[5]) * 100) / 100) + unit);
        if(powt == 8)RM8.setText(String.valueOf((int)ciezar) + unit);
        else RM8.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[7]) * 100) / 100) + unit);
        if(powt == 10)RM10.setText(String.valueOf((int)ciezar) + unit);
        else RM10.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[9]) * 100) / 100) + unit);
        if(powt == 12)RM12.setText(String.valueOf((int)ciezar) + unit);
        else RM12.setText(String.valueOf(Math.round(ciezarNa1 * (dziel[11]) * 100) / 100) + unit);
    }

    private void CreateDialogFeedbackView() {
        final Context context = this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_feedback, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.FeedbackMessageText);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(!userInput.getText().toString().matches("")){
                                    sendFeedbackMessage(userInput.getText().toString());
                                    Toast.makeText(getApplicationContext(),"Successfully sent message",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Message not sent ",Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();

                            }
                        })
                .setNegativeButton("Give me time",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"Message not sent ",Toast.LENGTH_SHORT).show();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void sendFeedbackMessage(String TextMessage) {
        final String textMessage = TextMessage;
        Thread sender = new Thread(new Runnable() {
                public void run() {
                    try {
                        GMailSender sender = new GMailSender("pawe322Feedback@gmail.com", "czxt*7^JhB&Px&_&");
                        sender.sendMail("[Feedback] GymCalculator", ""+textMessage, "pawe322Feedback@gmail.com", "pawe322Dev@gmail.com");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }
            });
            sender.start();
    }

}