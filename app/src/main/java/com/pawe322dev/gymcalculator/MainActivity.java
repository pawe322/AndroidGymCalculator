package com.pawe322dev.gymcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.WindowManager;
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

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static int SPLASH_TIME_OUT = 200;
    TextView RM1,RM5,RM6,RM8,RM10,RM12, RM15;
    SeekBar seekbar1,seekbar2;
    int progress1 = 1;
    int progress2 = 1;
    int textsize = 30;
    double ciezar;
    int powt;
    double[] dziel = new double[] {1, 0.943, 0.906, 0.881, 0.856, 0.831, 0.807, 0.786, 0.765, 0.744, 0.723, 0.703, 0.688, 0.675, 0.662};
    String unit = " kg";
    boolean isRound = false;

    private Tracker mTracker;
    private static final String TAG = "MainActivity";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);*/

       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MobileAds.initialize(this,"ca-app-pub-8851289925888038~7921459180");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Google analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        final String Reps = getString(R.string.Reps);
        // textView + seekbar from WEIGHT
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar1.setMax(299);
        seekbar1.setProgress(0);
        final TextView textView1 = findViewById(R.id.textView1);
        textView1.setTextSize(textsize);
        textView1.setText(1+unit);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress1=i;
                textView1.setText(progress1+1+unit);
                licz(isRound);
            }
            @Override
            public void onStartTrackingTouch (SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch (SeekBar seekBar){}
        });

        // textView + seekbar from REPS
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar2.setMax(14);
        seekbar2.setProgress(0);
        final TextView textView2 = findViewById(R.id.textView2);
        textView2.setTextSize(textsize);
        textView2.setText(progress2+unit);
        RM1 = findViewById(R.id.RM1);
        RM5 = findViewById(R.id.RM5);
        RM6 = findViewById(R.id.RM6);
        RM8 = findViewById(R.id.RM8);
        RM10 = findViewById(R.id.RM10);
        RM12 = findViewById(R.id.RM12);
        RM15 = findViewById(R.id.RM15);

        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress2 = i;
                textView2.setText(progress2+1+" "+Reps);
                licz(isRound);
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
                textView1.setText(progress1+unit);
                licz(isRound);
            }
        });
        Button buttonMW = findViewById(R.id.buttonMinusWeight);
        buttonMW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress1<1) {}
                    else progress1-= 1;
                seekbar1.setProgress(progress1);
                textView1.setText(progress1+unit);
                licz(isRound);
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
        if (id == R.id.infoButton) {
            CreateDialogInfoView();
        }
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
            licz(isRound);
        } else if (id == R.id.ImperialUnit) {
            SetMenuTracker("Action","Imperial Unit");
            unit=" lbs";
            seekbar1.setMax(599);
            seekbar1.setProgress((int) (ciezar*2.205));
            licz(isRound);
        } else if (id == R.id.Round) {
            SetMenuTracker("Action","Round result");
            isRound = !isRound;
            licz(isRound);
        } else if (id == R.id.nav_share) {
            SetMenuTracker("Action","Share");
            ShareApp();
        } else if (id == R.id.nav_send) {
            SetMenuTracker("Action","Feedback");
            CreateDialogFeedbackView();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SetMenuTracker(String Category, String Action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(Category)
                .setAction(Action)
                .build());
    }

    private String doCountRound(boolean isRound, int powt, double ciezarNa1){
        String CountedFraze;
        if(!isRound){
            CountedFraze = ((new DecimalFormat("##.##").format((ciezarNa1 * (dziel[powt]) * 100) / 100)) + unit);
        } else {
            CountedFraze = ((new DecimalFormat("##").format((ciezarNa1 * (dziel[powt]) * 100) / 100)) + unit);
        }

        return CountedFraze;
    }

    //method that counts the load on the bar
    private void licz(boolean isRound) {
        double ciezarNa1;
        ciezar=seekbar1.getProgress()+1;
        powt=seekbar2.getProgress()+1;
        String DefValue;

        ciezarNa1=(ciezar / (dziel[powt - 1]) * 100) / 100;

        if(!isRound) {
            DefValue = (new DecimalFormat("##.##").format(ciezar) + unit);
            RM1.setText(new DecimalFormat("##.##").format(ciezarNa1) + unit);
        } else {
            DefValue = (new DecimalFormat("##").format(ciezar) + unit);
            RM1.setText(String.valueOf((int)ciezarNa1) + unit);
        }

        if(powt == 5)RM5.setText(DefValue);
        else RM5.setText(doCountRound(isRound, 4, ciezarNa1));

        if(powt == 6)RM6.setText(DefValue);
        else RM6.setText(doCountRound(isRound, 5,ciezarNa1));

        if(powt == 8)RM8.setText(DefValue);
        else RM8.setText(doCountRound(isRound, 7,ciezarNa1));

        if(powt == 10)RM10.setText(DefValue);
        else RM10.setText(doCountRound(isRound, 9,ciezarNa1));

        if(powt == 12)RM12.setText(DefValue);
        else RM12.setText(doCountRound(isRound, 11,ciezarNa1));

        if(powt == 15)RM15.setText(DefValue);
        else RM15.setText(doCountRound(isRound, 14,ciezarNa1));
    }

    private void CreateDialogInfoView() {
        final String accept = getString(R.string.SendMessage);
        final String tryIt = getString(R.string.shareMsg);
        final Context context = this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_info, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false).setPositiveButton(accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), tryIt,Toast.LENGTH_SHORT).show();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void CreateDialogFeedbackView() {
        final String msgSent = getString(R.string.MessageSent);
        final String msgNotSent = getString(R.string.MessageNotSent);
        final String exitMsg = getString(R.string.ExitMessage);
        final String sendMsg = getString(R.string.SendMessage);
        final Context context = this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_feedback, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.FeedbackMessageText);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(sendMsg,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(!userInput.getText().toString().matches("")){
                                    sendFeedbackMessage(userInput.getText().toString());
                                    Toast.makeText(getApplicationContext(), msgSent,Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),msgNotSent,Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();

                            }
                        })
                .setNegativeButton(exitMsg,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),msgNotSent,Toast.LENGTH_SHORT).show();
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

    private void ShareApp() {
        String shareMsg = getString(R.string.shareMsg);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = shareMsg+" https://play.google.com/store/apps/details?id=com.pawe322dev.gymcalculator";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "GymCalculator");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}