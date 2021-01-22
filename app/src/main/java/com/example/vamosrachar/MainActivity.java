package com.example.vamosrachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextWatcher, OnInitListener {

    EditText txtGroup;
    EditText txtMoney;

    TextView txtResult;

    FloatingActionButton butShare;
    FloatingActionButton butSpeak;

    TextToSpeech ttsPlayer;

    private int MY_DATA_CHECK_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGroup = (EditText) findViewById(R.id.txtGroup);
        txtMoney = (EditText) findViewById(R.id.txtMoney);
        txtResult = (TextView) findViewById(R.id.txtResult);

        butShare = (FloatingActionButton) findViewById(R.id.butShare);
        butShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharingIntent();
            }
        });

        butSpeak = (FloatingActionButton) findViewById(R.id.butSpeak);
        butSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttsPlayer != null) {
                    ttsPlayer.speak("Vamos rachar! A conta por pessoa é "
                            + txtResult.getText().toString(),
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "ID1");
                }
            }
        });

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        txtGroup.addTextChangedListener(this);
        txtMoney.addTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            int groupValue = Integer.parseInt(txtGroup.getText().toString());
            float moneyValue = Float.parseFloat(txtMoney.getText().toString());

            float result = moneyValue / groupValue;

            DecimalFormat moneyFormat = new DecimalFormat("0.00");

            if (groupValue == 0) {
                txtResult.setText("R$0.00");
            } else {
                txtResult.setText("R$" + moneyFormat.format(result));
            }
        } catch (Exception e) {
            txtResult.setText("R$0.00");
        }
     }

     public void sharingIntent() {
         Intent shareIntent = new Intent(Intent.ACTION_SEND);
         shareIntent.setType("text/plain");
         shareIntent.putExtra(
                 Intent.EXTRA_TEXT,
                 "Vamos rachar! A conta por pessoa é " + txtResult.getText().toString()
         );
         startActivity(shareIntent);
     }

     protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == MY_DATA_CHECK_CODE) {
             if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                 ttsPlayer = new TextToSpeech(this, this);
             } else {
                 Intent installTTSIntent = new Intent();
                 installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                 startActivity(installTTSIntent);
             }
         }
     }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS Ativado", Toast.LENGTH_LONG).show();
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sem TTS Habilitado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}