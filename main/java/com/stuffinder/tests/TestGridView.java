package com.stuffinder.tests;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.stuffinder.R;

import java.util.ArrayList;

public class TestGridView extends Activity {

    GridView grid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interieur);




        grid = (GridView)findViewById(R.id.gridView);

        ArrayList<String> liste = new ArrayList<String>();
        liste.add("Telephone");
        liste.add("Clé");
        liste.add("PorteFeuille");

        ArrayAdapter<String> adaptater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liste);

        grid.setAdapter(adaptater);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_grid_view, menu);
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
