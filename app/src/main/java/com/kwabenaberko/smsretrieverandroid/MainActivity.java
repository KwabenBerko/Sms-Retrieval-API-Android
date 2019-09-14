package com.kwabenaberko.smsretrieverandroid;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SmsRetrieverClient smsRetrieverClient;
    private EditText otpEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        otpEditText = findViewById(R.id.otp_text_view);
        smsRetrieverClient = SmsRetriever.getClient(this);

        startSmsListener();


        List<String> strings = (new AppSignatureHelper(this)).getAppSignatures();
        Log.d("SIGNATURE", String.valueOf(strings.size()));
        for (String string : strings) {
            Log.d("SIGNATURE", string);
        }
        if (1 == 1) {
            return;
        }


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phoneNumber", "+233576232897");
        Ion.with(this)
                .load("https://eef2ee7c.ngrok.io/api/sms")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (result.getHeaders().code() == 200) {
                            startSmsListener();
                        }
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(SmsRetrievedEvent smsRetrievedEvent){
        Log.d("EVENT", "RECEIVED EVENT");
        Log.d("EVENT", String.valueOf(smsRetrievedEvent.isTimeout()));
        Log.d("EVENT", smsRetrievedEvent.getSmsMessage());
        if (smsRetrievedEvent.isTimeout()){
            Toast.makeText(this, "Timeout!", Toast.LENGTH_SHORT).show();
            return;
        }

        String otp = StringUtils.substringAfterLast(smsRetrievedEvent.getSmsMessage(), "is").replace(":", "").trim().substring(0, 4);
        Log.d("EVENT", otp);
        otpEditText.setText(otp);
        startSmsListener();

    }

    private void startSmsListener() {
        smsRetrieverClient.startSmsRetriever().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Starting Sms Retriever", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
