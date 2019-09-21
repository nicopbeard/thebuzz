package edu.lehigh.cse216.phase0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private ArrayList<DataFromVolley> dataFromVolley;
    private LayoutInflater mLayoutInflater;

    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private final int INDEX_NOT_SET = -1;

        private final TextView msgNum;
        private final TextView sender;
        private final TextView msg;
        private final Button numUpvotes;
        private final Button numDownvotes;

        int indexInDataFromVolley = INDEX_NOT_SET;

        ViewHolder(View itemView) {
            super(itemView);
            this.msgNum = (TextView) itemView.findViewById(R.id.listMsgNum);
            this.sender = (TextView) itemView.findViewById(R.id.listSender);
            this.msg = (TextView) itemView.findViewById(R.id.listMsg);
            this.numUpvotes = (Button) itemView.findViewById(R.id.listUpvotesButton);
            this.numDownvotes = (Button) itemView.findViewById(R.id.listDownvotesButton);

            numUpvotes.setOnClickListener(this);
            numDownvotes.setOnClickListener(this);
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

    ItemListAdapter(Context context, ArrayList<DataFromVolley> dataFromVolley) {
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
        final DataFromVolley dataFromVolley = this.dataFromVolley.get(position);
        holder.msgNum.setText(Integer.toString(dataFromVolley.msgNum()));
        holder.sender.setText(dataFromVolley.sender());
        holder.msg.setText(dataFromVolley.msg());
        holder.numUpvotes.setText(Integer.toString(dataFromVolley.numUpVotes()));
        holder.numDownvotes.setText(Integer.toString(dataFromVolley.numDownvotes()));

        holder.indexInDataFromVolley = position;
    }

    public void addMessage(DataFromVolley data) {
        data.msgNum(dataFromVolley.get(dataFromVolley.size() - 1).msgNum());
        dataFromVolley.add( data);
    }
}