package com.example.alexg.myapplication1.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexg.myapplication1.R;
import com.example.alexg.myapplication1.TranslateClient;
import com.example.alexg.myapplication1.network.NetworkManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexg.myapplication1.utils.Preferences.e;

public class MainActivity extends AppCompatActivity {
    //Лучше инкапсулировать вызов в отдельном классе. Тогда и ключ туда переедит (а лучше в ресурсы strings), и переиспользовать сможешь
    private final String KEY = "trnsl.1.1.20180918T191252Z.53cbfd5ec3f05d84.a986297002dc1543176eed7608cac41660f7724a";
    //Понятные имена. Лучше разделять объявление полей
    private Spinner langFromSpinner;
    private Spinner langToSpinner;
    private String lang;
    private EditText textToTranslate;
    private TextView translatedText;
    private ImageButton revertLang;
    private Button buttonTranslate;
    private Button clearText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        initSpinner();

        //всем можно задать один листенер, так как он не содержит состояние
        View.OnClickListener listener = getListener();
        buttonTranslate.setOnClickListener(listener);
        revertLang.setOnClickListener(listener);
        clearText.setOnClickListener(listener);

    }

    private View.OnClickListener getListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch-case всегда предпочтительнее, так как легче читается.
                // Вообще, лучше создавать для каждого свой листенер. Чтобы не было много кода, почитай про лямбды
                switch (v.getId()) {
                    case R.id.buttonTranslate:
                        getTranslation();
                        break;
                    case R.id.imageButtonRevert:
                        int fromLangId = langFromSpinner.getSelectedItemPosition();
                        langFromSpinner.setSelection(langToSpinner.getSelectedItemPosition());
                        langToSpinner.setSelection(fromLangId);
                        break;
                    case R.id.clear_text:
                        textToTranslate.getText().clear();
                        break;
                    default:
                        throw new IllegalArgumentException("No handler for id " + v.getId());
                }
            }
        };
    }

    private void initSpinner() {
        ArrayList<String> data = new ArrayList<>();
        data.add("en");
        data.add("ru");
        data.add("fr");
        data.add("de");
        data.add("it");

        //лучше давать понятные имена, даже если это локальная переменная. И про generic не забывать
        ArrayAdapter<String> langsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        langsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langFromSpinner.setAdapter(langsAdapter);
        langToSpinner.setAdapter(langsAdapter);
    }

    private void getTranslation() {
        //Почитай про MVP архитектуру.
        NetworkManager.getInstance().getForumServices().getTranslatedText(KEY, getTextFromUi(),
                getFromToLangCode()).enqueue(new Callback<TranslateClient>() {
            @Override
            public void onResponse(Call<TranslateClient> call, Response<TranslateClient> response) {
                if (response.isSuccessful())
                    //Возможно NPE здесь
                    translatedText.setText(response.body().getData().get(0));
                else
                    Toast.makeText(getApplicationContext(), "Write text to translate", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TranslateClient> call, Throwable t) {
                e("this is error" + t.getMessage());
            }
        });
    }

    private String getTextFromUi() {
        //Нужно смотреть подсказки студии. В данном случае text никогда не мог быть null,
        return textToTranslate.getText().toString();
    }

    private String getFromToLangCode() {
        String from = langFromSpinner.getSelectedItem().toString();
        String to = langToSpinner.getSelectedItem().toString();
        //StringBuilder ни к чему. Он полезен только в циклах
        return from + "-" + to;
    }

    /*
     * Посмотри http://jakewharton.github.io/butterknife/.
     * */
    private void initVariables() {
        langFromSpinner = findViewById(R.id.firstValueSpiner);
        langToSpinner = findViewById(R.id.secondValueSpinner);
        textToTranslate = findViewById(R.id.editText);
        translatedText = findViewById(R.id.textView);
        revertLang = findViewById(R.id.imageButtonRevert);
        buttonTranslate = findViewById(R.id.buttonTranslate);
        clearText = findViewById(R.id.clear_text);
        lang = "en-ru";
    }
}