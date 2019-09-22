package edu.lehigh.cse216.phase0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
                    //TODO call server
                    //Increase upvotes
                    if(indexInDataFromVolley == INDEX_NOT_SET) {
                        throw new RuntimeException("Index not set in ItemListAdapter");
                    }
                    dataFromVolley.get(indexInDataFromVolley).addUpvote();
                    ItemListAdapterHelper.incrementButtonCount(button);
                    break;
                case R.id.listDownvotesButton:
                    //TODO call server
                    if(indexInDataFromVolley == INDEX_NOT_SET) {
                        throw new RuntimeException("Index not set in ItemListAdapter");
                    }
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
        data.msgNum(dataFromVolley.get(dataFromVolley.size() - 1).msgNum());
        dataFromVolley.add( data);
    }
}