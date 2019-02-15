package com.example.ishwarya.kisan_sevak;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner states;
    Spinner district;
    Spinner season;

    int status;
    String selectedstate;
    String selectedDistrict;
    ArrayList<String> distlist =  new ArrayList<>();
    int statepos;

    Button goBtn;
    int seasonpos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        states = (Spinner) findViewById(R.id.statespinner);
        district = (Spinner) findViewById(R.id.districtspinner);
        season = (Spinner) findViewById(R.id.seasonspinner);
        goBtn = (Button)findViewById(R.id.submitbtn);

        ArrayAdapter<CharSequence> statesadapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_dropdown_item);
        statesadapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        states.setAdapter(statesadapter);

        ArrayAdapter<CharSequence> seasonsadapter = ArrayAdapter.createFromResource(this, R.array.seasons, android.R.layout.simple_spinner_dropdown_item);
        seasonsadapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        season.setAdapter(seasonsadapter);




        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statepos = i+1;
                selectedstate = states.getSelectedItem().toString();
                distlist.clear();
                GetDistricts gd = new GetDistricts();
                gd.execute();

            }





                @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDistrict = district.getSelectedItem().toString();
                selectedDistrict = selectedDistrict.toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        season.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seasonpos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Cropprediction.class);

                Bundle newBundle = new Bundle();
                newBundle.putInt("state_id", statepos);
                newBundle.putString("district", selectedDistrict);
                newBundle.putInt("season_id", seasonpos);

                i.putExtras(newBundle);
                startActivity(i);


            }
        });



    }


    public class GetDistricts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String inpurl = "https://api.myjson.com/bins/16jt3y";

            try {
                URL url = new URL(inpurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);

                status = urlConnection.getResponseCode();

                System.out.println("Hello"); //just a debugging statement
                System.out.println("Status: " + status);


                if (status == 200 || status == 201) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    Log.v("br", "after br");
                    StringBuilder sb = new StringBuilder();
                    Log.v("sb", "after sb");
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    String jsonString = sb.toString();
                    System.out.println("Districts are : \n");
                    //System.out.println(jsonString);


                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("states");


                    System.out.println(jsonArray.length());

                    for(int i=0; i<jsonArray.length();i++){

                        JSONObject jo = jsonArray.getJSONObject(i);

                        String statename = jo.getString("state");

                        if(statename.equals(selectedstate)) {

                            JSONArray jsonArray1 = jo.getJSONArray("districts");

                            System.out.println("Hello");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                String dist = jsonArray1.getString(j);
                                distlist.add(dist);
                            }

                            for(String st:distlist){
                                System.out.println(st);
                            }

                            break;
                        }



                    }

                    //System.out.println(hm);
                    System.out.println("hello");






                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter<String> ad = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item, distlist);

            ad.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            district.setAdapter(ad);


        }


     }

}


