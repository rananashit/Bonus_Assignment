package com.example.dell.bonusassignment;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by dell on 10/30/2017.
 */

public interface ServiceClient {


    @POST("/api/contacts")
    Call<List<Contacts>> contactMatch(@Body ArrayList<Contacts> list);

    @GET("/api/contacts")
    Call<List<Contacts>> getcontactMatch();

}
