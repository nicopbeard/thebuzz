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

    private ArrayList<DataFromVolley> dataFromVolley;
    private LayoutInflater mLayoutInflater;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView msgNum;
        private final TextView sender;
        private final TextView msg;
        private final Button numUpvotes;
        private final Button numDownvotes;

        ViewHolder(View itemView) {
            super(itemView);
            this.msgNum = (TextView) itemView.findViewById(R.id.listMsgNum);
            this.sender = (TextView) itemView.findViewById(R.id.listSender);
            this.msg = (TextView) itemView.findViewById(R.id.listMsg);
            this.numUpvotes = (Button) itemView.findViewById(R.id.listUpvotesButton);
            this.numDownvotes = (Button) itemView.findViewById(R.id.listDownvotesButton);
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
    }