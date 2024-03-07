package com.example.eventgate.event;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.eventgate.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is used to add, remove, and retrieve event data from the database
 */
public class EventDB {
    /**
     * An instance of the Firebase Firestore database
     */
    private FirebaseFirestore db;
    /**
     * The collection for the events collection in the database
     */
    private CollectionReference collection;
    /**
     * The TAG for logging
     */
    final String TAG = "EventDB";

    /**
     * Constructs a new EventDB
     */
    public EventDB() {
        db = MainActivity.db.getDB();
        collection = MainActivity.db.getEventsRef();
    }

    /**
     * Adds an event to the firebase database
     *
     * @param event the event to add
     */
    public void addEvent(com.example.eventgate.event.Event event) {
        String eventId = collection.document().getId();
        event.setEventId(eventId);
        HashMap<String, String> data = new HashMap<>();
        data.put("eventId", event.getEventId());
        data.put("name", event.getEventName());
        collection
                .document(eventId)
                .set(data)
                .addOnSuccessListener(unused -> Log.d(TAG, "Event has been added successfully!"))
                .addOnFailureListener(e -> Log.d(TAG, "Event could not be added!" + e));
    }

    /**
     * Adds an organizer event to the database.
     *
     * @param event         The event object containing details of the event.
     * @param eventQRBitmap The bitmap image of the event's QR code.
     */
    public void AddOrganizerEvent(com.example.eventgate.event.Event event, Bitmap eventQRBitmap) {
        // Convert bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        eventQRBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        // Convert byte array to list of integers
        List<Integer> byteArrayAsList = new ArrayList<>();
        for (byte b : byteArray) {
            byteArrayAsList.add((int) b);
        }

        String eventId = collection.document().getId();
        event.setEventId(eventId);

        String[] attendees = new String[0];

        HashMap<String, Object> data = new HashMap<>();
        data.put("eventId", event.getEventId());
        data.put("name", event.getEventName());
        data.put("checkInQRCode", byteArrayAsList.toString());
        data.put("organizer", ""); // Set organizer field to blank
        data.put("attendees", attendees); // Set attendees field to blank

        collection
                .document(eventId)
                .set(data)
                .addOnSuccessListener(unused -> Log.d(TAG, "Event has been added successfully!"))
                .addOnFailureListener(e -> Log.d(TAG, "Event could not be added!" + e));
    }

    /**
     * Removes an event from the database
     *
     * @param event the event to remove
     */
    public void removeEvent(com.example.eventgate.event.Event event) {
        String eventId = event.getEventId();
        collection.document(eventId)
                .delete()
                .addOnSuccessListener(unused -> Log.d(TAG, "Event has been deleted successfully"))
                .addOnFailureListener(e -> Log.d(TAG, "Error deleting event" + e));
    }
}