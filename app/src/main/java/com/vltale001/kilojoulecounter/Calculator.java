package com.vltale001.kilojoulecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


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

   /* public void updateDisplays(){
        try {
            int foodAmount = Integer.parseInt(((EditText)findViewById(R.id.food_amount_text)).getText().toString());
            int exerciseAmount = Integer.parseInt(((EditText)findViewById(R.id.exercise_amount_text)).getText().toString());

            int foodNum = ((Spinner)findViewById(R.id.food_spinner)).getSelectedItemPosition();
            int exerciseNum = ((Spinner)findViewById(R.id.exercise_spinner)).getSelectedItemPosition();

            Double foodValue = Double.parseDouble(getResources().getStringArray(R.array.value_array_food)[foodNum]);
            Double exerciseValue = Double.parseDouble(getResources().getStringArray(R.array.value_array_exercise)[exerciseNum]);

            TextView foodView = findViewById(R.id.food_total_display);
            TextView exerciseView = findViewById(R.id.exercise_total_display);
            foodView.setText(String.format("%.1f%s",foodAmount*foodValue,"kj"));
            exerciseView.setText(String.format("%.1f%s",exerciseAmount*exerciseValue*70,"kj"));
        } catch (NumberFormatException e){
            TextView foodView = findViewById(R.id.food_total_display);
            TextView exerciseView = findViewById(R.id.exercise_total_display);
            foodView.setText(".");
            exerciseView.setText(".");
        }
    }*/

    public void updateFoodDisplay(){
        try {
            int foodAmount = Integer.parseInt(((EditText)findViewById(R.id.food_amount_text)).getText().toString());

            int foodNum = ((Spinner)findViewById(R.id.food_spinner)).getSelectedItemPosition();

            Double foodValue = Double.parseDouble(getResources().getStringArray(R.array.value_array_food)[foodNum]);

            TextView foodView = findViewById(R.id.food_total_display);
            foodView.setText(String.format("%.1f%s",foodAmount*foodValue,"kj"));
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
            exerciseView.setText(String.format("%.1f%s",exerciseAmount*exerciseValue/60*70,"kj"));
        } catch (NumberFormatException e){
            TextView exerciseView = findViewById(R.id.exercise_total_display);
            exerciseView.setText(".");
        }
    }
}
