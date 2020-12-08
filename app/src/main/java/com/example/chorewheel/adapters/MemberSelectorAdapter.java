package com.example.chorewheel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.chorewheel.MainActivity;
import com.example.chorewheel.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class MemberSelectorAdapter extends RecyclerView.Adapter<MemberSelectorAdapter.ViewHolder> {
    List<ParseUser> usersList;
    Context context;

    public MemberSelectorAdapter(Context context, List<ParseUser> usersList) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_icon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser user = usersList.get(position);
        try {
            holder.bind(user);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
        public void clear() {
        usersList.clear();
        notifyDataSetChanged();
    }

    //adds all items to task list
    public void addAll(List<ParseUser> users){

        usersList.addAll(users);
        notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMemberImage;
        TextView tvMemberName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMemberImage = itemView.findViewById(R.id.iv_member_icon_hrv);
            tvMemberName = itemView.findViewById(R.id.tvMemberName_hrv);
        }

        public void bind(ParseUser user) throws ParseException {
            ParseUser currUser = ParseUser.getCurrentUser();
            if(user == currUser){
                tvMemberName.setText("My List");
            }else {
                tvMemberName.setText(user.getString("username"));
            }
            ParseFile image1= user.getParseFile("image");
            if (image1 != null){
                Glide.with(context).load(image1.getFile()).transform(new CircleCrop()).into(ivMemberImage);
            } else{
                Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(ivMemberImage);
            }

        }
    }
}
