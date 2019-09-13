package edu.lehigh.cse216.phase0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

//For some reason required to import these




public class MainActivity extends AppCompatActivity {

    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Datum> mData = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("kta221", "Debug Message from onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //ADDED CODE
        // Instantiate the RequestQueue.
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = "http://www.cse.lehigh.edu/~spear/5k.json";
        // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                final ArrayList<String> myList = new ArrayList<>();
//                                try {
//                                    JSONArray jStringArray = new JSONArray(response);
//                                    for (int i = 0; i < jStringArray.length(); ++i) {
//                                        myList.add(jStringArray.getString(i));
//                                    }
//                                } catch (final JSONException e) {
//                                    Log.d("kta221", "Error parsing JSON file..." + e.getMessage());
//                                }
//                                ListView mListView = (ListView) findViewById(R.id.datum_list_view);
//                                ArrayAdapter adapter = new ArrayAdapter<>(MainActivity.this,
//                                        android.R.layout.simple_list_item_1,
//                                        myList);
//                                mListView.setAdapter(adapter);
                                populateListFromVolley(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("kta221", "That didn't work!");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 789) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the "extra" string of data
                Toast.makeText(MainActivity.this, data.getStringExtra("result"), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SecondActivity.class);
            i.putExtra("label_contents", "CSE216 is the best");
            startActivityForResult(i, 789); // 789 is the number that will come back to us
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void populateListFromVolley(String response){
        try {
            JSONArray json= new JSONArray(response);
            for (int i = 0; i < json.length(); ++i) {
                int num = json.getJSONObject(i).getInt("num");
                String str = json.getJSONObject(i).getString("str");
                mData.add(new Datum(num, str));
            }
        } catch (final JSONException e) {
            Log.d("kta221", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("kta221", "Successfully parsed JSON file.");
        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);

        adapter.setClickListener(new ItemListAdapter.ClickListener() {
            @Override
            public void onClick(Datum d) {
                Toast.makeText(MainActivity.this, d.mIndex + " --> " + d.mText, Toast.LENGTH_LONG).show();
            }
        });

    }


}
