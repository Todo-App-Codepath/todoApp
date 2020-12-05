package com.example.chorewheel.Fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.chorewheel.R;
import com.example.chorewheel.models.Task;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class InfoTaskFragment extends DialogFragment {

    public static final String TAG = "InfoTaskFragment";

    // XML elements
    private EditText etInfoTaskName;
    private EditText etInfoTaskDate;
    private EditText etInfoTaskDescription;
    private Button btnEditSaveTask;

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

        // Set text boxes to current (detail view) task information
        etInfoTaskName.setText(task.getTaskName());
        etInfoTaskDate.setText(task.getFormattedDate());
        etInfoTaskDescription.setText(task.getDESCRIPTION());

        // Edit Task Button (Save)
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


}