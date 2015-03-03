package com.stuffinder.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.stuffinder.R;

public class ModifTagActivity extends Activity {

    private ListView mListModif = null;

    private Button mSend = null;

    private String[] mModif = {"Clefs", "TvCommande", "Lunettes", "Portefeuile"};

    public void goToFiche(View view){
        Intent intent = new Intent(ModifTagActivity.this, InfoTagActivity.class);
        startActivity(intent);}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_tag);

        mListModif = (ListView) findViewById(R.id.listModif);

        mSend = (Button) findViewById(R.id.send);

        mListModif.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mModif));



    }
}