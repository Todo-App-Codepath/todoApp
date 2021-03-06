package com.example.chorewheel.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.chorewheel.GetTasks;
import com.example.chorewheel.MainActivity;
import com.example.chorewheel.R;
import com.example.chorewheel.models.Members;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class MemberSelectorAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private GetTasks getTaskInterface;
    private int selected_position = 1;
    private final int USER = 0, GROUP_IMAGE = 1;

    Context context;
    List<Object> objectsList;

    public MemberSelectorAdapter1(Context context, List<Object> objectsList, GetTasks getTaskInterface) {
        this.context = context;
        this.objectsList = objectsList;
        this.getTaskInterface = getTaskInterface;
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
        // check if the item in array is a member class or a Parse user
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

    //bind members
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

    private void bindMemberIcon(final ViewMember viewMember, final int position) throws ParseException {
        //check if the current position is the selected position and change the background color if selected or not selected
        if (selected_position == position){
            viewMember.rlIconContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.user_selector));
        }else{
            viewMember.rlIconContainer.setBackgroundResource(0);
        }
        ParseUser curr_user = ParseUser.getCurrentUser();
        ParseUser user = (ParseUser) objectsList.get(position);

        if (user!=null) {
            Log.i("user", user.toString());
            if (objectsList.get(position)==curr_user){
                viewMember.tvMemberName.setText("My Tasks");
            }else {
                viewMember.tvMemberName.setText(user.getString("firstName"));
            }
            // add an icon to the user images
            ParseFile image1= user.getParseFile("image");
            if (image1 != null){
                Glide.with(context).load(image1.getFile()).transform(new CircleCrop()).into(viewMember.ivMemberImage);
            } else{
                Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(viewMember.ivMemberImage);
            }
        }
        // if item is clicked change the background to indicate it is selected and query the user's tasks
        viewMember.rlIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = (ParseUser)objectsList.get(position);
                getTaskInterface.queryUserTasks(user);
                if(selected_position == position){
                    selected_position = -1;
                    notifyDataSetChanged();
                    return;
                }
                selected_position = position;
                notifyDataSetChanged();
            }
        });

    }


    // bind group member icons
    private void bindGroupIcon(final ViewGroupHolder viewGroupHolder, final int position) throws ParseException {
        if (selected_position == position){
            viewGroupHolder.rlGroupContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.user_selector));
        }else{
            viewGroupHolder.rlGroupContainer.setBackgroundResource(0);
        }
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
            viewGroupHolder.rlGroupContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Members members_list = (Members)objectsList.get(position);
                    getTaskInterface.queryAllMemberTasks();
                    if(selected_position == position){
                        selected_position = -1;
                        notifyDataSetChanged();
                        return;
                        //reference https://stackoverflow.com/questions/50872380/how-to-select-and-de-select-an-item-in-recyclerview-how-to-highlight-selected-i
                    }
                    selected_position = position;
                    notifyDataSetChanged();
                }
            });
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
        RelativeLayout rlGroupContainer;
        ImageView ivMemberImage1;
        ImageView ivMemberImage2;
        ImageView ivMemberImage3;

        public ViewGroupHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setClickable(true);
            ivMemberImage1 = itemView.findViewById(R.id.ivMemberImage1);
            ivMemberImage2 = itemView.findViewById(R.id.ivMemberImage2);
            ivMemberImage3 = itemView.findViewById(R.id.ivMemberImage3);
            rlGroupContainer = itemView.findViewById(R.id.rlGroupContainer);

        }
    }
    public class ViewMember extends RecyclerView.ViewHolder{
        ImageView ivMemberImage;
        TextView tvMemberName;
        RelativeLayout rlIconContainer;

        public ViewMember(@NonNull View itemView) {
            super(itemView);
            itemView.setClickable(true);
            ivMemberImage = itemView.findViewById(R.id.iv_member_icon_hrv);
            tvMemberName = itemView.findViewById(R.id.tvMemberName_hrv);
            rlIconContainer = itemView.findViewById(R.id.rlIconContainer);
        }
    }


}
