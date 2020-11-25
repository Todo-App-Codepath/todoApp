package com.example.chorewheel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccountActivity";
    private EditText etEmail;
    private EditText etFname;
    private EditText etLname;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etGroupID;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etEmail = findViewById(R.id.etEmail);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etGroupID = findViewById(R.id.etGroupID);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Create Account button");
                final String email = etEmail.getText().toString();
                final String firstName = etFname.getText().toString();
                final String lastName = etLname.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String groupID = etGroupID.getText().toString();

                if (groupID.equals("")) {
                    createAccount(email,firstName, lastName, username, password);
                    finish();
                }
                else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
                    query.whereEqualTo("objectId", groupID);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (object == null) {
                                Toast.makeText(CreateAccountActivity.this, "Group ID doesn't exist!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                createAccount(email,firstName, lastName, username, password);
                                ParseUser user = ParseUser.getCurrentUser();
                                ParseObject group = object;
                                addUserToGroup(group, user);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void createAccount(final String email, final String firstName, final String lastName, final String username, final String password) {
        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    user.put("email", email);
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.saveInBackground();
                    Toast.makeText(CreateAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Create Account Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserToGroup(final ParseObject group, final ParseUser user) {
        ParseObject groupsUsers = new ParseObject("GroupsUsers");
        groupsUsers.put("userID", user);
        groupsUsers.put("groupID", group);
        groupsUsers.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "New User added to Group List");
                }
                else {
                    Log.e(TAG, "Error occurred: User not added to Group.");
                }
            }
        });
    }

    // Reroute to login page once an account is created.
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);    //change to LoginActivity once it's created.
        startActivity(i);
    }
}