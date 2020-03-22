package com.example.cloudifi.fragment;

import android.icu.text.Transliterator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloudifi.R;
import com.example.cloudifi.adapter.ChatRecyclerAdapter;
import com.example.cloudifi.model.ChatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private String userid;
    private String myId;
    private String username = "CLOUDiFi";
    private String lastseen = "Not Online";
    private Toolbar toolbar;
    private FloatingActionButton send;
    private EditText txtMsg;
    private RecyclerView chat;
    private List<ChatModel> chatModelList;
    private ChatRecyclerAdapter chatRecyclerAdapter;
    private boolean firstIsSent = false;

    //firebase connection
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ui elements
        toolbar = view.findViewById(R.id.user_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        userid = UserFragmentArgs.fromBundle(getArguments()).getUserId();
        send = view.findViewById(R.id.send_btn);
        txtMsg = view.findViewById(R.id.text_mssg);
        chat = view.findViewById(R.id.user_chat);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            myId = currentUser.getUid();

            Map<String, Object> lastseen = new HashMap<>();
            lastseen.put("last_seen", "Online");
            firebaseFirestore.collection("UserDetails").document(currentUser.getUid()).update(lastseen);
        }

        firebaseFirestore.collection("UserDetails").document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            username = task.getResult().get("username").toString();

                            firebaseFirestore.collection("UserDetails").document(userid)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                            if (documentSnapshot.get("last_seen") != null)
                                            lastseen = String.valueOf(documentSnapshot.get("last_seen"));
                                            if (lastseen.equals("Online")) {
                                                toolbar.setSubtitle(lastseen);
                                            } else if (lastseen.equals("Offline")){
                                                toolbar.setSubtitle(lastseen);
                                            } else {
                                                toolbar.setSubtitle("Last seen at " + lastseen);
                                            }
                                        }
                                    });

                            toolbar.setTitle(username);
                            toolbar.setLogo(R.drawable.ic_account_circle_black_24dp);

                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onDestroy() {
        if (currentUser != null) {

            Map<String, Object> lastseen = new HashMap<>();
            lastseen.put("last_seen", "Offline");
            firebaseFirestore.collection("UserDetails").document(currentUser.getUid()).update(lastseen);
        }
        super.onDestroy();
    }

}
