package com.stuffinder.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.stuffinder.R;


public class SupprTagActivity extends Activity {


    private ListView mListSuppr = null;

    private Button mSend = null;

    public void goToTags(View view){
        Intent intent = new Intent(SupprTagActivity.this, TagsActivity.class);
        startActivity(intent);}

    private String[] mSuppr = new String[]{"Clefs", "TvCommande", "Lunettes", "Portefeuile"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppr_tag);


        mListSuppr = (ListView) findViewById(R.id.listSuppr);
        mSend = (Button) findViewById(R.id.send);


        mListSuppr.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, mSuppr));



    }
}