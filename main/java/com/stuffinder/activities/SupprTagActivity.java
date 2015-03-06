package com.stuffinder.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.stuffinder.R;
import com.stuffinder.data.Tag;
import com.stuffinder.engine.NetworkServiceProvider;
import com.stuffinder.exceptions.NetworkServiceException;
import com.stuffinder.exceptions.NotAuthenticatedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SupprTagActivity extends Activity {

    private ListView mListSuppr = null;

    private Button mSend = null;
    private static List<Tag> arrayAdapter = new ArrayList<>();

    public void retour4 (View view) {
        Intent intentRetour = new Intent (SupprTagActivity.this, TagsActivity.class);
        startActivity(intentRetour); }

    public void goToTags(View view){

        SparseBooleanArray tab = mListSuppr.getCheckedItemPositions() ;

        for(int i=0; i<arrayAdapter.size(); i++) {
           if (tab.get(i) == true)
           { try {
                   NetworkServiceProvider.getNetworkService().removeTag(arrayAdapter.get(i));
               } catch (NotAuthenticatedException e) {
                   e.printStackTrace();
               } catch (NetworkServiceException e) {
                   e.printStackTrace();
               }
            }
        }


        Intent intent = new Intent(SupprTagActivity.this, TagsActivity.class);
        startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppr_tag);

        mListSuppr = (ListView) findViewById(R.id.listSuppr);

        mSend = (Button) findViewById(R.id.send);

        ArrayAdapter<Tag> tagArrayAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_multiple_choice);
        tagArrayAdapter.addAll(arrayAdapter);

        mListSuppr.setAdapter(tagArrayAdapter);




    }

    public static void ChangeTagsList(List<Tag> list)
    {
        arrayAdapter.clear();

        arrayAdapter.addAll(list);

        Collections.sort(arrayAdapter, new Comparator<Tag>() {
            @Override
            public int compare(Tag lhs, Tag rhs) {
                return lhs.getObjectName().compareTo(rhs.getObjectName());
            }
        });
    }

}

