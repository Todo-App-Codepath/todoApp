package com.example.chorewheel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.chorewheel.Fragments.AddTaskFragment;
import com.example.chorewheel.Fragments.EditUserFragment;
import com.example.chorewheel.adapters.MemberSelectorAdapter;
import com.example.chorewheel.models.Members;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    protected String groupId = null;
    SwipeRefreshLayout swipeContainer;
    FloatingActionButton addTask;
    protected MemberSelectorAdapter selectorAdapter;
    protected List <ParseUser> members;
    protected RecyclerView rvSelector;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // set up adapters and recycler views

        allTasks = new ArrayList<>();
        rvTasksList = findViewById(R.id.rvTaskList);
        taskAdapter = new TaskAdapter(this, allTasks);

        members = new ArrayList<>();
        rvSelector = findViewById(R.id.rvMembersSelector);
        selectorAdapter = new MemberSelectorAdapter(this, members);


        // swipe refresh layout
        swipeContainer =findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryMyTasks();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
      
        //set up task adapter 
        rvTasksList.setAdapter(taskAdapter);
        rvTasksList.setLayoutManager(new LinearLayoutManager(this));

        rvSelector.setAdapter(selectorAdapter);
        rvSelector.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //creating recyclerviews

        queryMyTasks();
        queryMembers();

        // Add Task Floating Action Button (FAB)
        addTask = findViewById(R.id.fab_add_task);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Add Task Fragment
                FragmentManager fm = getSupportFragmentManager();
                AddTaskFragment addTaskFragment = AddTaskFragment.newInstance("Add Task");
                addTaskFragment.show(fm, "fragment_add_task");

                // Update RecyclerView After Adding Task
                fm.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        super.onFragmentViewDestroyed(fm, f);
                        queryMyTasks();
                        fm.unregisterFragmentLifecycleCallbacks(this);
                    }
                }, false);
            }
        });

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
        else if (item.getItemId() == R.id.editProfileTab) {
            //EditUserFragment editUserFragment = EditUserFragment.newInstance("edit profile");
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(android.R.id.content, new EditUserFragment())
                    .commit();
        }
        return true;
    }

    // query for tasks of all members
    protected void queryMyTasks(){
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include("User");
        ParseUser curr_user = ParseUser.getCurrentUser();
        query.whereEqualTo("userID",curr_user ).addAscendingOrder("dueDate");
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
                    // For any test statements
                }
                taskAdapter.clear();
                taskAdapter.addAll(tasks);
                swipeContainer.setRefreshing(false);

            }
        });
    }

    // query all members excluding the current user
    protected void queryMembers(){
        final ParseUser user = ParseUser.getCurrentUser();
        Log.i("test", user.getParseObject("GroupID").getObjectId());
        ParseObject gObj= user.getParseObject("GroupID");
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.include("User");
        query.include("GroupID");
        query.whereEqualTo("GroupID",gObj );;
        //TODO add group member filter here
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersList, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issues with getting users", e);
                    return;
                }else{
                    // For any test statements
                    Log.i("test", "got the users");
                }
                //adding current user as the first item in the list
                usersList.add(0, user);
                selectorAdapter.clear();
                selectorAdapter.addAll(usersList);
            }

        });
    }
}