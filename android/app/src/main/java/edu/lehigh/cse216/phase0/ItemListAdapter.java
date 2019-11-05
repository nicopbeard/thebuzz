package edu.lehigh.cse216.phase0;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.lehigh.cse216.phase0.R;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private ArrayList<MessageInfo> dataFromVolley;
    private LayoutInflater mLayoutInflater;

    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private final int INDEX_NOT_SET = -1;

        private final TextView msgNum;
        private final TextView sender;
        private final TextView msg;
        private final Button upvotes;
        private final Button downvotes;

        //refers to index in the field
        int indexInDataFromVolley = INDEX_NOT_SET;

        ViewHolder(View itemView) {
            super(itemView);
            this.msgNum = itemView.findViewById(R.id.listMsgNum);
            this.sender = itemView.findViewById(R.id.listSender);
            this.msg = itemView.findViewById(R.id.listMsg);
            this.upvotes = itemView.findViewById(R.id.listUpvotesButton);
            this.downvotes = itemView.findViewById(R.id.listDownvotesButton);

            upvotes.setOnClickListener(this);
            downvotes.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            switch (button.getId()){
                case R.id.listUpvotesButton:
                    if(indexInDataFromVolley == INDEX_NOT_SET) {
                        throw new RuntimeException("Index not set in ItemListAdapter");
                    }
                    likeMessage(Integer.parseInt(dataFromVolley.get(indexInDataFromVolley).sender()), dataFromVolley.get(indexInDataFromVolley).msgNum(), true);
                    //check if already liked message -- if so, then it becomes a -1 like
                    dataFromVolley.get(indexInDataFromVolley).addUpvote();
                    ItemListAdapterHelper.incrementButtonCount(button);
                    break;
                case R.id.listDownvotesButton:
                    if(indexInDataFromVolley == INDEX_NOT_SET) {
                        throw new RuntimeException("Index not set in ItemListAdapter");
                    }

                    //likeMessage(Integer.parseInt(dataFromVolley.get(indexInDataFromVolley).sender()), dataFromVolley.get(indexInDataFromVolley).msgNum(), false);
                    //Waiting on backend to allow for above line
                    
                    dataFromVolley.get(indexInDataFromVolley).addDownvote();
                    ItemListAdapterHelper.incrementButtonCount(button);
                    break;
            }
        }
    }

    ItemListAdapter(Context context, ArrayList<MessageInfo> dataFromVolley) {
        this.dataFromVolley = dataFromVolley;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return dataFromVolley.size();
    }

    //Creates the whole grid of views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        return new ViewHolder(view);
    }

    //Puts data current data into each of the rows
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MessageInfo info = this.dataFromVolley.get(position);
        holder.msgNum.setText(Integer.toString(info.msgNum()));
        holder.sender.setText(info.sender());
        holder.msg.setText(info.msg());
        holder.upvotes.setText(Integer.toString(info.upvotes()));
        holder.downvotes.setText(Integer.toString(info.downvotes()));

        //so we can update information in the list when user clicks upvote/downvote, ect
        holder.indexInDataFromVolley = position;
    }

    public void addMessage(MessageInfo data) {
        //sets message number as the last message number + 1. Assumes all messages are in the array
        //And they're sorted. This assumption might not hold true in the future.
        data.msgNum(dataFromVolley.get(dataFromVolley.size() - 1).msgNum() + 1);
        dataFromVolley.add(data);
    }

    private void likeMessage(int userId, int msgId, boolean like) {
        RequestQueue serverQueue = VolleySingleton.mRequestQueue;

        String sendMsgServerURL = "https://clowns-who-code.herokuapp.com/like";

        JSONObject postparams = new JSONObject();
        try{
            postparams.put("userId", userId);
            postparams.put("msgId", msgId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                sendMsgServerURL, postparams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.d("SUCCESS", "SUCCESS IN PUT REQ");
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