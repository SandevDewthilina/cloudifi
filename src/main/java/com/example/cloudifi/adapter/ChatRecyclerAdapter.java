package com.example.cloudifi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudifi.R;
import com.example.cloudifi.model.ChatModel;

import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.MyViewHolder> {

    private List<ChatModel> chatModelList;
    private String myId = "s";

    public ChatRecyclerAdapter(List<ChatModel> chatModels, String myId) {

        chatModelList = chatModels;
        this.myId = myId;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (chatModelList.get(position).getWho().equals(myId)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.myText.setText(chatModelList.get(position).getMessage());
        } else {
            holder.hisLayout.setVisibility(View.VISIBLE);
            holder.hisText.setText(chatModelList.get(position).getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout hisLayout;
        private ConstraintLayout myLayout;
        private TextView myText;
        private TextView hisText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hisLayout = itemView.findViewById(R.id.his_text_layout);
            myLayout = itemView.findViewById(R.id.my_text_layout);
            hisText = itemView.findViewById(R.id.his_text);
            myText = itemView.findViewById(R.id.my_text);

        }
    }
}
