package edu.lehigh.cse216.phase0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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
import org.json.JSONObject;

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
    private final ArrayList<DataFromVolley> dataFromVolley = new ArrayList<>();



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
            JSONArray jsonArr = new JSONArray(response);
            for (int i = 0; i < jsonArr.length(); i++) {
                /*JSONObject jsonObj = jsonArr.getJSONObject(i);
                int msgNum = jsonObj.getInt("num");
                String sender = jsonObj.getString("str");
                String msg = jsonObj.getString("msg");
                int numUpvotes = jsonObj.getInt("numUpvotes");
                int numDownvotes = jsonObj.getInt("numDownvotes");

                dataFromVolley.add(new DataFromVolley(msgNum, sender, msg, numUpvotes, numDownvotes));*/

                dataFromVolley.add(new DataFromVolley(i, "Chad", "Hello Darling", 5, 2));
            }
        } catch (final JSONException e) {
            Log.d("ERROR", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("ERROR", "Successfully parsed JSON file.");
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerViewMsgs);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemListAdapter adapter = new ItemListAdapter(this, dataFromVolley);
        rv.setAdapter(adapter);
    }


}
