package com.example.alexg.myapplication1.network;

import com.example.alexg.myapplication1.utils.ForumServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* Синглтон лучше, чем статичные поля
 * https://habr.com/post/129494/ - про разные реализации синглтона
 * */
public class NetworkManager {

    private static final String baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/";
    private Retrofit retrofit = null;
    private ForumServices forumServices = null;

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ForumServices getForumServices() {
        if (forumServices == null) {
            forumServices = getRetrofit().create(ForumServices.class);
        }
        return forumServices;
    }

    /* Методы тоже должны отвечать принципам единой ответственности.
     * В данном случае при добавлении еще одного сервиса нам будет легко переиспользовать метод
     * */
    private Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)                                               //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create())            //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();
        }
        return retrofit;
    }

    private static class InstanceHolder {
        private static NetworkManager INSTANCE = new NetworkManager();
    }
}
