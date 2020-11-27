package com.example.chorewheel.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.chorewheel.MainActivity;
import com.example.chorewheel.R;
import com.example.chorewheel.models.Task;
import com.example.chorewheel.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

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
        holder.bind(task);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            cbCheckBox = itemView.findViewById(R.id.cbCheckBox);
            tvTaskDueDate = itemView.findViewById(R.id.tvTaskDueDate);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        }

        public void bind(Task task) {
            tvTaskName.setText(task.getTaskName());
            cbCheckBox.setChecked(task.getChecked());
            tvTaskDueDate.setText(task.getTaskName());

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











        }
    }
}
