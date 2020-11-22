package com.example.chorewheel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccountActivity";
    private EditText etEmail;
    private EditText etFname;
    private EditText etLname;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etGroupName;
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
        etGroupName = findViewById(R.id.etGroupName);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Create Account button");
                String email = etEmail.getText().toString();
                String firstName = etFname.getText().toString();
                String lastName = etLname.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String group = etGroupName.getText().toString();
                createAccount(email, firstName, lastName, username, password, group);
            }
        });
    }

    private void createAccount(final String email, final String firstName, final String lastName, final String username, final String password, final String group) {
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
                    //ParseUser newUser = ParseUser.getCurrentUser();
                    user.saveInBackground();
                    createGroup(group, user);
                    Toast.makeText(CreateAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(CreateAccountActivity.this, "Create Account Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createGroup(final String groupName, ParseUser user) {
        ParseObject group = new ParseObject("Group");
        group.put("name", groupName);
        group.saveInBackground();

        ParseObject groupsUsers = new ParseObject("GroupsUsers");
        groupsUsers.put("userID", user);
        groupsUsers.put("groupID", group);
        groupsUsers.saveInBackground();
    }

    // Reroute to login page once an account is created.
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);    //change to LoginActivity once it's created.
        startActivity(i);
    }
}