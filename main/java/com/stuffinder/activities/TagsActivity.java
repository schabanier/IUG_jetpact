package com.stuffinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.stuffinder.R;

public class TagsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
    }
    public void goToAjout(View view){
        Intent intentAjout = new Intent( TagsActivity.this, AjoutTagActivity.class);
        startActivity(intentAjout);}

    public void goToModif (View view) {
        Intent intentModif = new Intent ( TagsActivity.this, ModifTagActivity.class);
        startActivity(intentModif);
    }

    public void goToSuppr (View view) {
        Intent intentSuppr = new Intent ( TagsActivity.this, SupprTagActivity.class);
        startActivity(intentSuppr);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
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
