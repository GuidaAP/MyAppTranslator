package com.example.alexg.myapplication1.utils;

import com.example.alexg.myapplication1.TranslateClient;
import com.example.alexg.myapplication1.model.LangCode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForumServices {
    //тут лишний "?". ? отделяет основной адрес от параметров. Ретрофит сам его добавляет
    @GET("translate?")
    Call<TranslateClient> getTranslatedText(@Query("key")String key, @Query("text") String text, @Query("lang") String lang);

    @GET ("getLangs?")
    Call<LangCode> getLanguages(@Query("key")String key, @Query("ui") String langs);

}
