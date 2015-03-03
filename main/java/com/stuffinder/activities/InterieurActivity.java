package com.stuffinder.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;


import com.stuffinder.R;
import com.stuffinder.data.Account;
import com.stuffinder.data.Tag;
import com.stuffinder.engine.NetworkServiceProvider;
import com.stuffinder.exceptions.NotAuthenticatedException;

import java.util.ArrayList;
import java.util.List;


public class InterieurActivity extends Activity {

    GridView grid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interieur);


        grid = (GridView)findViewById(R.id.gridView);


        try {
            Account account = NetworkServiceProvider.getNetworkService().getCurrentAccount();
            List<Tag> tags = account.getTags();
            ArrayList<String> liste = new ArrayList<String>();
            int size = tags.size();

            for ( int i=0; i<size; i++ ) {
               String name = tags.get(i).getObjectName();
               liste.add(name);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.list_content, liste);
            grid.setAdapter(adapter);
        }

        catch ( NotAuthenticatedException e ) {
            Toast.makeText(InterieurActivity.this, "Nous n'avons pas réussi à récupérer les informations de votre compte, veuillez réassyer",Toast.LENGTH_LONG).show();}


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interieur, menu);
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
