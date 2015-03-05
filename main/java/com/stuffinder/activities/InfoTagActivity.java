package com.stuffinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.stuffinder.R;
import com.stuffinder.data.Tag;
import com.stuffinder.engine.NetworkServiceProvider;
import com.stuffinder.exceptions.NetworkServiceException;
import com.stuffinder.exceptions.NotAuthenticatedException;

public class InfoTagActivity extends Activity {

    EditText EditTextNom ;
    EditText EditTextImage ;

    private static Tag tagModif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tag);

        EditTextNom = (EditText)findViewById(R.id.editTextNom) ;
        EditTextImage = (EditText)findViewById(R.id.editTextImage) ;


            EditTextNom.setText(tagModif.getObjectName(), TextView.BufferType.EDITABLE);
            EditTextImage.setText(tagModif.getObjectImageName(), TextView.BufferType.EDITABLE);

    }

    public void creerCompte (View view) {

        try {
            NetworkServiceProvider.getNetworkService().modifyObjectName(tagModif, EditTextNom.getText().toString()) ;
            NetworkServiceProvider.getNetworkService().modifyObjectImage(tagModif, EditTextImage.getText().toString()) ;
            Intent intent = new Intent (InfoTagActivity.this, TagsActivity.class);
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

    public static void ChangeTag(Tag tag)
    {
        tagModif = tag ;

    }

}
