package com.vltale001.kilojoulecounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    public static SharedPreferences sharedPrefs;
    public static SharedPreferences.Editor noteEditor;
    public static ArrayList<View> entries = new ArrayList<>();
    public static ArrayList<View> newEntries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentLinearLayout = findViewById(R.id.entry_list);

        sharedPrefs = getSharedPreferences("entry_list_test", Activity.MODE_PRIVATE);
        noteEditor = sharedPrefs.edit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEntry = new Intent(getApplicationContext(), Calculator.class);
                startActivity(addEntry);
            }
        });
        buildEntryList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (newEntries.size() != 0)
            showNewEntries();
    }

    private void showNewEntries() {
        double total = Double.parseDouble(((TextView)findViewById(R.id.daily_total)).getText().toString());
        for (int i = 0;i<newEntries.size();i++){
            View e = newEntries.get(i);
            if (((ColorDrawable)e.getBackground()).getColor() == getResources().getColor(R.color.colorAccent))
                total += Double.parseDouble(((TextView)((ViewGroup)e).getChildAt(2)).getText().toString());
            else
                total -= Double.parseDouble(((TextView)((ViewGroup)e).getChildAt(2)).getText().toString());
            parentLinearLayout.addView(e,0);
        }
        ((TextView)findViewById(R.id.daily_total)).setText(String.format("%.1f",total));
        entries.addAll(newEntries);
        newEntries = new ArrayList<>();
    }

    @Override
    protected void onStop() {
        new Thread(new Runnable() {
            public void run() {
                int numOfEntries = entries.size();
                noteEditor.putInt("numOfEntries",numOfEntries);
                for (View e:entries){
                    String type;
                    if (((ColorDrawable)e.getBackground()).getColor() == getResources().getColor(R.color.colorAccent))
                        type = "food";
                    else
                        type = "exercise";
                    String description = ((TextView)((ViewGroup) e).getChildAt(0)).getText().toString();
                    String amount = ((TextView)((ViewGroup) e).getChildAt(1)).getText().toString();
                    String total = ((TextView)((ViewGroup) e).getChildAt(2)).getText().toString();


                    noteEditor.putString(entries.indexOf(e)+"",String.format("%s %s %s %s", type, description, amount, total));
                }
                noteEditor.apply();
            }
        }).start();
        super.onStop();
    }

    public void reset(){
        int numOfEntries = sharedPrefs.getInt("numOfEntries",0);
        parentLinearLayout.removeAllViews();
        for(int i = 0; i < numOfEntries;i++){
            noteEditor.putString(i+"","");
        }
        noteEditor.putInt("numOfEntries",0);
        entries = new ArrayList<>();
        newEntries = new ArrayList<>();
        ((TextView)findViewById(R.id.daily_total)).setText("0");
        ((TextView)findViewById(R.id.global_total)).setText("0");
    }

    public void buildEntryList(){
        final int numOfEntries = sharedPrefs.getInt("numOfEntries",0);
        double dailyTotal = 0;
        for (int i = 0; i < numOfEntries; i++) {
            Scanner in = new Scanner(sharedPrefs.getString(i+"",""));
            Boolean isFood = (in.next().equals("food"));
            String description = in.next();
            int amount = Integer.parseInt(in.next());
            double total = Double.parseDouble(in.next().replace(',','.'));
            if (isFood)
                dailyTotal += total;
            else
                dailyTotal -= total;
            addEntry(isFood,description,amount,total);
        }
        ((TextView)findViewById(R.id.daily_total)).setText(String.format("%.1f",dailyTotal));
        showBuiltEntries();
    }

    public void addEntry(boolean isFood,String description,int amount,double total){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup row = (ViewGroup) inflater.inflate(R.layout.entry, null);
        ((TextView)row.getChildAt(0)).setText(description);
        ((TextView)row.getChildAt(1)).setText(""+amount);
        ((TextView)row.getChildAt(2)).setText(""+total);
        if (isFood)
            row.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        else
            row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        entries.add(row);
    }

    public void showBuiltEntries(){
        for (int i = 0;i<entries.size();i++){
             View e = entries.get(i);
            parentLinearLayout.addView(entries.get(i),0);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            reset();
        }

        return super.onOptionsItemSelected(item);
    }
}
