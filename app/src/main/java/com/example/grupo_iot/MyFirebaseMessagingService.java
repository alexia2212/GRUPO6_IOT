package com.example.grupo_iot;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message){

        super.onMessageReceived(message);
    }

}
