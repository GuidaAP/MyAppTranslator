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

    private final String KEY = "trnsl.1.1.20180918T191252Z.53cbfd5ec3f05d84.a986297002dc1543176eed7608cac41660f7724a";
    private Spinner listLang1,
            listLang2;
    private String lang;
    private EditText textToTranslate;
    private TextView translatedText;
    private ImageButton revertLang;
    private Button buttonTranslate,
            clearText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        initSpinner();

        buttonTranslate.setOnClickListener(getListener());
        revertLang.setOnClickListener(getListener());
        clearText.setOnClickListener(getListener());

    }

    private View.OnClickListener getListener() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == buttonTranslate.getId()) {
                    getTranslation();
                } else if (id == revertLang.getId()) {
                    int fromLangId = listLang1.getSelectedItemPosition();
                    listLang1.setSelection(listLang2.getSelectedItemPosition());
                    listLang2.setSelection(fromLangId);
                } else if (id == clearText.getId()) {
                    textToTranslate.getText().clear();

                }
            }
        };
        return listener;
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
        listLang1.setAdapter(langsAdapter);
        listLang2.setAdapter(langsAdapter);
    }

    private void getTranslation() {

        NetworkManager.getInstance().getForumServices().getTranslatedText(KEY, getTextFromUi(),
                getFromToLangCode()).enqueue(new Callback<TranslateClient>() {
            @Override
            public void onResponse(Call<TranslateClient> call, Response<TranslateClient> response) {
                if (response.isSuccessful())
                    translatedText.setText(response.body().getData().get(0));
                else
                    //getTextFromUi();
                    Toast.makeText(getApplicationContext(), "Write text to translate", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "Smth went wrong", Toast.LENGTH_SHORT).show();
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
        String from = listLang1.getSelectedItem().toString();
        String to = listLang2.getSelectedItem().toString();
        //StringBuilder ни к чему. Он полезен только в циклах
        return from + "-" + to;
    }

    /*
     * Посмотри http://jakewharton.github.io/butterknife/.
     * */
    private void initVariables() {
        listLang1 = findViewById(R.id.firstValueSpiner);
        listLang2 = findViewById(R.id.secondValueSpinner);
        textToTranslate = findViewById(R.id.editText);
        translatedText = findViewById(R.id.textView);
        revertLang = findViewById(R.id.imageButtonRevert);
        buttonTranslate = findViewById(R.id.buttonTranslate);
        clearText = findViewById(R.id.clear_text);
        lang = "en-ru";
    }
}