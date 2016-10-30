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

import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;

import ObjectClasses.Space;
import ObjectClasses.SpaceType;


public class MyListedSpacesActivity extends AppCompatActivity {

    public static String LISTING_SPACE_MESSAGE = "com.pfp.parkhere.LISTINGSPACEMESSAGE";
    ListView listedSpacesList;
    LinkedList<Space> MyListedSpaces;
    String [] strFormattedSpaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces);

        //Retrieve the list to add the new elements to
        listedSpacesList = (ListView) findViewById(R.id.listed_spaces_list);
        MyListedSpaces = new LinkedList<Space>();

        //Generate test cases for spaces
        MyListedSpaces = createTestSpaces();

        //Format the information into an array of strings to add to the list
        strFormattedSpaces = new String[MyListedSpaces.size()];
        for (int i = 0; i < MyListedSpaces.size(); i++) {
            Space currSpace = MyListedSpaces.get(i);
            String ad = currSpace.getAddress();
            Address currAddress = null;
            try {
                currAddress = new Geocoder(this).getFromLocationName(ad, 1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            strFormattedSpaces[i] = currSpace.getSpaceName() + "\n" +
                    currAddress.getAddressLine(0) + ",\n" +
                    currAddress.getLocality() + ", " +
                    currAddress.getAdminArea();
        }

        //This adapts the strings to be compatable with the list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, strFormattedSpaces);

        listedSpacesList.setAdapter(arrayAdapter);

        listedSpacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Context context = view.getContext();

                Intent intent = new Intent(context, MyListedSpacesDetailsActivity.class);
                Bundle extras = new Bundle();

                Space chosenSpace = MyListedSpaces.get(position);

                extras.putString("LISTED_SPACE_NAME", chosenSpace.getSpaceName());

                extras.putDouble("LISTED_SPACE_PRICE", chosenSpace.getPricePerHour());

                String spaceAddress = chosenSpace.getAddress();
                extras.putString("LISTED_SPACE_ADDRESS", spaceAddress);

                intent.putExtras(extras);

                context.startActivity(intent);
            }
        });
    }

    private LinkedList<Space> createTestSpaces() {
        LinkedList<Space> retList = new LinkedList<Space>();

        for (int i = 1; i <= 11; i++) {
            Space testSpace = new Space();

            testSpace.setSpaceName("My Test Space "+ i);
            testSpace.setPricePerHour(5*i);
            testSpace.setType(SpaceType.TRUCK);

            testSpace.setAddress("654" + i + " Washington Avenue" + "Los Angeles" + "CA" + "90007");

            retList.add(testSpace);
        }

        return retList;
    }
}