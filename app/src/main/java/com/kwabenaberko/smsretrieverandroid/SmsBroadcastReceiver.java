package com.kwabenaberko.smsretrieverandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import org.greenrobot.eventbus.EventBus;


public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = SmsBroadcastReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive Called!");
        if(SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                Status status = (Status) bundle.get(SmsRetriever.EXTRA_STATUS);
                SmsRetrievedEvent event = new SmsRetrievedEvent();
                switch (status.getStatusCode()){
                    case CommonStatusCodes.SUCCESS:
                        String message = (String) bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        Log.d(TAG, message);
                        event.setSmsMessage(message);
                        break;

                    case CommonStatusCodes.TIMEOUT:
                        Log.d(TAG, "Timeout");
                        event.setTimeout(true);
                        break;
                }

                EventBus.getDefault().post(event);
            }
        }
    }
}
