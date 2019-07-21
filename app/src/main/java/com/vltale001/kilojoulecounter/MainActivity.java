package com.vltale001.kilojoulecounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    public static SharedPreferences sharedPrefs;
    public static SharedPreferences.Editor noteEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentLinearLayout = findViewById(R.id.entry_list);

        sharedPrefs = getSharedPreferences("entries", Activity.MODE_PRIVATE);
        noteEditor = sharedPrefs.edit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateList();
                Intent addEntry = new Intent(getApplicationContext(), Calculator.class);
                startActivity(addEntry);
            }
        });

        updateList();
    }

    public static void addEntry(String type, String description, int amount,double total){
        int numOfEntries = sharedPrefs.getInt("numOfEntries",0);
        MainActivity.noteEditor.putString(numOfEntries+"",String.format("%s %s %d %f", type, description, amount, total));
        numOfEntries++;
        noteEditor.putInt("numOfEntries",numOfEntries);
        noteEditor.apply();
    }

    /*public void displayEnry(Boolean isFood, String description, int amount, double total) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup row = (ViewGroup) inflater.inflate(R.layout.entry, null);
        ((TextView)row.getChildAt(0)).setText(description);
        ((TextView)row.getChildAt(1)).setText(amount);
        ((TextView)row.getChildAt(2)).setText(""+total);
        parentLinearLayout.addView(row, 0);
    }*/


    private void displayEntry(Boolean isFood, String description, int amount, double total) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup row = (ViewGroup) inflater.inflate(R.layout.entry, null);
        ((TextView)row.getChildAt(0)).setText(description);
        ((TextView)row.getChildAt(1)).setText(""+amount);
        ((TextView)row.getChildAt(2)).setText(""+total);
        if (isFood)
            row.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        else
            row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        parentLinearLayout.addView(row, 0);
    }

    public void updateList(){
        final int numOfEntries = sharedPrefs.getInt("numOfEntries",0);
        parentLinearLayout.removeAllViews();
        double dailyTotal = 0;
        for (int i = 0; i < numOfEntries; i++) {
            Scanner in = new Scanner(sharedPrefs.getString(i+"",""));
            Boolean isFood = (in.next().equals("food")?true:false);
            String description = in.next();
            int amount = Integer.parseInt(in.next());
            double total = Double.parseDouble(in.next().replace(',','.'));
            if (isFood)
                dailyTotal += total;
            else
                dailyTotal -= total;

            displayEntry(isFood,description,amount,total);
        }
        ((TextView)findViewById(R.id.daily_total)).setText(dailyTotal+"");
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
