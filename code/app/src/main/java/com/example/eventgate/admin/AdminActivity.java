package com.example.eventgate.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.eventgate.attendee.Attendee;
import com.example.eventgate.MainActivity;
import com.example.eventgate.attendee.AttendeeActivity;
import com.example.eventgate.attendee.AttendeeEventViewer;
import com.example.eventgate.event.Event;
import com.example.eventgate.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This is the activity for the administrator main menu.
 * It allows the administrator to view and delete any events or users
 */
public class AdminActivity extends AppCompatActivity {
    /**
     * This is an array list that holds events
     */
    ArrayList<Event> eventDataList;
    /**
     * This is the listview that displays the events in the eventDataList
     */
    ListView eventList;
    /**
     * This is an adapter for displaying a list of events
     */
    ArrayAdapter<Event> eventAdapter;
    /**
     * this is an array list that holds attendees
     */
    ArrayList<Attendee> attendeeDataList;
    /**
     * this is the listview that displays the attendees in the attendeeDataList
     */
    ListView attendeeList;
    /**
     * this is an adapter for displaying a list of attendees
     */
    ArrayAdapter<Attendee> attendeeAdapter;
    /**
     * This is the button used to get back to the Main Menu activity
     */
    Button adminActivityBackButton;
    /**
     * holds reference to events collection in the database
     */
    CollectionReference eventsRef;
    /**
     * holds reference to attendees collection in the database
     */
    CollectionReference attendeesRef;

    /**
     * Called when the activity is starting.
     * Initializes the activity layout and views.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // sends admin back to the main menu
        adminActivityBackButton = findViewById(R.id.admin_back_button);
        adminActivityBackButton.setOnClickListener(v -> finish());

        // create the event and attendee lists and set adapters
        createEventList();
        createAttendeeList();

        // get references to firestore events and attendees collections
        eventsRef = MainActivity.db.getEventsRef();
        attendeesRef = MainActivity.db.getAttendeesRef();

        // adds/updates info from the database
        createDBListeners(eventsRef, attendeesRef);

        eventList.setOnItemClickListener((parent, view, position, id) -> {
            Event event = eventDataList.get(position);
            Intent intent = new Intent(AdminActivity.this, AdminEventViewerActivity.class);
            intent.putExtra("eventId", event.getEventId());
            intent.putExtra("name", event.getEventName());
            startActivity(intent);
        });
    }

    /**
     * creates the event list and sets the adapter
     */
    private void createEventList() {
        eventDataList = new ArrayList<>();

        eventList = findViewById(R.id.event_list);

        eventAdapter = new AdminEventListAdapter(this, eventDataList);
        eventList.setAdapter(eventAdapter);
    }

    /**
     * creates the attendee list and sets the adapter
     */
    private void createAttendeeList() {
        attendeeDataList = new ArrayList<>();

        attendeeList = findViewById(R.id.user_list);

        attendeeAdapter = new AdminAttendeeListAdapter(this, attendeeDataList);
        attendeeList.setAdapter(attendeeAdapter);
    }

    /**
     * creates snapshot listeners to add or update info from the database
     * @param eventsRef a reference to the events collection in the database
     * @param attendeesRef a reference to the attendees collection in the database
     */
    private void createDBListeners(CollectionReference eventsRef, CollectionReference attendeesRef) {
        // snapshot listener that adds/updates all the events from the database
        eventsRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
            {
                Event event = new Event((String) doc.getData().get("name"));
                event.setEventId(doc.getId());
                eventDataList.add(event);
                eventAdapter.notifyDataSetChanged();
            }
        });

        // snapshot listener that adds/updates all the attendees/users from the database
        attendeesRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
            {
                Attendee attendee = new Attendee((String) doc.getData().get("name"), (String) doc.getData().get("deviceId"));
                attendeeDataList.add(attendee);
                attendeeAdapter.notifyDataSetChanged();
            }
        });
    }
}