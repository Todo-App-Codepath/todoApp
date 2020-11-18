# Chore Wheel

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Purpose: A todo list app that can be used to between a group of people to keep track of chores and assign chores to other group members.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Productivity / Social Media
- **Mobile:** This app would be primary developed for mobile but could be used across other platforms.
- **Story:** Allows users to join a group and create, assign, and keep track of tasks within that group.
- **Market:** Households, people who live together, or anyone who shares chores with a group.
- **Habit:** This app should be used daily or weekly in order to be useful so that chores don't get missed.
- **Scope:** The app is currently going to be built for small groups of household users. At a later time, it could be adapted for larger organizational use.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* As a user I want to see my list of tasks 
* As a user I want to make a task
* As a user I want to add a due date for the tasks
* As a user I want to be able to create an account
* As a user I want to be able sign in and sign out
* As a user I want to see other user's tasks, filter task
* As a user I want to be able to edit a task
* As a user I want to be able to add an avatar to my profile image
* As a user I want to be able to assign a task to a member
* As a user I want to get notified of a task that's due


**Optional Nice-to-have Stories**

* As a user I want to be able to view items offline(optional)
* As a user I want to be able to view items in a calendar.

### 2. Screen Archetypes

* Login / Create Account
   * The user is prompted to login or create an account when they first open the app.
* List of Tasks
   * A recycler view of the tasks assigned to the current user ***(or unassigned tasks that user can pick up - maybe?)***
* Members Screen
   * A recylcler view where the user can see the list of other users in their group
* Individual Members Screen
   * A screen that shows another member's profile and tasks that are assigned to them.
* Add New Items
   * A form that allows a user to create a new task, assign it to a user and add it to the task list.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* List of Tasks
* Add a Task
* Members Screen

**Flow Navigation** (Screen to Screen)

* Login / Create Account
   * Login redirects to create account if the user does not have an account.
   * Once the user successfully logs in, they are redirected to List of Tasks screen
* List of Tasks screen
   * Will be included on action navbar along with Add a Task and Members screen
   * Click on a task to navigate to a more details screen
* Add a Task
   * Will be included on action navbar along with List of Tasks and Members screen
   * Once a user creates a task and clicks submit navigate to List of Tasks screen
* Members Screen
   * Will be included on action navbar along with List of Tasks and Add a Task
   * Click on a member to navigate to their profile and view their tasks

## Wireframes
### Note: we used a digital media to sketch the wireframes
<img src="https://github.com/Todo-App-Codepath/todoApp/blob/main/sketch_wireframes.png" width=600>

### [BONUS] Digital Wireframes & Mockups
<img src="https://github.com/Todo-App-Codepath/todoApp/blob/main/wireframes_of_app.png" width=600>

### [BONUS] Interactive Prototype
<img src="https://github.com/Todo-App-Codepath/todoApp/blob/main/wireframes.gif" width=600>

## Schema 
[This section will be completed in Unit 9]


### Models
User
| Property       | Type    | Description     |
| :------------- | :----------: | -----------: |
|  userID        | String       | unique id for the user (default field)    |
|  name          | String       | user's prefered name |
|  email         | String       | user's email for login |
|  password      | String       | password for account |
|  image         | File         | user's profile image |

Group
| Property       | Type    | Description     |
| :------------- | :----------: | -----------: |
|  groupID       | String       | unique id for the group (default field)   |
|  name          | String       | name for the group |

GroupsUsers  (Many to Many joint table)
| Property       | Type    | Description     |
| :------------- | :----------: | -----------: |
|  groupuserID   | String       | unique id  (default field)  |
|  userID        | String       | unique id for the user |
|  groupID       | String       | unique id for the group |

Task
| Property       | Type    | Description     |
| :------------- | :----------: | -----------: |
|  taskID        | String       | unique id for the task (default field)   |
|  name          | String       | name for the task |
|  description   | String       | description of the task |
|  groupID       | String       | unique ID for the group the task belongs to |
|  userID        | String       | unique ID of the user the task is assigned to |

### Networking


#### Login Page:

- READ user
```java
ParseUser.logInInBackground(username, password, new LogInCallback() {
    @Override
        public void done(ParseUser user, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Login Error", e);
            }
            // Successful Login
                goMainActivity();
            }
});

```

#### Create an Account Page

- CREATE user
```java
// Create the ParseUser
ParseUser user = new ParseUser();
user.setUsername(userName);
user.setPassword(password);
	
user.signUpInBackground(new SignUpCallback() {
    @Override
    public void done(ParseException e) {
        // Successful signup
        if (e == null) {
               // TODO: Action after successful signup
        }
        // Unsuccessful signup
        else {
                Log.e(TAG, "Signup error", e);
                return;
        }
    }
});
```

#### Edit Profile Page 

- UPDATE user
```java
// Update user
ParseUser currentUser = ParseUser.getCurrentUser();
if (currentUser != null) {
    // Update profile image
    currentUser.put("profileImage", new ParseFile(photoFile));
    // Save profile image
    currentUser.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {
	    // Profile image change successful
        } else {
	   // Profile image change unsuccessful
	}
      }
    });
}  

```


#### Task Page 

- CREATE task
```java
// As a sample, but Task can also be a class with setters/getters
ParseObject task = new ParseObject("Task");
task.put("name", "Buy Groceries");
task.put("dueDate", date); // Date object
task.put("description", "Buy milk, eggs, and butter");
  
task.saveInBackground(new SaveCallback() {
    @Override
    public void done(ParseException e) {
        if (e != null) {
            Log.e(TAG, "Task save error", e);
         }
            Log.i(TAG, "Saved task successfully");
            }
        });
}

```
- READ task
```java
// As a sample with Task as a class
ParseQuery<Task> query = ParseQuery.getQuery(Task.class);

query.include(Task.KEY_NAME);
query.include(Task.KEY_DUEDATE);
query.include(Task.KEY_DESCRIPTION);
query.findInBackground(new FindCallback<Task>() {
	@Override
        public void done(List<Task> tasks, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with getting tasks", e);
                    return;
            } else {
		   // Successfully retrieved tasks
		  // TODO: Action after getting tasks
	    }
        }
});

```

#### Member List:

- READ members
```java
ParseQuery<ParseUser> query = ParseUser.getQuery();
// Find users that equal a particular list / membership
query.whereEqualTo("listID", 123456);
query.findInBackground(new FindCallback<ParseUser>() {
	public void done(List<ParseUser> users, ParseException e) {
		if (e == null) {
			// Found users that match the listID
		} else {
			// Error retrieving users
      }
    }
});
```
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
