package com.example.chorewheel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chorewheel.R;
import com.example.chorewheel.models.Members;
import com.example.chorewheel.models.User;
import com.parse.ParseUser;

import java.util.List;

public class MemberSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int USER = 0, GROUP_IMAGE = 1;

    Context context;
    List<Object> objectsList;

    public MemberSelectorAdapter(Context context, List<Object> objectsList) {
        this.context = context;
        this.objectsList = objectsList;
    }

    @Override
    public int getItemViewType(int position) {
        if(objectsList.get(position) instanceof Members){
            return GROUP_IMAGE;
        }else{
            return USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case GROUP_IMAGE:
                View viewGroup = LayoutInflater.from(context).inflate(R.layout.group_member_icon, parent, false);
                viewHolder = new ViewGroupHolder(viewGroup);
                break;

            default:
                View viewMember = LayoutInflater.from(context).inflate(R.layout.user_icon, parent, false);
                viewHolder = new ViewMember(viewMember);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case GROUP_IMAGE:
                ViewGroupHolder viewGroupHolder = (ViewGroupHolder) holder;
                bindGroupIcon(viewGroupHolder, position);
                break;
            default:
                ViewMember viewMember = (ViewMember) holder;
                bindMemberIcon(viewMember, position);
                break;
        }
    }

    private void bindMemberIcon(ViewMember viewMember, int position) {
        ParseUser curr_user = ParseUser.getCurrentUser();
        User user = new User(objectsList.get(position));

        if (objectsList.get(position)==curr_user){
            viewMember.tvMemberName.setText("My Tasks");
        }else{
            viewMember.tvMemberName.setText(user.getFirstName());
        }

    }

    private void bindGroupIcon(ViewGroupHolder viewGroupHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewGroupHolder extends RecyclerView.ViewHolder{
        ImageView ivMemberImage1;
        ImageView ivMemberImage2;
        ImageView ivMemberImage3;

        public ViewGroupHolder(@NonNull View itemView) {
            super(itemView);
            ivMemberImage1 = itemView.findViewById(R.id.ivMemberImage1);
            ivMemberImage2 = itemView.findViewById(R.id.ivMemberImage2);
            ivMemberImage3 = itemView.findViewById(R.id.ivMemberImage3);
        }
    }
    public class ViewMember extends RecyclerView.ViewHolder{
        ImageView ivMemberImage;
        TextView tvMemberName;

        public ViewMember(@NonNull View itemView) {
            super(itemView);
            ivMemberImage = itemView.findViewById(R.id.ivMemberImage);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
        }
    }


}
