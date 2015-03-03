package com.stuffinder.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView.BufferType;
import android.content.Intent;
import android.view.View;

import com.stuffinder.R;
import com.stuffinder.data.Account;
import com.stuffinder.engine.NetworkServiceProvider;
import com.stuffinder.exceptions.NotAuthenticatedException;


public class InfoPersoActivity extends Activity {



    EditText editTextNom ;
    EditText editTextPrenom;
    EditText editTextIdentifiant;
    EditText editTextEmail ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_perso);

        editTextNom= (EditText)findViewById(R.id.editTextNom);
        editTextPrenom= (EditText)findViewById(R.id.editTextPrenom);
        editTextIdentifiant= (EditText)findViewById(R.id.editTextIdentifiant1);
        editTextEmail= (EditText)findViewById(R.id.editTextEmail);


        try {
            Account account = NetworkServiceProvider.getNetworkService().getCurrentAccount();
            editTextNom.setText(account.getLastName(), BufferType.EDITABLE);
            editTextPrenom.setText(account.getFirstName(), BufferType.EDITABLE);
            editTextEmail.setText(account.getEMailAddress(), BufferType.EDITABLE);
            editTextIdentifiant.setText(account.getPseudo(), BufferType.EDITABLE);


        } catch (NotAuthenticatedException e) {
            Toast.makeText(InfoPersoActivity.this, "Nous n'avons pas réussi à récupérer les informations de votre compte, veuillez réassyer", Toast.LENGTH_LONG).show();
        }
    }



    public void retourConfiguration(View view) {
        Intent intent = new Intent ( InfoPersoActivity.this,ConfigurationActivity.class);
        startActivity(intent);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_perso, menu);
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
