package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;

import ObjectClasses.Booking;
import ObjectClasses.Peer;
import ObjectClasses.Space;


public class MyBookingsActivity extends AppCompatActivity {
    public final static String BOOKING_DETAIL_MESSAGE = "com.pfp.parkhere.BOOKINGDETAILMESSAGE";
    ListView bookingsView;
    LinkedList<Booking> myBookingsTest;
    //GregorianCalendar[] dateValues;
    String[] viewValues;
    int ownerRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        //get bookings list
        bookingsView = (ListView) findViewById(R.id.bookinglist);

        //HARD CODED BOOKINGs
        //get test bookings, in the future requests all the bookings from database
//        myBookingsTest = new LinkedList<Booking>();
//        mockBookings(myBookingsTest);
        String currentUserEmail =
                ((Global_ParkHere_Application) getApplication()).getCurrentUserObject().getEmailAddress();
//        FirebaseDatabase.getInstance().getReference().

        //'tis a SimpleDateFormat to help display times
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        //Set values displayed as a list
        viewValues = new String[myBookingsTest.size()];
        for(int i = 0; i < myBookingsTest.size();i++){
            GregorianCalendar tempDate = myBookingsTest.get(i).getStart();
            viewValues[i] = timeFormat.format(tempDate.getTime())+ " "
                + tempDate.getDisplayName(Calendar.AM_PM,Calendar.SHORT,Locale.ENGLISH)+ " " +
                    tempDate.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.ENGLISH) + " " +
                    tempDate.get(Calendar.DAY_OF_MONTH);
        }

        //Adapter to dynamically fill listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, viewValues);

        // Assign adapter to ListView
        bookingsView.setAdapter(adapter);

        // ListView Item Click Listener
        bookingsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // ListView Clicked item index
                int itemPosition  = position;
                Context context = view.getContext();

                //New intent to pass to booking details, new Bundle to attach to intent
                Intent intent = new Intent(context, MyBookingsDetailsActivity.class);
                Bundle extras = new Bundle();

                //Generate text for address
                String ad = myBookingsTest.get(position).getSpace().getStreetAddress();
                Address bookingAddress = null;
                try {
                    bookingAddress = new Geocoder(MyBookingsActivity.this).getFromLocationName(ad, 1).get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String addressText = bookingAddress.getAddressLine(0) + "\n" +
                        bookingAddress.getLocality()+ " " + bookingAddress.getAdminArea();
                extras.putString("ADDRESS_TEXT",addressText);

                //Generate text for start and end dates
                GregorianCalendar endTime = myBookingsTest.get(position).getEnd();
                GregorianCalendar startTime = myBookingsTest.get(position).getStart();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                String endTimeText = timeFormat.format(endTime.getTime())+ " "
                        + endTime.getDisplayName(Calendar.AM_PM,Calendar.SHORT,Locale.ENGLISH)+ " " +
                        endTime.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.ENGLISH) + " " +
                        endTime.get(Calendar.DAY_OF_MONTH);
                String startTimeText = timeFormat.format(startTime.getTime())+ " "
                        + startTime.getDisplayName(Calendar.AM_PM,Calendar.SHORT,Locale.ENGLISH)+ " " +
                        startTime.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.ENGLISH) + " " +
                        startTime.get(Calendar.DAY_OF_MONTH);
                extras.putString("START_TIME_TEXT",startTimeText);
                extras.putString("END_TIME_TEXT",endTimeText);
                //Generate text for owner name and email
                extras.putString("OWNER_NAME_TEXT",myBookingsTest.get(position).getSpace().getSpaceName());
                extras.putString("OWNER_EMAIL_TEXT",myBookingsTest.get(position).getSpace().getOwnerEmail());
                //Generate Rating and Review
                //Get owner object to set rating
                String bookingsSpacesOwnerEmail = myBookingsTest.get(position).getSpace().getOwnerEmail();
                FirebaseDatabase.getInstance()
                        .getReference("Peers")
                        .child(Global_ParkHere_Application.reformatEmail(bookingsSpacesOwnerEmail))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.

                                Peer currentUser = dataSnapshot.getValue(Peer.class);
                                ownerRating = ((Peer) currentUser).getOwnerRating();
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });
                extras.putString("SPACE_RATING_TEXT", ownerRating + " ");
                extras.putString("SPACE_REVIEW_TEXT",myBookingsTest.get(position).getSpace().getSpaceReview());

                //Place bundle into intent and start activity
                intent.putExtras(extras);
                context.startActivity(intent);
            }

        });
    }
//    private void mockBookings(LinkedList<Booking> myBookingsTest) {
//        for(int i = 0; i < 11;i++){
//            //create new booking
//            Booking tempBooking = new Booking();
//            //start and end times for booking
//            tempBooking.setStart(new GregorianCalendar(2000+i,1+i,1+i,3+i,1+i));
//            tempBooking.setEnd(new GregorianCalendar(2000+i,1+i,1+i,4+i,1+i));
//            //new space
//            Space tempSpace = new Space();
//            //set address in space
//            tempSpace.setStreetAddress("1 Infinity LoopCupertinoCA");
//            tempSpace.setOwnerEmail("ownerName" + i + "@email.net");
//            //set owner name
//            tempSpace.setSpaceName("FirstName LastName");
//            //set review and rating
//            tempSpace.setSpaceReview("This space got my car towed.");
////            tempSpace.setOwnerRating(3); No need. Owner rating will be retrieved from owner email - owner object
//            //put space in boooking and add booking to linked list
//            tempBooking.setSpace(tempSpace);
//            myBookingsTest.add(tempBooking);
//        }
//    }


    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        FirebaseAuth.getInstance().signOut();
        super.onStop();
    }
}