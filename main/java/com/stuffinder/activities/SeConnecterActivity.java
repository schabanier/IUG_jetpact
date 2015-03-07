package com.stuffinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.stuffinder.R;
import com.stuffinder.engine.NetworkServiceProvider;
import com.stuffinder.exceptions.AccountNotFoundException;
import com.stuffinder.exceptions.IllegalFieldException;

import com.stuffinder.engine.FieldVerifier;
import com.stuffinder.exceptions.NetworkServiceException;
import com.stuffinder.data.Account;

public class SeConnecterActivity extends Activity {

    EditText editTextIdentifiant;
    EditText editTextMdp;



    public void retourAccueil ( View view) {
        finish();
    }



   public void connexion (View view) {

       String mdp = editTextMdp.getText().toString();
       String identifiant = editTextIdentifiant.getText().toString();
       Intent intent = new Intent ( SeConnecterActivity.this, HomeActivity.class);

       try {
           if (! FieldVerifier.verifyName(identifiant)) { throw new IllegalFieldException(IllegalFieldException.PSEUDO, "entrer identifiant ");}
           if (! FieldVerifier.verifyPassword(mdp) ) { throw new IllegalFieldException(IllegalFieldException.PASSWORD, " Mot de passe trop court ");}
           Account account = NetworkServiceProvider.getNetworkService().authenticate( identifiant, mdp);
           startActivity(intent);
       }
       catch ( NullPointerException e ) {}
       catch ( IllegalFieldException e ) {
           switch(e.getFieldId()) {
               case IllegalFieldException.PSEUDO:
                   Toast.makeText(SeConnecterActivity.this, "Entrer identifiant", Toast.LENGTH_LONG).show();
                   break;
               case IllegalFieldException.PASSWORD:
                   Toast.makeText(SeConnecterActivity.this, "Mot de passe incorect ; il doit contenir au moins 6 caractères", Toast.LENGTH_LONG).show();
                   break;
           } }
       catch (AccountNotFoundException e)   { Toast.makeText(SeConnecterActivity.this, "Vos identifiant et mot de passe sont incorrects",Toast.LENGTH_LONG).show(); }
       catch (NetworkServiceException e) {Toast.makeText(SeConnecterActivity.this, "Erreur de serveur", Toast.LENGTH_LONG).show();}

       }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_se_connecter);

        editTextIdentifiant = (EditText)findViewById(R.id.editTextIdentifiant1);
        editTextMdp = (EditText)findViewById(R.id.editTextMdP1);

        //NetworkServiceProvider.setNetworkService(NetworkServiceEmulator.getInstance());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_se_connecter, menu);
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
