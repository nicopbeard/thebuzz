package edu.lehigh.cse216.phase0;

import android.os.Bundle;
import android.util.Log;

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

public class Comments extends AppCompatActivity {
    private final ArrayList<CommentInfo> commentFromVolley = new ArrayList<>();
    private String user = ItemListAdapter.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Comments");

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
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                String sender = Integer.toString(jsonObj.getInt("senderId"));
                JSONArray commentArray = jsonObj.getJSONArray("comments");
                ArrayList<String> comments = new ArrayList<String>();
                for(int j = 0; j < commentArray.length(); j++)
                {
                    try {
                        JSONObject object = commentArray.getJSONObject(j);
                        int commentId = object.getInt("commentId");
                        int msgId = object.getInt("msgId");
                        String comment = object.getString("text");
                        if(sender.equals(user)) {
                            Log.d("npb221", comments.get(i));
                            commentFromVolley.add(new CommentInfo(commentId, msgId, comment));
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (final JSONException e) {
            Log.d("ERROR", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("ERROR", "Successfully parsed JSON file.");
        RecyclerView rv = findViewById(R.id.recyclerViewMsgs);
        if(rv == null)
        {
            return;
        }
        rv.setLayoutManager(new LinearLayoutManager(this));
        //final ItemListAdapter adapter = new ItemListAdapter(this, commentFromVolley);
        //rv.setAdapter(adapter);
    }

    private String getMdata(String str){
        String[] data = str.split("mData\":");
        return data[1];
    }
}
