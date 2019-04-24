package com.pawe322Development.gymcalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static int SPLASH_TIME_OUT = 200;
    private TextView RM1,RM2,RM3,RM4,RM5,RM6,RM7,RM8,RM9,RM10,RM11,RM12,RM15,textView1,textView2;
    private SeekBar seekbar1,seekbar2;
    private Button buttonPW, buttonMW;
    private Toolbar toolbar;
    private int progress1 = 1;
    private int progress2 = 1;
    private int textsize = 30;
    private double ciezar;
    private int powt;
    private double[] dziel = new double[] {1, 0.943, 0.906, 0.881, 0.856, 0.831, 0.807, 0.786, 0.765, 0.744, 0.723, 0.703, 0.688, 0.675, 0.662};
    private String unit = " kg";
    private boolean isRound = false;
    private String FirebaseUnitID, Reps, PartnersUrl;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Tracker mTracker;
    private static final String TAG = "MainActivity";
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First start of the app
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            CreateDialogInfoView();
        }

        MobileAds.initialize(this,"ca-app-pub-8805158593485927~4923417527");
        GetFirebaseAdmobData();
        // Google analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        InitializeView();
    }

    @Override
    protected void onResume() {
        //mRewardedVideoAd.resume(this);
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
    protected void onDestroy() {
        //mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //mRewardedVideoAd.pause(this);
        super.onPause();
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
            SetMenuTracker("Action", "Feedback");
            CreateDialogFeedbackView();
        } else if (id == R.id.privacy_policy) {
            SetMenuTracker("Action","Privacy policy");
            goToUrl("https://sites.google.com/view/gymcalculatorprivacypolicy");
        } else if (id == R.id.partners_sites) {
            SetMenuTracker("Action","Partners Sites");
            goToUrl(PartnersUrl);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void InitializeView(){
        Reps = getString(R.string.Reps);

        seekbar1 = findViewById(R.id.seekBar1);
        textView1 = findViewById(R.id.textView1);
        seekbar2 = findViewById(R.id.seekBar2);
        textView2 = findViewById(R.id.textView2);
        RM1 = findViewById(R.id.RM1);
        RM2 = findViewById(R.id.RM2);
        RM3 = findViewById(R.id.RM3);
        RM4 = findViewById(R.id.RM4);
        RM5 = findViewById(R.id.RM5);
        RM6 = findViewById(R.id.RM6);
        RM7 = findViewById(R.id.RM7);
        RM8 = findViewById(R.id.RM8);
        RM9 = findViewById(R.id.RM9);
        RM10 = findViewById(R.id.RM10);
        RM11 = findViewById(R.id.RM11);
        RM12 = findViewById(R.id.RM12);
        RM15 = findViewById(R.id.RM15);

        buttonPW = findViewById(R.id.buttonPlusWeight);
        buttonMW = findViewById(R.id.buttonMinusWeight);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        AddListenersToViewElements();
    }

    private void AddListenersToViewElements(){
        // textView + seekbar from WEIGHT
        seekbar1.setMax(300);
        seekbar1.setProgress(0);
        textView1.setTextSize(textsize);
        textView1.setText(1+unit);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress1=i;
                textView1.setText(progress1+unit);
                licz(isRound);
            }
            @Override
            public void onStartTrackingTouch (SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch (SeekBar seekBar){}
        });

        // textView + seekbar from REPS
        seekbar2.setMax(14);
        seekbar2.setProgress(0);
        textView2.setTextSize(textsize);
        textView2.setText(progress2+" "+Reps);
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
        buttonPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress1>seekbar1.getMax()-1) {}
                else progress1+=1;
                seekbar1.setProgress(progress1);
                textView1.setText(progress1+unit);
                licz(isRound);
            }
        });
        buttonMW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress1<1) {}
                else progress1-=1;
                seekbar1.setProgress(progress1);
                textView1.setText(progress1+unit);
                licz(isRound);
            }
        });

        // Toolbar stuff
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        GetFirebasePartnersData();
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
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
    @SuppressLint("SetTextI18n")
    private void licz(boolean isRound) {
        double ciezarNa1;
        ciezar=seekbar1.getProgress();
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

        if(powt == 2)RM2.setText(DefValue);
        else RM2.setText(doCountRound(isRound, 1, ciezarNa1));

        if(powt == 3)RM3.setText(DefValue);
        else RM3.setText(doCountRound(isRound, 2, ciezarNa1));

        if(powt == 4)RM4.setText(DefValue);
        else RM4.setText(doCountRound(isRound, 3, ciezarNa1));

        if(powt == 5)RM5.setText(DefValue);
        else RM5.setText(doCountRound(isRound, 4, ciezarNa1));

        if(powt == 6)RM6.setText(DefValue);
        else RM6.setText(doCountRound(isRound, 5,ciezarNa1));

        if(powt == 7)RM7.setText(DefValue);
        else RM7.setText(doCountRound(isRound, 6, ciezarNa1));

        if(powt == 8)RM8.setText(DefValue);
        else RM8.setText(doCountRound(isRound, 7,ciezarNa1));

        if(powt == 9)RM9.setText(DefValue);
        else RM9.setText(doCountRound(isRound, 8, ciezarNa1));

        if(powt == 10)RM10.setText(DefValue);
        else RM10.setText(doCountRound(isRound, 9,ciezarNa1));

        if(powt == 11)RM11.setText(DefValue);
        else RM11.setText(doCountRound(isRound, 10, ciezarNa1));

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

        //First start of the app
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
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

    private void GetFirebaseAdmobData(){
        Firebase.setAndroidContext(this);
        Firebase adMobFirebase = new Firebase("https://gymcalculator-73017.firebaseio.com/admob");
        adMobFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUnitID = dataSnapshot.getValue(String.class);
                LoadAdMobAd(FirebaseUnitID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void LoadAdMobAd(String unitId){
        View adContainer = findViewById(R.id.adMobView);
        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(unitId);
        ((RelativeLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void GetFirebasePartnersData() {
        Firebase.setAndroidContext(this);
        Firebase partnersFirebase = new Firebase("https://gymcalculator-73017.firebaseio.com/partners_sites");
        partnersFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PartnersUrl = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}