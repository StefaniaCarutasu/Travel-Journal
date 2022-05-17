package com.android.traveljournalapp;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebasePushNotifications extends FirebaseMessagingService {

    public FirebasePushNotifications() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);


    }
}
