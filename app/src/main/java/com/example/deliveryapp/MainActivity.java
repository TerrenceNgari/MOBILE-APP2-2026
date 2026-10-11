package com.example.deliveryapp;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    //Flags
    boolean isChar8 = false;
    boolean isLetter3 = false;
    boolean isUpper = false;
    boolean isNum = false;
    boolean isSpecial = false;

    //Firebase Authentication
    private FirebaseAuth auth;
    //Firebase Database
    private FirebaseDatabase fdb;
    //Database reference
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Firebase Authentication: Initialization
        auth = FirebaseAuth.getInstance(); //Allow access to firebase Authentication services
        //initialise database
        fdb = FirebaseDatabase.getInstance();
        //initialize database reference
        dbRef = fdb.getReference("Users");

        //UI elements
        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        TextView atleast8char = findViewById(R.id.atLeast8CharTV);
        TextView letters3 = findViewById(R.id.letters3TV);
        TextView upperCase = findViewById(R.id.upperCaseTV);
        TextView numerics = findViewById(R.id.numerics3TV);
        TextView specials = findViewById(R.id.specialCharTV);
        //Card Details
        EditText cardName = findViewById(R.id.cardName);
        EditText cardNumber = findViewById(R.id.cardNumber);
        EditText cardExp = findViewById(R.id.cardExp);
        EditText cardCVV = findViewById(R.id.cardCVV);
        //User details- Physical data
        EditText userHeight = findViewById(R.id.userHeight);
        EditText userWeight = findViewById(R.id.userWeight);
        EditText userAge = findViewById(R.id.userAge);
        //Signup Button
        Button signUp = findViewById(R.id.signUp);

        //Password Validation
        password.addTextChangedListener(new TextWatcher() { //Add text changed Listener
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //When user types in the password field, During typing...
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = charSequence.toString();
                //At least 8 characters
                if(pass.length() >= 8){
                    atleast8char.setTextColor(getColor(R.color.green));
                    isChar8 = true;
                }else {
                    atleast8char.setTextColor(getColor(R.color.red));
                }

                //At least 3 letters
                int letterCount = 0;
                for(int j=0; j<pass.length(); j++){
                    if(Character.isLetter(pass.charAt(j))){
                        letterCount++;
                    }
                }
                if(letterCount >= 3){
                    letters3.setTextColor(getColor(R.color.green));
                    isLetter3 = true;
                }else{
                    letters3.setTextColor(getColor(R.color.red));
                }

                //Uppercase letters
                int uppers = 0;
                for(int j=0; j<pass.length(); j++){
                    if(Character.isUpperCase(pass.charAt(j))){
                        uppers++;
                    }
                }
                if(uppers >= 1){
                    upperCase.setTextColor(getColor(R.color.green));
                    isUpper = true;
                }else{
                    upperCase.setTextColor(getColor(R.color.red));
                }

                //At least 3 Numerics
                int nums = 0;
                for(int j=0; j<pass.length(); j++){
                    if(Character.isDigit(pass.charAt(j))){
                        nums++;
                    }
                }
                if(nums >= 3){
                    numerics.setTextColor(getColor(R.color.green));
                    isNum = true;
                }else{
                    numerics.setTextColor(getColor(R.color.red));
                }

                //Special Characters
                int specialChars = 0;
                for(int j=0; j<pass.length(); j++){
                    if(!Character.isLetter(pass.charAt(j)) && !Character.isDigit(pass.charAt(j))){
                        specialChars++;
                    }
                }
                if(specialChars >= 1){
                    specials.setTextColor(getColor(R.color.green));
                    isSpecial = true;
                }else{
                    specials.setTextColor(getColor(R.color.red));
                }

            }
        });

        //SignUp Button Click
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Retrieve values
                String userNameS = username.getText().toString();
                String emailS = email.getText().toString().trim();
                //Payment card details
                String cardNameS = cardName.getText().toString();
                String cardNumberS = cardNumber.getText().toString();
                String cardExpS = cardExp.getText().toString();
                String cardCVVS = cardCVV.getText().toString();
                //User Physical data
                String userHeightS = userHeight.getText().toString();
                String userWeightS = userWeight.getText().toString();
                String userAgeS = userAge.getText().toString();
                //Validate inputs
                if(isChar8 && isLetter3 && isUpper && isNum && isSpecial){
                    Toast.makeText(getApplicationContext(),"Valid", LENGTH_SHORT).show();
                    String passwordS = password.getText().toString();

                    //Create user account using Firebase Authentication
                    auth.createUserWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(MainActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        //Get unique ID created by firebase Authentication
                                        String userID = auth.getCurrentUser().getUid();
                                        //Create user object from UserModel class
                                        UserModel user = new UserModel(userNameS, emailS, userHeightS, userWeightS, userAgeS);
                                        //write to database
                                        //Save the user under Users/userID
                                        dbRef.child(userID).setValue(user)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(getApplicationContext(),"Account Created Successfully", LENGTH_LONG).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(),"Error!", LENGTH_LONG).show();
                                                            }
                                                        });
                                    }else{
                                        Toast.makeText(getApplicationContext(),"SignUp Failed!", LENGTH_LONG).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(getApplicationContext(), "Invalid", LENGTH_SHORT).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}