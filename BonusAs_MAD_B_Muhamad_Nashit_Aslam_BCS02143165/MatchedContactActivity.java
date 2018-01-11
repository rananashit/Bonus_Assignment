package com.example.dell.bonusassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchedContactActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Contacts> list;
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_contact);
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        request();
    }

    private void request() {
        Call<List<Contacts>> call = ServiceSingleton.getInstance().getcontactMatch();
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if (response.isSuccessful()) {
                    for (Contacts contact : response.body()) {
                        list.add(contact);
                    }
                    setRecyclerView();

                } else if (response.code() != 200) {

                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                Log.d(MainActivity.TAG, "onFailure: ");
            }
        });
    }

    public void setRecyclerView() {
        adapter = new ContactsAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}
