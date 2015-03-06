package com.stuffinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.stuffinder.data.Account;
import com.stuffinder.engine.NetworkServiceProvider;
import com.stuffinder.exceptions.NetworkServiceException;
import com.stuffinder.exceptions.NotAuthenticatedException;


public class InfoPersoActivity extends Activity {


    TextView nomTextView ;
    TextView prenomTextView ;
    TextView idTextView ;
    EditText editTextMail ;
    EditText editTextModp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_perso);

        nomTextView = (TextView)findViewById(R.id.textViewNomVue);
        prenomTextView = (TextView)findViewById(R.id.textViewPrenomVue);
        idTextView = (TextView)findViewById(R.id.textViewIdVue);
        editTextMail= (EditText)findViewById(R.id.editTextMail);
        editTextModp= (EditText)findViewById(R.id.editTextModp);


        Account account = null;
        try {
            account = NetworkServiceProvider.getNetworkService().getCurrentAccount();
            nomTextView.setText(account.getLastName());
            prenomTextView.setText(account.getFirstName());
            idTextView.setText(account.getPseudo());
            editTextMail.setText(account.getEMailAddress(), TextView.BufferType.EDITABLE);
        } catch (NotAuthenticatedException e) {
            e.printStackTrace();
        }

    }



    public void retourConfiguration(View view) {

        try {
            NetworkServiceProvider.getNetworkService().modifyEMailAddress(editTextMail.getText().toString());
            NetworkServiceProvider.getNetworkService().modifyEMailAddress(editTextModp.getText().toString());
            Intent intent = new Intent (InfoPersoActivity.this,ConfigurationActivity.class);
            startActivity(intent);
        } catch (NotAuthenticatedException e) {
            e.printStackTrace();
        } catch (NetworkServiceException e) {
            e.printStackTrace();
        }


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
