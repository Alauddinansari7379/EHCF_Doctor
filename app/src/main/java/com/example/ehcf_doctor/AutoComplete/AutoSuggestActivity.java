package com.example.ehcf_doctor.AutoComplete;


import android.os.Bundle;
 import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ehcf_doctor.R;

import java.util.ArrayList;
import java.util.List;

public class AutoSuggestActivity extends AppCompatActivity {

    List<String> listOfText = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfText.add("Mat");
        listOfText.add("Banana");
        listOfText.add("Cherry");
        listOfText.add("Date");
        listOfText.add("Grape");
        listOfText.add("Kiwi");
        listOfText.add("Mango");
        listOfText.add("Pear");
        listOfText.add("aa");
        listOfText.add("car");
        listOfText.add("cat");
        listOfText.add("Mango");

//        AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(this,
//                android.R.layout.simple_list_item_1, listOfText);


       // AutoCompleteTextView autotext = findViewById(R.id.autoCompleteTextView);
       // autotext.setAdapter(autoSuggestAdapter);
       // autotext.setThreshold(1);//comparesion start from first character


    }


}
