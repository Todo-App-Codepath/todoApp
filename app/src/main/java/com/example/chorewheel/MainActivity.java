package com.example.chorewheel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.chorewheel.adapters.TaskAdapter;
import com.example.chorewheel.models.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView rvTasksList;
    protected TaskAdapter taskAdapter;
    private static final String TAG = "MainActivity";
    protected List<Task> allTasks;
    protected String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //query for group Id


        rvTasksList = findViewById(R.id.rvTaskList);
        allTasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, allTasks);
//        //TODO swipe refresh layout
//

        rvTasksList.setAdapter(taskAdapter);
        rvTasksList.setLayoutManager(new LinearLayoutManager(this));

        queryMyTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rv_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuLogout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            finish();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
        return true;
    }

    // query for tasks of all members
    protected void queryMyTasks(){
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include("User");
        ParseUser curr_user = ParseUser.getCurrentUser();
        query.whereEqualTo("userID",curr_user );
        //TODO add group member filter here
        query.setLimit(20);
//        query.addDescendingOrder(Task.KEY_DUE_DATE);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issues with getting tasks", e);
                    return;
                }else{
                    try {
                        Log.i(TAG,"UserObject: " + tasks.get(0).getUser().fetchIfNeeded().getParseFile("image"));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                }
                taskAdapter.clear();
                taskAdapter.addAll(tasks);


            }
        });
    }


}