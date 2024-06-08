package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private final Context context;

    CustomItemClickListener listener;

    private  List<Message> messages;
    public ListAdapter(@NonNull Context context, @NonNull List<Message> objects, CustomItemClickListener listener) {
        this.context = context;
        this.messages = objects;
        this.listener = listener;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View rowView = inflater.inflate(R.layout.activity_receiving,R.id.messagelayout ,true);
//        LinearLayout layout = (LinearLayout) rowView.findViewByid(R.id.messagelayout);
//
//        TextView senderNumber = (TextView) rowView.findViewById(R.id.smsNumberText);
//        senderNumber.setText(messages.get(position).getSender());
//
//        TextView message = (TextView) rowView.findViewById(R.id.smsMessageText);
//        message.setText(messages.get(position).getText());
//
//        TextView timestamp = (TextView) rowView.findViewById(R.id.smsTimestampText);
//        timestamp.setText(messages.get(position).getTimestamp());
//        return rowView;
//    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);

        // Passing view to ViewHolder
        ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = (Message) messages.get(position);
        holder.senderNumber.setText(message.getSender());
        holder.message.setText(message.getText());
        holder.timestamp.setText(message.getTimestamp());
    }




    @Override
    public int getItemCount() {
        return messages.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView senderNumber,message,timestamp;

        public ViewHolder(View view) {
            super(view);
             senderNumber = (TextView) view.findViewById(R.id.smsNumberText);
             message = (TextView) view.findViewById(R.id.smsMessageText);
             timestamp = (TextView) view.findViewById(R.id.smsTimestampText);
        }
    }
}
