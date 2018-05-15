package com.example.paras.assignemnt_191;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataTransferInterface {
    //API link of the weather info which we get online.
    private static final String URL="http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";
    // listView to display different cities weather details.
    ListView listView;
    // ArrayList to store the data of the array in the form of an array.
    ArrayList<String> arrayList;
    // ArrayAdapter to connect the ArrayList to the listView.
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //onCreate method.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initializing listView by referencing to its id in activity_main.xml
        listView = (ListView) findViewById(R.id.myList);
        // arrayList initialization.
        arrayList = new ArrayList<>();
        // adapter initialization referring to arrayList.
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        // attaching the arrayList with the listView using the arrayAdapter.
        listView.setAdapter(arrayAdapter);

        if (connectionIsEstablished()){
            FetchData fetchData = new FetchData(MainActivity.this, URL, this);
            fetchData.execute();
        }else{
            Toast.makeText(this, "Make sure your internet is connected.", Toast.LENGTH_SHORT).show();
        }
    }

    // method to check whether the connection is established or not.
    private boolean connectionIsEstablished() {
        // initially we assume that the connection is not established.
        boolean isConnected = false;
        // getting the connectivityManager to check the connection.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // getting the information about the network that is established.
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // if networkInfo has some information about the connectivity then,
        if (networkInfo != null){
            // set the connection as true.
            isConnected = true;
        }
        // return the status of connectivity.
        return isConnected;
    }

    // data recieved from the FetchData class using the interface DataTransferInterface.
    @Override
    public void dataHere(String data) {

        // initializing the variables to store data from the extracted JSON.
        String idd = null;
        String main = null;
        String description = null;
        String icon = null;

        String name;
        String id ;
        String cod ;
        String visibility;
        String base;

        String lat;
        String lon;

        String temp;
        String pressure;
        String humidity;
        String tempMin;
        String tempMAx;

        // JSONObject initialized passing the data in it which we received as a string form of JSON.
        try {
            JSONObject jsonObject = new JSONObject(data);
//////////////////////////////////////////////////////////////////////////////////////
            // getting the data from JSONObject named as coord.
            JSONObject jsonObject1 = jsonObject.getJSONObject("coord");
            // extracting latitude and longitude from coord.
            lon = jsonObject1.getString("lon");
            lat = jsonObject1.getString("lat");
//////////////////////////////////////////////////////////////////////////////////////
            name = jsonObject.getString("name");
            id = jsonObject.getString("id");
            cod = jsonObject.getString("cod");
            visibility = jsonObject.getString("visibility");
            base = jsonObject.getString("base");
//////////////////////////////////////////////////////////////////////////////////////
            // getting the data inside the jsonArray named as weather.
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            // extracting the data from the array till the end of its length.
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                idd = jsonObject2.getString("id");
                main = jsonObject2.getString("main");
                description = jsonObject2.getString("description");
                icon = jsonObject2.getString("icon");
            }
/////////////////////////////////////////////////////////////////////////////////////
            JSONObject jsonObject3 = jsonObject.getJSONObject("main");
            temp = jsonObject3.getString("temp");
            pressure = jsonObject3.getString("pressure");
            humidity = jsonObject3.getString("humidity");
            tempMin = jsonObject3.getString("temp_min");
            tempMAx = jsonObject3.getString("temp_max");
//////////////////////////////////////////////////////////////////////////////////////
            // Structuring the extracted data in a form that can be displayed in listVeiw.
            String str1 = "\nLatitude: "+lat+"\nLongitude: "+lon+"\n\n";
            String str2 = "Weather Id: "+idd+"\nMain: "+main+"\nDescription: "+description+"\nIcon: "+icon+"\n\n" ;
            String str3 = "Temperature: "+temp+"\nPressure: "+pressure+"\nHumidity: "+humidity+"\nMin Temp.: "+tempMin+"\nMax Temp.: "+tempMAx+"\n\n";
            String str4 = "City Name: "+name+"\nId: "+id+"\nCod: "+cod+"\nVisibility: "+visibility+"\nBase: "+base+"" ;
            String finalStr = str1+str2+str3+str4;
            // adding the final data in the arrayList
            arrayList.add(finalStr);
            // refreshing the page if data set is changed, here in this case the data set is changed in above line.
            arrayAdapter.notifyDataSetChanged();

        }catch (JSONException e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
        }
    }
}
