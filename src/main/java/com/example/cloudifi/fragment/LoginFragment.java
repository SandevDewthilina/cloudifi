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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private NavController navController;
    private TextView goReg;
    private EditText mEmail;
    private EditText mPassword;
    private Button loginBtn;
    private ProgressBar progressBar;
    private TextView progressTxt;

    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // navigation and ui elements
        goReg = view.findViewById(R.id.regtext);
        navController = Navigation.findNavController(view);
        mEmail = view.findViewById(R.id.login_email);
        mPassword = view.findViewById(R.id.login_password1);
        loginBtn = view.findViewById(R.id.login_btn);
        progressBar = view.findViewById(R.id.login_progress);
        progressTxt = view.findViewById(R.id.login_progress_txt);

        //set listner for the back text
        goReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                progressTxt.setVisibility(View.VISIBLE);

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (!email.isEmpty() & !password.isEmpty()) {

                    progressTxt.setText("Login your account...");

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressTxt.setText("Almost there...");

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        navController.navigate(R.id.action_loginFragment_to_splashScreenActivity);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    progressTxt.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "fill your forms correctly", Toast.LENGTH_LONG).show();
                    progressTxt.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }
}
