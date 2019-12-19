package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {

        if (edtDriverOrPassenger.getText().toString().equals("Driver") || edtDriverOrPassenger.getText().toString().equals("Passenger")) {
            if (ParseUser.getCurrentUser() == null) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {

                            FancyToast.makeText(SignUp.this, "Anonymous User", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                            user.put("as", edtDriverOrPassenger.getText().toString());
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    transitionToPassengerActivity();
                                }
                            });

                        }
                    }
                });
            }
        }
    }

    enum State {
        SIGNUP, LOGIN
    }

    private State state;
    private Button btnSignUpLogin;
    private Button btnOneTimeLogin;
    private RadioButton driverRadioButton;
    private RadioButton passengerRadioButton;
    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtDriverOrPassenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        if (ParseUser.getCurrentUser() != null) {
            // transition
            // ParseUser.logOut();
            transitionToPassengerActivity();
            //transitionToDriverRequestListActivity();
        }

        btnSignUpLogin = findViewById(R.id.btnSignUpLogin);
        driverRadioButton = findViewById(R.id.rdbDriver);
        passengerRadioButton = findViewById(R.id.rdbPassenger);
        btnOneTimeLogin = findViewById(R.id.btnOneTimeLogin);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtDriverOrPassenger = findViewById(R.id.edtDOrP);

        btnOneTimeLogin.setOnClickListener(this);

        state = State.SIGNUP;

        btnSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state == State.SIGNUP) {
                    if (driverRadioButton.isChecked() == false && passengerRadioButton.isChecked() == false) {
                        FancyToast.makeText(SignUp.this, "Are you a driver or a passenger ?!!", Toast.LENGTH_SHORT, FancyToast.WARNING, true).show();
                        return;
                    }
                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(edtUserName.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());
                    if (driverRadioButton.isChecked()) {
                        appUser.put("as", "Driver");
                    } else if (passengerRadioButton.isChecked()) {
                        appUser.put("as", "Passenger");
                    }
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUp.this, "Signed up !", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                //transitionToPassengerActivity();
                            }
                        }
                    });

                } else if (state == State.LOGIN) {
                    ParseUser.logInInBackground(edtUserName.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(SignUp.this, "Logged in !", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                               transitionToPassengerActivity();
                            }
                        }
                    });
                }
            }
        });
    }

//    private void transitionToDriverRequestListActivity() {
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.loginItem:
                if (state == State.SIGNUP) {
                    state = State.LOGIN;
                    item.setTitle("SIGN UP");
                    btnSignUpLogin.setText("LOGIN");

                } else if (state == State.LOGIN) {
                    state = State.SIGNUP;
                    item.setTitle("LOGIN");
                    btnSignUpLogin.setText("SIGN UP");
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void transitionToPassengerActivity() {
        if (ParseUser.getCurrentUser() != null) {

            if (ParseUser.getCurrentUser().get("as").equals("Passenger")) {
                Intent intent = new Intent(SignUp.this, PassengerActivity.class);
                startActivity(intent);
            }
        }
    }
}
