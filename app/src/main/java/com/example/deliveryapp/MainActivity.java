
package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Password validation flags
    private boolean isChar8 = false;
    private boolean isLetter3 = false;
    private boolean isUpper = false;
    private boolean isNum = false;
    private boolean isSpecial = false;

    private FirebaseAuth auth;
    private DatabaseReference dbRef;

    private EditText username, email, password;
    private EditText userHeight, userWeight, userAge;

    private Spinner userSex, activityLevel, weightGoal;

    private TextView atleast8char, letters3;
    private TextView upperCase, numerics, specials;
    private TextView textDailyCalories;

    private Button signUp;

    private int calculatedCalories = 0;

    private final String[] sexOptions = {
            "Select an option",
            "Male",
            "Female"
    };

    private final String[] activityOptions = {
            "Select activity level",
            "Sedentary",
            "Lightly active",
            "Moderately active",
            "Very active",
            "Extra active"
    };

    private final String[] goalOptions = {
            "Select your goal",
            "Maintain weight",
            "Lose weight",
            "Gain weight"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Firebase
        auth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase
                .getInstance()
                .getReference("Users");

        // Existing UI elements
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        userHeight = findViewById(R.id.userHeight);
        userWeight = findViewById(R.id.userWeight);
        userAge = findViewById(R.id.userAge);

        atleast8char = findViewById(R.id.atLeast8CharTV);
        letters3 = findViewById(R.id.letters3TV);
        upperCase = findViewById(R.id.upperCaseTV);
        numerics = findViewById(R.id.numerics3TV);
        specials = findViewById(R.id.specialCharTV);

        signUp = findViewById(R.id.signUp);

        // New nutrition fields
        userSex = findViewById(R.id.userSex);
        activityLevel = findViewById(R.id.activityLevel);
        weightGoal = findViewById(R.id.weightGoal);
        textDailyCalories =
                findViewById(R.id.textDailyCalories);

        setupSpinners();
        setupPasswordValidation();
        setupCalorieListeners();
        setupSignupButton();
    }

    // ---------------------------------------
    // SPINNER SETUP
    // ---------------------------------------

    private void setupSpinners() {

        setupSpinner(userSex, sexOptions);
        setupSpinner(activityLevel, activityOptions);
        setupSpinner(weightGoal, goalOptions);

        android.widget.AdapterView.OnItemSelectedListener listener =
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {
                        calculateAndDisplayCalories();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent
                    ) {
                    }
                };

        userSex.setOnItemSelectedListener(listener);
        activityLevel.setOnItemSelectedListener(listener);
        weightGoal.setOnItemSelectedListener(listener);
    }

    private void setupSpinner(
            Spinner spinner,
            String[] options
    ) {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        options
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);
    }

    // ---------------------------------------
    // PASSWORD VALIDATION
    // ---------------------------------------

    private void setupPasswordValidation() {

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start,
                    int count, int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start,
                    int before, int count
            ) {

                String pass = s.toString();

                int letterCount = 0;
                int upperCount = 0;
                int numberCount = 0;
                int specialCount = 0;

                for (int i = 0; i < pass.length(); i++) {

                    char c = pass.charAt(i);

                    if (Character.isLetter(c)) {
                        letterCount++;
                    }

                    if (Character.isUpperCase(c)) {
                        upperCount++;
                    }

                    if (Character.isDigit(c)) {
                        numberCount++;
                    }

                    if (!Character.isLetterOrDigit(c)) {
                        specialCount++;
                    }
                }

                // Update flags every time the text changes
                isChar8 = pass.length() >= 8;
                isLetter3 = letterCount >= 3;
                isUpper = upperCount >= 1;
                isNum = numberCount >= 3;
                isSpecial = specialCount >= 1;

                updateValidationColor(atleast8char, isChar8);
                updateValidationColor(letters3, isLetter3);
                updateValidationColor(upperCase, isUpper);
                updateValidationColor(numerics, isNum);
                updateValidationColor(specials, isSpecial);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateValidationColor(
            TextView textView,
            boolean valid
    ) {

        if (valid) {
            textView.setTextColor(
                    android.graphics.Color.rgb(39, 140, 83)
            );
        } else {
            textView.setTextColor(
                    android.graphics.Color.rgb(190, 65, 65)
            );
        }
    }

    // ---------------------------------------
    // CALORIE INPUT LISTENERS
    // ---------------------------------------

    private void setupCalorieListeners() {

        TextWatcher watcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start,
                    int count, int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start,
                    int before, int count
            ) {
                calculateAndDisplayCalories();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        userAge.addTextChangedListener(watcher);
        userHeight.addTextChangedListener(watcher);
        userWeight.addTextChangedListener(watcher);
    }

    // ---------------------------------------
    // DAILY CALORIE CALCULATION
    // ---------------------------------------

    private boolean calculateAndDisplayCalories() {

        calculatedCalories = 0;

        try {

            int age = Integer.parseInt(
                    userAge.getText().toString().trim()
            );

            double height = Double.parseDouble(
                    userHeight.getText().toString().trim()
            );

            double weight = Double.parseDouble(
                    userWeight.getText().toString().trim()
            );

            int sexPosition =
                    userSex.getSelectedItemPosition();

            int activityPosition =
                    activityLevel.getSelectedItemPosition();

            int goalPosition =
                    weightGoal.getSelectedItemPosition();

            // Basic validation for adult estimates
            if (age < 18 || age > 120
                    || height <= 0 || height > 250
                    || weight <= 0 || weight > 500
                    || sexPosition == 0
                    || activityPosition == 0
                    || goalPosition == 0) {

                textDailyCalories.setText(
                        "Not calculated yet"
                );

                return false;
            }

            // Mifflin-St Jeor BMR
            double bmr = 10 * weight
                    + 6.25 * height
                    - 5 * age;

            if (sexPosition == 1) {
                // Male
                bmr += 5;
            } else {
                // Female
                bmr -= 161;
            }

            double multiplier = 1.2;

            switch (activityPosition) {

                case 1:
                    multiplier = 1.2;
                    break;

                case 2:
                    multiplier = 1.375;
                    break;

                case 3:
                    multiplier = 1.55;
                    break;

                case 4:
                    multiplier = 1.725;
                    break;

                case 5:
                    multiplier = 1.9;
                    break;
            }

            double dailyCalories = bmr * multiplier;

            // Simple project goal adjustment
            if (goalPosition == 2) {
                dailyCalories -= 300;
            } else if (goalPosition == 3) {
                dailyCalories += 300;
            }

            // Do not automatically assign a restrictive
            // target when the estimate is too low.
            if (dailyCalories < 1500) {

                textDailyCalories.setText(
                        "Please set a target with guidance"
                );

                return false;
            }

            calculatedCalories =
                    (int) Math.round(dailyCalories);

            textDailyCalories.setText(
                    String.format(
                            Locale.CANADA,
                            "%,d kcal/day",
                            calculatedCalories
                    )
            );

            return true;

        } catch (NumberFormatException e) {

            textDailyCalories.setText(
                    "Not calculated yet"
            );

            return false;
        }
    }

    // ---------------------------------------
    // SIGNUP BUTTON
    // ---------------------------------------

    private void setupSignupButton() {

        signUp.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        registerUser();
                    }
                }
        );
    }

    private void registerUser() {

        final String userNameS =
                username.getText().toString().trim();

        final String emailS =
                email.getText().toString().trim();

        final String passwordS =
                password.getText().toString();

        final String userHeightS =
                userHeight.getText().toString().trim();

        final String userWeightS =
                userWeight.getText().toString().trim();

        final String userAgeS =
                userAge.getText().toString().trim();

        // Validate account details
        if (userNameS.isEmpty()) {
            username.setError("Enter your name");
            username.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(emailS).matches()) {

            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if (!(isChar8 && isLetter3
                && isUpper && isNum && isSpecial)) {

            Toast.makeText(
                    this,
                    "Password does not meet all requirements",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // Validate nutrition details
        if (!calculateAndDisplayCalories()) {

            Toast.makeText(
                    this,
                    "Enter valid nutrition details. "
                            + "This estimate is for adults.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        final String sexS =
                userSex.getSelectedItem().toString();

        final String activityS =
                activityLevel.getSelectedItem().toString();

        final String goalS =
                weightGoal.getSelectedItem().toString();

        final int dailyCalorieTarget =
                calculatedCalories;

        // Prevent repeated submissions
        signUp.setEnabled(false);

        auth.createUserWithEmailAndPassword(
                emailS, passwordS
        ).addOnCompleteListener(
                MainActivity.this,
                new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(
                            @NonNull Task<AuthResult> task
                    ) {

                        if (!task.isSuccessful()) {

                            signUp.setEnabled(true);

                            String error = task.getException() == null
                                    ? "Unknown error"
                                    : task.getException().getMessage();

                            Toast.makeText(
                                    MainActivity.this,
                                    "Signup failed: " + error,
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        FirebaseUser firebaseUser =
                                auth.getCurrentUser();

                        if (firebaseUser == null) {

                            signUp.setEnabled(true);

                            Toast.makeText(
                                    MainActivity.this,
                                    "Unable to retrieve user ID",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        String userID = firebaseUser.getUid();

                        // Save profile data only.
                        // Never store passwords or card details.
                        Map<String, Object> userData =
                                new HashMap<>();

                        userData.put("username", userNameS);
                        userData.put("email", emailS);

                        // Existing physical-data fields
                        userData.put("height", userHeightS);
                        userData.put("weight", userWeightS);
                        userData.put("age", userAgeS);

                        // New nutrition fields
                        userData.put("sex", sexS);
                        userData.put("activityLevel", activityS);
                        userData.put("goal", goalS);

                        userData.put(
                                "dailyCalorieTarget",
                                dailyCalorieTarget
                        );

                        dbRef.child(userID)
                                .setValue(userData)
                                .addOnCompleteListener(
                                        new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(
                                                    @NonNull Task<Void> saveTask
                                            ) {

                                                signUp.setEnabled(true);

                                                if (saveTask.isSuccessful()) {

                                                    Toast.makeText(
                                                            MainActivity.this,
                                                            "Account created successfully",
                                                            Toast.LENGTH_LONG
                                                    ).show();

                                                    // Sign out so Login page
                                                    // starts a fresh session
                                                    auth.signOut();

                                                    Intent intent =
                                                            new Intent(
                                                                    MainActivity.this,
                                                                    Login.class
                                                            );

                                                    startActivity(intent);
                                                    finish();

                                                } else {

                                                    Toast.makeText(
                                                            MainActivity.this,
                                                            "Account created, but "
                                                                    + "profile could not be saved. "
                                                                    + "Please retry or contact support.",
                                                            Toast.LENGTH_LONG
                                                    ).show();
                                                }
                                            }
                                        }
                                );
                    }
                }
        );
    }
}
