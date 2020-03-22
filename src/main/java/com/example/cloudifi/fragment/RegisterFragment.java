package com.example.cloudifi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudifi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private NavController navController;
    private TextView goLogin;
    private TextView progressTxt;
    private ProgressBar progressBar;
    private EditText mEmail;
    private EditText mPassword1;
    private EditText mpassword2;
    private Button registerBtn;
    private EditText username;
    private EditText status;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentuser;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //firebase instance
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //navigation and ui elements
        goLogin = view.findViewById(R.id.logintxt);
        navController = Navigation.findNavController(view);
        mEmail = view.findViewById(R.id.register_email);
        mPassword1 = view.findViewById(R.id.regiter_password1);
        mpassword2 = view.findViewById(R.id.regiter_password2);
        registerBtn = view.findViewById(R.id.register_btn);
        progressBar = view.findViewById(R.id.reg_progress);
        progressTxt = view.findViewById(R.id.reg_progress_txt);
        username = view.findViewById(R.id.register_username);
        status = view.findViewById(R.id.register_status);

        //listner for the back text
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                progressTxt.setVisibility(View.VISIBLE);
                progressTxt.setText("Initializing...");

                String email = mEmail.getText().toString();
                String password1 = mPassword1.getText().toString();
                String password2 = mpassword2.getText().toString();
                final String user_name = username.getText().toString();
                final String status_string = status.getText().toString();

                if (!email.isEmpty() && password1.equals(password2) & !user_name.isEmpty() & !status_string.isEmpty()) {

                    progressTxt.setText("creating account...");

                    mAuth.createUserWithEmailAndPassword(email, password1)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressTxt.setText("Almost there...");

                                    if (task.isSuccessful()) {

                                        currentuser = mAuth.getCurrentUser();

                                        // Sign in success, update UI with the signed-in user's information
                                        Map<String, String> userdetail = new HashMap<>();
                                        userdetail.put("username", user_name);
                                        userdetail.put("status", status_string);
                                        userdetail.put("registered_date", FieldValue.serverTimestamp().toString());
                                        userdetail.put("last_seen", "Not yet Connected");

                                        if (currentuser != null) {

                                            firebaseFirestore.collection("UserDetails").document(currentuser.getUid())
                                                    .set(userdetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                        navController.navigate(R.id.action_registerFragment_to_splashScreenActivity);
                                                    else
                                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getContext(), "Couldn't save your data ", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getContext(), task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                    progressTxt.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                } else {
                    Toast.makeText(getContext(), "enter your forms correctly and try again", Toast.LENGTH_LONG).show();
                    progressTxt.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }


}
