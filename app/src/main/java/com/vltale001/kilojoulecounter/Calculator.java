package com.vltale001.kilojoulecounter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Spinner foodSpinner = findViewById(R.id.food_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array_food, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodSpinner.setAdapter(adapter);
        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateFoodDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner exerciseSpinner = findViewById(R.id.exercise_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array_exercise, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);
        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateExerciseDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText foodInputText = findViewById(R.id.food_amount_text);
        EditText exerciseInputText = findViewById(R.id.exercise_amount_text);
        TextWatcher foodTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateFoodDisplay();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        TextWatcher exerciseTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateExerciseDisplay();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        foodInputText.addTextChangedListener(foodTextWatcher);
        exerciseInputText.addTextChangedListener(exerciseTextWatcher);

        Button foodButton = findViewById(R.id.food_button);
        foodButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                //String type = "food";
                String description = ((Spinner)findViewById(R.id.food_spinner)).getSelectedItem().toString();
                int amount = Integer.parseInt(((EditText)findViewById(R.id.food_amount_text)).getText().toString());
                String totalDisplay = ((TextView)findViewById(R.id.food_total_display)).getText().toString();

                if (!totalDisplay.equals(".")){
                    double total = Double.parseDouble(totalDisplay.substring(0,totalDisplay.length()-2).replace(',','.'));
                    addEntry(true,description,amount,total);

                } else {
                    Toast.makeText(getApplicationContext(),R.string.add_error,Toast.LENGTH_LONG).show();
                }
            }
        });

        Button exerciseButton = findViewById(R.id.exercise_button);
        exerciseButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //String type = "exercise";
                String description = ((Spinner)findViewById(R.id.exercise_spinner)).getSelectedItem().toString();
                int amount = Integer.parseInt(((EditText)findViewById(R.id.exercise_amount_text)).getText().toString());
                String totalDisplay = ((TextView)findViewById(R.id.exercise_total_display)).getText().toString();

                if (!totalDisplay.equals(".")){
                    double total = Double.parseDouble(totalDisplay.substring(0,totalDisplay.length()-2).replace(',','.'));
                    addEntry(false,description,amount,total);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.add_error,Toast.LENGTH_LONG).show();
                }
            }
        });
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
        MainActivity.newEntries.add(row);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void updateFoodDisplay(){
        try {
            int foodAmount = Integer.parseInt(((EditText)findViewById(R.id.food_amount_text)).getText().toString());

            int foodNum = ((Spinner)findViewById(R.id.food_spinner)).getSelectedItemPosition();

            Double foodValue = Double.parseDouble(getResources().getStringArray(R.array.value_array_food)[foodNum]);

            TextView foodView = findViewById(R.id.food_total_display);
            if (foodAmount*foodValue > R.integer.max_kjperday){
                foodView.setText(".");
                Toast.makeText(this, "Unreasonable KJ intake",
                        Toast.LENGTH_LONG).show();
            } else {
                foodView.setText(String.format("%.1f%s",foodAmount*foodValue,"kj"));
            }
        } catch (NumberFormatException e){
            TextView foodView = findViewById(R.id.food_total_display);
            foodView.setText(".");
        }
    }

    public void updateExerciseDisplay(){
        try {
            int exerciseAmount = Integer.parseInt(((EditText)findViewById(R.id.exercise_amount_text)).getText().toString());

            int exerciseNum = ((Spinner)findViewById(R.id.exercise_spinner)).getSelectedItemPosition();

            Double exerciseValue = Double.parseDouble(getResources().getStringArray(R.array.value_array_exercise)[exerciseNum]);

            TextView exerciseView = findViewById(R.id.exercise_total_display);

            if(exerciseAmount*exerciseValue/60*70 > 5000){
                exerciseView.setText(".");
                Toast.makeText(this, "Unreasonable Exercise Amount", Toast.LENGTH_SHORT).show();
            } else {
                exerciseView.setText(String.format("%.1f%s",exerciseAmount*exerciseValue/60*70,"kj"));
            }
        } catch (NumberFormatException e){
            TextView exerciseView = findViewById(R.id.exercise_total_display);
            exerciseView.setText(".");
        }
    }
}
