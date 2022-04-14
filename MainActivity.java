package com.example.text_to_speech;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText usertxt;
    Button btn;
    ImageView imageView;
    TextToSpeech textToSpeech;
    String userToSpeak;

    private final int REQUEST_CODE =100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usertxt = findViewById(R.id.usertext);
        btn =findViewById(R.id.btn);
        imageView = findViewById(R.id.img_mic);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.CANADA);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userToSpeak = usertxt.getText().toString();
                if (userToSpeak.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Text!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), userToSpeak,Toast.LENGTH_SHORT).show();
                    textToSpeech.speak(userToSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                try {
                    startActivityForResult(intent, REQUEST_CODE);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Sorry! Your Device Don't Support Speech Input...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:{
                if (resultCode == RESULT_OK && null != data){
                    ArrayList <String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    usertxt.setText(res.get(0));
                    Toast.makeText(getApplicationContext(), res.get(0), Toast.LENGTH_SHORT).show();
                    textToSpeech.speak(res.get(0),TextToSpeech.QUEUE_FLUSH, null);
                } break;
            }
        }
    }

    public void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}