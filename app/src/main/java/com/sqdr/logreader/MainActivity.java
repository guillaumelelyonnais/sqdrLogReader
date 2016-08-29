package com.sqdr.logreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String mLogFile;
    private LogAdapter mLogAdapter;
    private SharedPreferences mSharedPreferences;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_log);

        mSharedPreferences = getPreferences(MODE_PRIVATE);

        // Get extra
        mLogFile = null;
        if (getIntent().hasExtra("logfile")) {
            mLogFile = getIntent().getStringExtra("logfile");
        }

        // read log file
        if(mLogFile != null){
            readLogFile(mLogFile);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettingsDialog();
                return true;

            case R.id.action_open:
                openFileDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void openFileDialog() {
        Intent intent = new Intent(this, FileManager.class);
        intent.putExtra("path", "/storage/sdcard0/Hexoplus");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("settings", "pref_show_time: " + mSharedPreferences.contains("pref_show_time"));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void readLogFile(String fileName){

        //Get the text file
        File file = new File(fileName);

        //Read text from file
        StringBuilder text = new StringBuilder();
        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            e.printStackTrace();
        }

        mLogAdapter = new LogAdapter(this, mSharedPreferences, lines);
        mListView.setAdapter(mLogAdapter);
    }

    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View view = View.inflate(this, R.layout.settings_layout, null);
        builder.setView(view);
        builder.setTitle("Settings");

        final CheckBox checkBox_show_log_flight = (CheckBox) view.findViewById(R.id.checkBox_show_log_flight);
        checkBox_show_log_flight.setText("Show flight log");
        checkBox_show_log_flight.setChecked(mSharedPreferences.getBoolean("pref_show_flight_log", true));

        final CheckBox checkBox_show_timestamp = (CheckBox) view.findViewById(R.id.checkBox_show_timestamp);
        checkBox_show_timestamp.setText("Show timestamp");
        checkBox_show_timestamp.setChecked(mSharedPreferences.getBoolean("pref_show_timestamp", true));

        final CheckBox checkBox_show_title = (CheckBox) view.findViewById(R.id.checkBox_show_title);
        checkBox_show_title.setText("Show title");
        checkBox_show_title.setChecked(mSharedPreferences.getBoolean("pref_show_title", true));

        final CheckBox checkBox_show_location = (CheckBox) view.findViewById(R.id.checkBox_show_location);
        checkBox_show_location.setText("Show type");
        checkBox_show_location.setChecked(mSharedPreferences.getBoolean("pref_show_type", true));

        builder.setPositiveButton("Save and close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("pref_show_flight_log", checkBox_show_log_flight.isChecked());
                editor.putBoolean("pref_show_timestamp", checkBox_show_timestamp.isChecked());
                editor.putBoolean("pref_show_title", checkBox_show_title.isChecked());
                editor.putBoolean("pref_show_type", checkBox_show_location.isChecked());
                editor.commit();

                // read log file
                if(mLogFile != null){
                    readLogFile(mLogFile);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void refreshUi() {
    }


}
