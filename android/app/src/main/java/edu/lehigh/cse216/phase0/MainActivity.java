package edu.lehigh.cse216.phase0;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final ArrayList<MessageInfo> dataFromVolley = new ArrayList<>();
    private Button sendMsgButton;
    private EditText msgToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("kta221", "Debug Message from onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Message Board");

        sendMsgButton = findViewById(R.id.sendMessage);
        msgToSend = findViewById(R.id.messageToSend);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //For future features

    }*/


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

                dataFromVolley.add(new MessageInfo(msgNum, sender, msg, numUpvotes, numDownvotes));
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

        sendMsgButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(msgToSend.getText().toString().isEmpty()){
                    return;
                }
                adapter.addMessage(new MessageInfo("User has no name", msgToSend.getText().toString()));
                sendMessageToServer(msgToSend.getText().toString(), 56);
                msgToSend.setText("");

                //takes you out of editText interface
                InputMethodManager imm = (InputMethodManager) msgToSend.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(msgToSend.getWindowToken(), 0);
            }
        });
    }

    private String getMdata(String str){
        String[] data = str.split("mData\":");
        return data[1];
    }

    private void sendMessageToServer(final String msg, final int name) {
        RequestQueue serverQueue = VolleySingleton.getInstance(this).getRequestQueue();

        String sendMsgServerURL = "https://clowns-who-code.herokuapp.com/messages";

        JSONObject postparams = new JSONObject();
        try{
            postparams.put("senderId", name);
            postparams.put("text", msg);
            postparams.put("nUpVotes", 0);
            postparams.put("nDownVotes", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                sendMsgServerURL, postparams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.d("SUCCESS", "SUCCESS IN POST REQ");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "ERROR IN POST REQUEST: " + error.toString() );
                    }
                });
        serverQueue.add(jsonObjReq);
    }
}