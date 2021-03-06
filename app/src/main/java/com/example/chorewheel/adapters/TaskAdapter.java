package com.example.chorewheel.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.chorewheel.Fragments.InfoTaskFragment;
import com.example.chorewheel.MainActivity;
import com.example.chorewheel.R;
import com.example.chorewheel.models.Task;
import com.example.chorewheel.models.User;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        try {
            holder.bind(task);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    //clears elements of recyclerview
    public void clear() {
        tasks.clear();
        notifyDataSetChanged();
    }

    //adds all items to task list
    public void addAll(List<Task> taskList){
        tasks.addAll(taskList);
        notifyDataSetChanged();

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTaskName;
        private CheckBox cbCheckBox;
        private TextView tvTaskDueDate;
        private ImageView ivProfileImage;
        private TextView tvUsername;
        private RelativeLayout taskContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            cbCheckBox = itemView.findViewById(R.id.cbCheckBox);
            tvTaskDueDate = itemView.findViewById(R.id.tvTaskDueDate);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            taskContainer = itemView.findViewById(R.id.taskContainer);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }

        public void bind(final Task task) throws ParseException {
            if (task.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                tvUsername.setText("My Task");
            }else{
                tvUsername.setText(task.getUser().fetchIfNeeded().getString("firstName"));
            }
            tvTaskName.setText(task.getTaskName());
            cbCheckBox.setChecked(task.getChecked());
            tvTaskDueDate.setText(task.getFormattedDate());


            // for placing profile image into user icon on task
            ParseFile image = null;
            try {
                image= task.getUser().fetchIfNeeded().getParseFile("image");
                if(image==null){
                    Glide.with(context).load(R.drawable.ic_user_img).transform(new CircleCrop()).into(ivProfileImage);
                }
                else{
                    Glide.with(context).load(image.getFile()).transform(new CircleCrop()).into(ivProfileImage);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (task.getChecked()){
                itemView.setBackgroundResource(R.drawable.faded_out_layout);
            }else{
                itemView.setBackgroundResource(R.drawable.background_layout);
            }

            // View Info / Edit Task
            taskContainer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    // Open Info Task Fragment
                    FragmentManager fm =  ((AppCompatActivity) context).getSupportFragmentManager();
                    InfoTaskFragment infoTaskFragment = InfoTaskFragment.newInstance("Task Info");
                    infoTaskFragment.setTask(task);
                    infoTaskFragment.show(fm, "fragment_info_task");

                    // Update Task in RecyclerView
                    fm.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                            super.onFragmentViewDestroyed(fm, f);

                            // Post Dismiss Action Here

                            // Same user, update task in list
                            if (task.getUser().getUsername().equals( ParseUser.getCurrentUser().getUsername()) ) {
                                tvTaskName.setText(task.getTaskName());
                                cbCheckBox.setChecked(task.getChecked());
                                tvTaskDueDate.setText(task.getFormattedDate());
                            } // Member assigned is different from current user, remove from list
                            else {
                                tasks.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(),tasks.size());
                            }
                            fm.unregisterFragmentLifecycleCallbacks(this);
                        }
                    }, false);
                }
            });

            cbCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    if (isChecked){
                        itemView.setBackgroundResource(R.drawable.faded_out_layout);
                    }else{
                        itemView.setBackgroundResource(R.drawable.background_layout);
                    }
//                    ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
//                    query.getInBackground(task.getObjectId(), new GetCallback<Task>() {
//                        @Override
//                        public void done(Task object, ParseException e) {
//                            if (e!=null){
//                                Log.e("UpdateTask", "Issues with getting tasks", e);
//                            }else{
//                                object.put("checked", isChecked);
//                                object.saveInBackground();
//                            }
//
//                        }
//                    });
                    task.put("checked", isChecked);
                    task.saveInBackground();
                }
            });










        }
    }
}
