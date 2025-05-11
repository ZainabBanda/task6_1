package com.mine.pl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.mine.pl.databinding.FragmentSetupBinding;

public class SetupFragment extends Fragment {
    private FragmentSetupBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentSetupBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b.btnNext.setOnClickListener(v -> {
            String username = b.etUsername.getText().toString().trim();
            String email    = b.etEmail.getText().toString().trim();
            String confirmEmail = b.etConfirmEmail.getText().toString().trim();
            String password = b.etPassword.getText().toString().trim();
            String confirmPassword = b.etConfirmPassword.getText().toString().trim();

            // basic validation
            if (TextUtils.isEmpty(username)) {
                b.etUsername.setError("Required");
                return;
            }
            if (!email.equals(confirmEmail)) {
                Toast.makeText(getContext(), "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword) || password.length() < 4) {
                Toast.makeText(getContext(), "Passwords must match and be ≥4 chars", Toast.LENGTH_SHORT).show();
                return;
            }

            // save credentials
            SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            prefs.edit()
                .putString("username", username)
                .putString("password", password)
                .apply();

            // navigate to Interests, passing username
            SetupFragmentDirections.ActionSetupToInterests action =
                SetupFragmentDirections.actionSetupToInterests(username);
            NavHostFragment.findNavController(this).navigate(action);
        });
    }
}
