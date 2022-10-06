package com.malik.msd;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;

import model.Entry;
import ui.RecordsRecyclerAdapter;
import utils.MSDApi;

public class RecordsFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Records");

    private List<Entry> entryList;
    private RecyclerView recordsRecyclerView;
    private RecordsRecyclerAdapter recordsRecyclerAdapter;

    private TextView tvNoRecords;

    public RecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        entryList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recods, container, false);

        tvNoRecords = rootView.findViewById(R.id.tvNoRecords);

        recordsRecyclerView = rootView.findViewById(R.id.recordsRecyclerView);
        recordsRecyclerView.setHasFixedSize(true);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", MSDApi.getInstance()
                .getUserId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot records : queryDocumentSnapshots){
                                Entry entry = records.toObject(Entry.class);
                                entryList.add(entry);
                            }

                            recordsRecyclerAdapter = new RecordsRecyclerAdapter(getContext(), entryList);
                            recordsRecyclerView.setAdapter(recordsRecyclerAdapter);
                            recordsRecyclerAdapter.notifyDataSetChanged();
                        }else {
                            tvNoRecords.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}