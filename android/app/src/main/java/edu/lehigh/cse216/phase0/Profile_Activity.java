package edu.lehigh.cse216.phase0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Profile_Activity extends AppCompatActivity {
    private final ArrayList<MessageInfo> dataFromVolley = new ArrayList<>();
    private String user = ItemListAdapter.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);
        setTitle(user);

        RequestQueue serverQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String getMsgsServerURL = "https://clowns-who-code.herokuapp.com/messages";
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getMsgsServerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        populateListFromVolley(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Trouble getting info from volley");
            }
        });

        serverQueue.add(stringRequest);
    }

    private void populateListFromVolley(String response){
        try {
            String mData = getMdata(response);
            JSONArray jsonArr = new JSONArray(mData);
            for (int i = 0; i < jsonArr.length() && i < 10; i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                int msgNum = jsonObj.getInt("id");
                String sender = Integer.toString(jsonObj.getInt("senderId"));
                String msg = jsonObj.getString("text");
                int numUpvotes = jsonObj.getInt("nUpVotes");
                int numDownvotes = jsonObj.getInt("nDownVotes");

                if(sender.equals(user)) {
                    dataFromVolley.add(new MessageInfo(msgNum, sender, msg, numUpvotes, numDownvotes));
                }
            }
        } catch (final JSONException e) {
            Log.d("ERROR", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("ERROR", "Successfully parsed JSON file.");
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerViewMsgs);
        rv.setLayoutManager(new LinearLayoutManager(this));
        final ItemListAdapter adapter = new ItemListAdapter(this, dataFromVolley);
        rv.setAdapter(adapter);
    }

    private String getMdata(String str){
        String[] data = str.split("mData\":");
        return data[1];
    }
}