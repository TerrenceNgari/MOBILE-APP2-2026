package com.example.deliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {

    private TextView textAvatar;
    private TextView textProfileName;
    private TextView textProfileEmail;
    private TextView textProfileHeight;
    private TextView textProfileWeight;
    private TextView textProfileGender;
    private TextView textProfileGoal;
    private TextView textProfileTarget;

    private DatabaseReference userRef;

    private String profileName = "";
    private String profileEmail = "";
    private String heightCm = "";
    private String weightKg = "";
    private String gender = "Not specified";
    private String goal = "Maintain";

    // Actual Firebase keys used by the current user record
    private String nameKey = "username";
    private String heightKey = "height";
    private String weightKey = "weight";
    private String genderKey = "gender";
    private String goalKey = "goal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_page);

        initializeViews();
        setupButtons();
        loadProfile();
    }

    private void initializeViews() {

        textAvatar = findViewById(R.id.textAvatar);
        textProfileName = findViewById(R.id.textProfileName);
        textProfileEmail = findViewById(R.id.textProfileEmail);
        textProfileHeight = findViewById(R.id.textProfileHeight);
        textProfileWeight = findViewById(R.id.textProfileWeight);
        textProfileGender = findViewById(R.id.textProfileGender);
        textProfileGoal = findViewById(R.id.textProfileGoal);
        textProfileTarget = findViewById(R.id.textProfileTarget);
    }

    private void setupButtons() {

        findViewById(R.id.btnBack).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        // Open Order History
        findViewById(R.id.btnHistory).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                ProfilePage.this,
                                OrderHistoryPage.class
                        );

                        startActivity(intent);
                    }
                }
        );

        // Edit Profile
        findViewById(R.id.btnEdit).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditProfile();
                    }
                }
        );

        // Logout
        findViewById(R.id.btnLogout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmLogout();
                    }
                }
        );

        // Delete account (not yet implemented securely)
        findViewById(R.id.btnDelete).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(ProfilePage.this)
                                .setTitle("Delete account")
                                .setMessage(
                                        "Account deletion is not connected yet."
                                                + "\n\nYour account and orders"
                                                + " have not been changed."
                                )
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
        );
    }

    private void loadProfile() {

        FirebaseUser user =
                FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(
                    this,
                    "Please log in to view your profile",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        profileEmail = user.getEmail() == null
                ? "" : user.getEmail();

        profileName = user.getDisplayName() == null
                ? "" : user.getDisplayName();

        displayProfile();

        userRef = FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(user.getUid());

        userRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot
                    ) {

                        if (!snapshot.exists()) {
                            displayProfile();
                            return;
                        }

                        nameKey = findKey(
                                snapshot, "username",
                                "userName", "name", "fullName"
                        );

                        heightKey = findKey(
                                snapshot, "height", "heightCm"
                        );

                        weightKey = findKey(
                                snapshot, "weight", "weightKg"
                        );

                        genderKey = findKey(
                                snapshot, "gender", "sex"
                        );

                        goalKey = findKey(
                                snapshot, "goal", "fitnessGoal"
                        );

                        String savedName = readString(
                                snapshot, nameKey
                        );

                        String savedEmail = readString(
                                snapshot, "email"
                        );

                        if (!savedName.isEmpty()) {
                            profileName = savedName;
                        }

                        if (!savedEmail.isEmpty()) {
                            profileEmail = savedEmail;
                        }

                        heightCm = readString(
                                snapshot, heightKey
                        );

                        weightKg = readString(
                                snapshot, weightKey
                        );

                        String savedGender = readString(
                                snapshot, genderKey
                        );

                        String savedGoal = readString(
                                snapshot, goalKey
                        );

                        if (!savedGender.isEmpty()) {
                            gender = savedGender;
                        }

                        if (!savedGoal.isEmpty()) {
                            goal = savedGoal;
                        }

                        displayProfile();
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error
                    ) {

                        Toast.makeText(
                                ProfilePage.this,
                                "Unable to load profile: "
                                        + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void displayProfile() {

        if (profileName.trim().isEmpty()) {
            profileName = "MacroBite User";
        }

        textProfileName.setText(profileName);
        textProfileEmail.setText(profileEmail);

        textProfileHeight.setText(
                heightCm.isEmpty() ? "Not set" : heightCm + " cm"
        );

        textProfileWeight.setText(
                weightKg.isEmpty() ? "Not set" : weightKg + " kg"
        );

        textProfileGender.setText(gender);

        textProfileGoal.setText(
                goal.equalsIgnoreCase("Maintain")
                        ? "Maintain weight"
                        : goal
        );

        // Sample target, consistent with current CartPage
        textProfileTarget.setText(
                "Sample goal: 2000 kcal per day"
        );

        StringBuilder initials = new StringBuilder();

        String[] words = profileName.trim().split("\\s+");

        for (String word : words) {

            if (!word.isEmpty() && initials.length() < 2) {
                initials.append(
                        Character.toUpperCase(word.charAt(0))
                );
            }
        }

        textAvatar.setText(
                initials.length() == 0
                        ? "U"
                        : initials.toString()
        );
    }

    private void showEditProfile() {

        View form = getLayoutInflater().inflate(
                R.layout.dialog_edit_profile,
                null,
                false
        );

        final EditText editName =
                form.findViewById(R.id.editName);

        final EditText editEmail =
                form.findViewById(R.id.editEmail);

        final EditText editHeight =
                form.findViewById(R.id.editHeight);

        final EditText editWeight =
                form.findViewById(R.id.editWeight);

        final Spinner spinnerGender =
                form.findViewById(R.id.spinnerGender);

        final Spinner spinnerGoal =
                form.findViewById(R.id.spinnerGoal);

        editName.setText(profileName);
        editEmail.setText(profileEmail);
        editHeight.setText(heightCm);
        editWeight.setText(weightKg);

        // Email changes require Firebase Auth handling.
        // Display it but do not edit it here.
        editEmail.setEnabled(false);

        String[] genders = {
                "Not specified", "Female", "Male", "Other"
        };

        String[] goals = {
                "Maintain", "Lose weight", "Gain weight"
        };

        setupSpinner(spinnerGender, genders, gender);
        setupSpinner(spinnerGoal, goals, goal);

        final AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Edit profile")
                        .setView(form)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Save", null)
                        .create();

        dialog.setOnShowListener(
                new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface ignored) {

                        dialog.getButton(
                                AlertDialog.BUTTON_POSITIVE
                        ).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String name = editName
                                                .getText().toString().trim();

                                        String height = editHeight
                                                .getText().toString().trim();

                                        String weight = editWeight
                                                .getText().toString().trim();

                                        if (name.isEmpty()) {
                                            editName.setError(
                                                    "Name is required"
                                            );
                                            return;
                                        }

                                        if (!validPositive(height)) {
                                            editHeight.setError(
                                                    "Enter a valid height"
                                            );
                                            return;
                                        }

                                        if (!validPositive(weight)) {
                                            editWeight.setError(
                                                    "Enter a valid weight"
                                            );
                                            return;
                                        }

                                        saveProfile(
                                                name,
                                                height,
                                                weight,
                                                spinnerGender
                                                        .getSelectedItem()
                                                        .toString(),
                                                spinnerGoal
                                                        .getSelectedItem()
                                                        .toString(),
                                                dialog
                                        );
                                    }
                                }
                        );
                    }
                }
        );

        dialog.show();
    }

    private void saveProfile(
            final String name,
            final String height,
            final String weight,
            final String selectedGender,
            final String selectedGoal,
            final AlertDialog dialog
    ) {

        if (userRef == null) {
            Toast.makeText(
                    this,
                    "Profile is not ready",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();

        updates.put(nameKey, name);
        updates.put(heightKey, height);
        updates.put(weightKey, weight);
        updates.put(genderKey, selectedGender);
        updates.put(goalKey, selectedGoal);

        userRef.updateChildren(updates)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<Void> task
                            ) {

                                if (task.isSuccessful()) {

                                    profileName = name;
                                    heightCm = height;
                                    weightKg = weight;
                                    gender = selectedGender;
                                    goal = selectedGoal;

                                    displayProfile();
                                    dialog.dismiss();

                                    Toast.makeText(
                                            ProfilePage.this,
                                            "Profile updated",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } else {

                                    Toast.makeText(
                                            ProfilePage.this,
                                            "Unable to save profile",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            }
                        }
                );
    }

    private void setupSpinner(
            Spinner spinner,
            String[] options,
            String selected
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

        for (int i = 0; i < options.length; i++) {

            if (options[i].equalsIgnoreCase(selected)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void confirmLogout() {

        new AlertDialog.Builder(this)
                .setTitle("Log out?")
                .setMessage(
                        "You will need to sign in again"
                                + " to access your account."
                )
                .setNegativeButton("Cancel", null)
                .setPositiveButton(
                        "Log out",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which
                            ) {

                                CartManager.clearCart();

                                FirebaseAuth.getInstance()
                                        .signOut();

                                Intent intent = new Intent(
                                        ProfilePage.this,
                                        StartPage.class
                                );

                                intent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                );

                                startActivity(intent);
                            }
                        }
                )
                .show();
    }

    private boolean validPositive(String value) {

        try {
            return Double.parseDouble(value) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String readString(
            DataSnapshot snapshot,
            String key
    ) {

        Object value = snapshot.child(key).getValue();

        return value == null
                ? ""
                : String.valueOf(value);
    }

    private String findKey(
            DataSnapshot snapshot,
            String... possibleKeys
    ) {

        for (String key : possibleKeys) {

            if (snapshot.hasChild(key)) {
                return key;
            }
        }

        return possibleKeys[0];
    }
}
