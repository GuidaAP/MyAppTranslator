package com.example.alexg.myapplication1;

import com.example.alexg.myapplication1.utils.ForumServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication  {

    private static Retrofit retrofit = null;
    private static ForumServices forumServices = null;
    private static final String baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/";

    public static ForumServices getForumServices() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)                                               //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create())            //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();
            forumServices = retrofit.create(ForumServices.class);
        }
        return forumServices;
    }

}
