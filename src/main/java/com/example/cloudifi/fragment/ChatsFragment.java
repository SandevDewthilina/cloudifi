package com.example.cloudifi.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudifi.R;
import com.example.cloudifi.adapter.UsersListAdapter;
import com.example.cloudifi.entitiies.UserEntity;
import com.example.cloudifi.model.UsersModel;
import com.example.cloudifi.repository.UserRepository;
import com.example.cloudifi.viewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment implements UsersListAdapter.OnItemClickListner {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;

    private UsersListAdapter usersListAdapter;
    private RecyclerView userList;
    private TextView alert;
    private ProgressBar progressBar;
    private NavController navController;

    private UserViewModel userViewModel;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        userList = view.findViewById(R.id.user_chat);
        usersListAdapter = new UsersListAdapter(this);
        alert = view.findViewById(R.id.nolist_alert);
        progressBar = view.findViewById(R.id.user_loading_progress);
        navController = Navigation.findNavController(view);

        Map<String, Object> lastseen = new HashMap<>();
        lastseen.put("last_seen", "Offline");
     //   if (currentUser != null) firebaseFirestore.collection("UserDetails").document(currentUser.getUid()).update(lastseen);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alert.setText("Loading please wait...");

    }

    @Override
    public void onItemClick(final String userid) {
        firebaseFirestore.collection("UserDetails").document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {

                            ChatsFragmentDirections.ActionChatsFragment2ToUserFragment action = ChatsFragmentDirections.actionChatsFragment2ToUserFragment();
                            action.setUserId(userid);
                            navController.navigate(action);

                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {

            Toast.makeText(getContext(), "Your online", Toast.LENGTH_SHORT).show();

            final Query query = firebaseFirestore.collection("UserDetails")
                    .orderBy("registered_date", Query.Direction.ASCENDING);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    List<UserEntity> usersModelList = task.getResult().toObjects(UserEntity.class);

                    if (usersModelList.size() != 0) {

                        UserRepository userRepository = new UserRepository(getActivity().getApplication());
                            userRepository.insertUserList(usersModelList);

                    } else Toast.makeText(getContext(), "user list is empty", Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            Toast.makeText(getContext(), "You are offline", Toast.LENGTH_SHORT).show();
        }

        //get the data from user database
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userViewModel.getUserDetailLiveData().observe(getViewLifecycleOwner(), new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                setChatlist(userEntities);
            }
        });

        alert.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void setChatlist(List<UserEntity> userEntitiesList) {

        usersListAdapter.setUsersModelList(userEntitiesList);
        userList.setHasFixedSize(true);
        userList.setLayoutManager(new LinearLayoutManager(getContext()));
        userList.setAdapter(usersListAdapter);

    }

    @Override
    public void onDestroy() {

        Map<String, String> lastseen = new HashMap<>();
        lastseen.put("last_seen", FieldValue.serverTimestamp().toString());
       // if (currentUser != null)
  //      firebaseFirestore.collection("UserDetails").document(currentUser.getUid()).update(lastseen);

        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
