package com.example.palengke.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palengke.CompletedOrders;
import com.example.palengke.Login;
import com.example.palengke.R;
import com.example.palengke.databinding.FragmentSettingsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private FragmentSettingsBinding binding;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private TextView userProfileName;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();

        // Display signed-in user's email
        if (auth.getCurrentUser() != null) {
            String email = auth.getCurrentUser().getEmail();
            binding.userProfileName.setText(email);
        } else {
            binding.userProfileName.setText("Not signed in");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        binding.logoutBtn.setOnClickListener(v -> {
            auth.signOut();

            googleSignInClient.signOut().addOnCompleteListener(requireActivity(), task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(requireActivity(), "Logout failed", Toast.LENGTH_SHORT).show();
                } else {
                    Intent logout = new Intent(requireActivity(), Login.class);
                    startActivity(logout);
                    requireActivity().finish();
                    Toast.makeText(requireActivity(), "Logout successfully", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.completedOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCompletedOrders = new Intent(getActivity(), CompletedOrders.class);
                startActivity(toCompletedOrders);
            }
        });

        binding.cancelledOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "You don't have any cancelled order yet.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }
}
