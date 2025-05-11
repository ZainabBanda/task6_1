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

import com.mine.pl.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentLoginBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b.btnLogin.setOnClickListener(v -> {
            String u = b.etUsername.getText().toString().trim();
            String p = b.etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
                Toast.makeText(getContext(),
                    "Username and password required",
                    Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String savedUser = prefs.getString("username", null);
            String savedPass = prefs.getString("password", null);

            if (u.equals(savedUser) && p.equals(savedPass)) {
                // on success, navigate to Home
                LoginFragmentDirections.ActionLoginToHome action =
                    LoginFragmentDirections
                        .actionLoginToHome(u, new String[]{});
                NavHostFragment.findNavController(this).navigate(action);
            } else {
                Toast.makeText(getContext(),
                    "Invalid credentials",
                    Toast.LENGTH_SHORT).show();
            }
        });

        b.tvNeedAccount.setOnClickListener(v ->
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_login_to_setup)
        );
    }
}
