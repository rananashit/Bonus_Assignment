package com.example.dell.bonusassignment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MYTAG";
    RecyclerView recyclerView;
    ArrayList<Contacts> list;
    ContactsAdapter adapter;
    Button btnMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.phonerecycler);
        btnMatch = findViewById(R.id.btnMatch);
        list = readContacts();
        adapter = new ContactsAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        onMatchButton();

    }

    private void onMatchButton() {
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<List<Contacts>> callList = ServiceSingleton.getInstance().contactMatch(list);
                callList.enqueue(new Callback<List<Contacts>>() {
                    @Override
                    public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: ");
                            list.clear();
                            for (Contacts contact : response.body()) {
                                list.add(contact);
                                Log.d(TAG, "onResponse: number: " + contact.getNumber() + " \n");
                            }
                            startActivity(new Intent(MainActivity.this, MatchedContactActivity.class));
                        } else if (response.code() != 200) {
                            Log.d(TAG, "onResponse:else code " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Contacts>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });

            }
        });

    }

    public ArrayList<Contacts> readContacts() {
        ArrayList<Contacts> contactList = new ArrayList<Contacts>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC "); // Return

        // Move cursor at starting
        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
                // data of
                // contacts
                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);// Retrun data cusror represntative to
                // contact ID

                // Strings to get all details
                String displayName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                String contactNumbers = "";


                // Now start the cusrsor
                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// get
                    // the
                    // contact
                    // name
                    do {

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers = homePhone;
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers = workPhone;
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers = mobilePhone;
                                    break;
                            }
                        }


                    } while (dataCursor.moveToNext()); // Now move to next
                    // cursor

                    contactList.add(new Contacts(
                            displayName + "", contactNumbers + ""));// Finally add
                }

            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }
}
