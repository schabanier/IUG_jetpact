package com.stuffinder.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;

import com.stuffinder.R;


public class HomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void goToInterieur(View view){
        Intent intentInterieur = new Intent(HomeActivity.this, InterieurActivity.class);
        startActivity(intentInterieur);}

    public void goToExterieur(View view){
        Intent intentExt = new Intent(HomeActivity.this, ExterieurActivity.class);
        startActivity(intentExt);}

    public void goToConfiguration(View view){
        Intent intentConf = new Intent(HomeActivity.this, ConfigurationActivity.class);
        startActivity(intentConf);}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
