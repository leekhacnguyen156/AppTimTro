package com.example.timtro.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
            private static final String base_url = "http://192.168.43.35/webtimtro/Server/";
            private static Retrofit retrofit;

            public static Retrofit getAPIClient(){
                if(retrofit == null){
                    retrofit = new Retrofit.Builder()
                            .baseUrl(base_url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
                return retrofit;
        }
}
