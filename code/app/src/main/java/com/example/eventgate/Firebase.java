package com.example.eventgate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class represents Firebase
 * It allows the app to access the Firestore database and use Firebase authentication
 */

public class Firebase {
    /**
     * this holds the firestore database
     */
    private final FirebaseFirestore db;
    /**
     * this holds an instance of FirebaseAuth
     */
    private final FirebaseAuth mAuth;
    /**
     * this holds an instance of the Firebase Cloud Messaging
     */
    private final FirebaseMessaging fcm;
    /**
     * this is the reference to the events collection in the database
     */
    private final CollectionReference eventsRef;

    /**
     * this is the reference to the attendees collection in the database
     */
    private final CollectionReference attendeesRef;

    private final CollectionReference fcmTokensRef;
    private FirebaseUser currentUser;

    /**
     * this constructs a new Firebase object
     */
    public Firebase() {
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.fcm = FirebaseMessaging.getInstance();
        this.eventsRef = db.collection("events");
        this.attendeesRef = db.collection("attendees");
        this.fcmTokensRef = db.collection("fcmTokens");
        this.currentUser = null;
    }

    /**
     * this gets an instance of the firestore database
     * @return an instance of the database
     */
    public FirebaseFirestore getDB() {
        return this.db;
    }

    /**
     * this gets an instance of firebase authentication
     * @return an of FirebaseAuth
     */
    public FirebaseAuth getmAuth() {
        return this.mAuth;
    }

    /**
     * this gets a reference to the events collection in the database
     * @return a collection reference to the events collection
     */

    public FirebaseMessaging getFcm() {
        return this.fcm;
    }

    public CollectionReference getEventsRef() {
        return this.eventsRef;
    }

    /**
     * this gets a reference to the attendees collection in the database
     * @return a collection reference to the attendees collection
     */
    public CollectionReference getAttendeesRef() {
        return this.attendeesRef;
    }
  
    public CollectionReference getFcmTokensRef() {
        return this.fcmTokensRef;
    }

    public void setUser(FirebaseUser user) {
        currentUser = user;
    }
    public FirebaseUser getUser() {
        return currentUser;
    }
}
