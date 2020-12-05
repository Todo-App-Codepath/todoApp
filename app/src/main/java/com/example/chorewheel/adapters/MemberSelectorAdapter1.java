package com.example.chorewheel.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.chorewheel.R;
import com.example.chorewheel.models.Members;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class MemberSelectorAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int USER = 0, GROUP_IMAGE = 1;

    Context context;
    List<Object> objectsList;

    public MemberSelectorAdapter1(Context context, List<Object> objectsList) {
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case GROUP_IMAGE:
                View viewGroup = inflater.inflate(R.layout.group_member_icon, parent, false);
                viewHolder = new ViewGroupHolder(viewGroup);
                break;

            default:
                View viewMember = inflater.inflate(R.layout.user_icon, parent, false);
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
                try {
                    bindGroupIcon(viewGroupHolder, position);
//                    viewGroupHolder.bind()
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            default:
                ViewMember viewMember = (ViewMember) holder;
                try {
                    bindMemberIcon(viewMember, position);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void bindMemberIcon(ViewMember viewMember, int position) throws ParseException {
        ParseUser curr_user = ParseUser.getCurrentUser();
        ParseUser user = (ParseUser) objectsList.get(position);
        if (user!=null) {

            Log.i("user", user.toString());
            if (objectsList.get(position)==curr_user){
                viewMember.tvMemberName.setText("My Tasks");
            }else {
                viewMember.tvMemberName.setText(user.getString("firstName"));
            }
            Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(viewMember.ivMemberImage);
        }



    }
//        Glide.with(context).load(user.getParseFile("image").getFile()).transform(new CircleCrop()).into(viewMember.ivMemberImage);



    private void bindGroupIcon(ViewGroupHolder viewGroupHolder, int position) throws ParseException {
        Members members = (Members)objectsList.get(position);
            List<ParseUser> users;
            users = members.getUserList();
            ParseFile image1 = null;
            ParseFile image2 = null;
            ParseFile image3 = null;
            if (users.size() < 3){
                viewGroupHolder.ivMemberImage3.setVisibility(View.GONE);
                //get image
                image1= users.get(0).fetchIfNeeded().getParseFile("image");
                image2= users.get(1).fetchIfNeeded().getParseFile("image");

                if (image1 == null){
                    Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage1);
                }else{
                    Glide.with(context).load(image1.getFile()).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage1);
                }
                if (image2 == null){
                    Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage2);
                }else{
                    Glide.with(context).load(image2.getFile()).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage2);
                }
            }else{
                //get images from first 3 users
                image1= users.get(0).fetchIfNeeded().getParseFile("image");
                image2= users.get(1).fetchIfNeeded().getParseFile("image");
                image3= users.get(2).fetchIfNeeded().getParseFile("image");

                if (image1 == null){
                    Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage1);
                }else{
                    Glide.with(context).load(image1.getFile()).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage1);
                }
                if (image2 == null){
                    Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage2);
                }else{
                    Glide.with(context).load(image2.getFile()).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage2);
                }
                if (image3 == null){
                    Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage3);
                }else{
                    Glide.with(context).load(image3.getFile()).transform(new CircleCrop()).into(viewGroupHolder.ivMemberImage3);
                }
            }

    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }

    public void clear() {
        objectsList.clear();
        notifyDataSetChanged();
    }

    //adds all items to task list
    public void addAll(List<Object> objects){

        objectsList.addAll(objects);

        notifyDataSetChanged();

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
            ivMemberImage = itemView.findViewById(R.id.iv_member_icon_hrv);
            tvMemberName = itemView.findViewById(R.id.tvMemberName_hrv);
        }
    }


}
