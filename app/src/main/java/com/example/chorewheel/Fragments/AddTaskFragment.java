package com.example.chorewheel.Fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chorewheel.R;
import com.example.chorewheel.models.Task;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddTaskFragment extends DialogFragment {

    public static final String TAG = "AddTaskFragment";

    private EditText etAddTaskName;
    private EditText etAddTaskDate;
    private EditText etAddTaskDescription;
    private Button btnAddTask;
    private Spinner spinnerPerson;

    public AddTaskFragment() {
        // Empty constructor required for Task Fragment
    }
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    public static AddTaskFragment newInstance(String title) {

        AddTaskFragment frag = new AddTaskFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_add_task, parent, false);
    }

    // This event is triggered soon after onCreateView().
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etAddTaskName = view.findViewById(R.id.etAddTaskName);
        etAddTaskDate = view.findViewById(R.id.etdAddTaskDate);
        etAddTaskDescription = view.findViewById(R.id.etAddTaskNotes);
        btnAddTask = view.findViewById(R.id.btnAddTask);
        spinnerPerson = view.findViewById(R.id.spinnerPerson);
        List<ParseUser> members = new ArrayList<>();
        final ArrayList<String> userList = userList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerson.setAdapter(adapter);
        userList.add(0, "Please Select a Member");
        spinnerPerson.setSelection(0);
        adapter.notifyDataSetChanged();
        final int[] selection = new int[1];
        spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selection[0] = 0;
            }
        });


        // Submit task button functionality
        final List<ParseUser> finalMembers = getUserID();
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create task
                Task newTask = new Task();
                newTask.put("name", etAddTaskName.getText().toString());
                newTask.put("description", etAddTaskDescription.getText().toString());
                // TODO: Select a user for task. Default behavior: Current User
                if (selection[0] == 0) {
                    newTask.put("userID", ParseUser.getCurrentUser());
                } else {
                    ParseUser memberSelected = new ParseUser();
                    for (int i = 0; i < finalMembers.size(); i++) {
                        if (finalMembers.get(i).getUsername().equals(userList.get(selection[0]))) {
                            memberSelected = finalMembers.get(i);
                        }
                    }
                    newTask.put("userID", memberSelected);
                }
                // Add Task Date (Format: MM/dd/yyy (Capitalization matters)
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = etAddTaskDate.getText().toString();
                try {
                    Date dateObject = dateFormat.parse(dateString);
                    newTask.put("dueDate", dateObject);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                newTask.put("checked", false);

                // Saves the task
                newTask.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while adding Task! ", e);
                        }
                        // Close fragment
                        dismiss();
                    }
                });
            }

        });



    }

    private List<ParseUser> getUserID() {
            final ParseUser user = ParseUser.getCurrentUser();
            final List<ParseUser> groupMembers = new ArrayList<>();
            Log.i("test", user.getParseObject("GroupID").getObjectId());
            ParseObject gObj = user.getParseObject("GroupID");
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.include("User");
            query.include("GroupID");
            query.whereEqualTo("GroupID", gObj);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> usersList, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues with getting users", e);
                        return;
                    } else {
                        // For any test statements
                        for (int i = 0; i < usersList.size(); i++) {
                            groupMembers.add(usersList.get(i));
                        }
                    }
                }
            });
            return groupMembers;
    }

    protected ArrayList<String> userList () {
        final ParseUser user = ParseUser.getCurrentUser();
        final ArrayList<String> members = new ArrayList<>();
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
                    for (int i = 0; i < usersList.size(); i++) {
                        members.add(usersList.get(i).getUsername());
                    }
                    Log.i("test", "got the users");
                }
            }
        });

        return members;
    }
}