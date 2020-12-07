package com.example.chorewheel.Fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.chorewheel.R;
import com.example.chorewheel.models.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class InfoTaskFragment extends DialogFragment {

    public static final String TAG = "InfoTaskFragment";

    // XML elements
    private EditText etInfoTaskName;
    private EditText etInfoTaskDate;
    private EditText etInfoTaskDescription;
    private Button btnEditSaveTask;
    private Spinner spinnerInfoPerson;

    private Task task;

    public InfoTaskFragment() {
        // Required empty public constructor
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    public static InfoTaskFragment newInstance(String title) {
        InfoTaskFragment frag = new InfoTaskFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_info_task, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etInfoTaskName = view.findViewById(R.id.etInfoTaskName);
        etInfoTaskDate = view.findViewById(R.id.etdInfoTaskDate);
        etInfoTaskDescription = view.findViewById(R.id.etInfoTaskNotes);
        btnEditSaveTask = view.findViewById(R.id.btnEditSaveTask);

        spinnerInfoPerson = view.findViewById(R.id.infoSpinnerPerson);
        List<ParseUser> members = new ArrayList<>();
        final ArrayList<String> userList = userList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInfoPerson.setAdapter(adapter);
        userList.add(0, "Assign to a different member...");
        spinnerInfoPerson.setSelection(0);
        adapter.notifyDataSetChanged();
        final int[] selection = new int[1];
        spinnerInfoPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selection[0] = 0;
            }
        });

        // Set text boxes to current (detail view) task information
        etInfoTaskName.setText(task.getTaskName());
        etInfoTaskDate.setText(task.getFormattedDate());
        etInfoTaskDescription.setText(task.getDESCRIPTION());

        // Edit Task Button (Save)
        final List<ParseUser> finalMembers = getUserID();
        btnEditSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit Name
                task.setTaskName(etInfoTaskName.getText().toString());
                // Edit Date
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = etInfoTaskDate.getText().toString();
                try {
                    Date dateObject = dateFormat.parse(dateString);
                    task.put("dueDate", dateObject);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                // Edit Description
                task.setDescription(etInfoTaskDescription.getText().toString());

                // Edit Member Assignment
                if (selection[0] == 0) {
                   task.put("userID", ParseUser.getCurrentUser());
                } else {
                    ParseUser memberSelected = new ParseUser();
                    for (int i = 0; i < finalMembers.size(); i++) {
                        if (finalMembers.get(i).getUsername().equals(userList.get(selection[0]))) {
                            memberSelected = finalMembers.get(i);
                        }
                    }
                    task.put("userID", memberSelected);
                }

                // Add all edits
               task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while editing Task! ", e);
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