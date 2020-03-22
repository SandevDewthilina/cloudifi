package com.example.cloudifi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudifi.R;
import com.example.cloudifi.entitiies.UserEntity;
import com.example.cloudifi.model.UsersModel;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.MyViewHolder> {

    private List<UserEntity> usersModelList;
    private OnItemClickListner onItemClickListner;

    public UsersListAdapter(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public void setUsersModelList(List<UserEntity> usersModelList) {
        this.usersModelList = usersModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.username.setText(usersModelList.get(position).getUsername());
        holder.status.setText(usersModelList.get(position).getStatus());
        holder.time.setText(usersModelList.get(position).getRegistered_date().toString());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListner.onItemClick(usersModelList.get(position).getDocument_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (usersModelList.size() != 0)
        return usersModelList.size();
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView status;
        TextView time;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            username = itemView.findViewById(R.id.username_item);
            status = itemView.findViewById(R.id.user_status_item);
            time = itemView.findViewById(R.id.member_since_item);

        }
    }

    public interface OnItemClickListner {
        void onItemClick(String userid);
    }

}
