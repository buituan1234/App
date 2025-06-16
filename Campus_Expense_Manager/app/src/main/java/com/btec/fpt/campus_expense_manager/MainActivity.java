package com.btec.fpt.campus_expense_manager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.fragments.LoginFragment;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    private MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<?> sharedPreferences = getClass();
        Class<?> isLoggedIn = Objects.requireNonNull(sharedPreferences).getComponentType();


        if (isLoggedIn) {
            // Navigate to HomeActivity if already logged in
            AtomicReference<Intent> intent;
            intent = new AtomicReference<>();
            intent.set(new Intent(this, HomeActivity.class));
            startActivity(intent.get());
            finish(); // Prevent returning to this screen
        } else {
            setContentView(R.layout.activity_main);

            // Show LoginFragment by default
            loadFragment(new LoginFragment());
        }
    }

    @NonNull
    public Class<?> getClass() {
        Class<?> aClass = null;
        return aClass;
    }

    private void setContentView(int ignoredActivityMain) {
    }

    private void finish() {
    }

    private void startActivity(Intent ignoredIntent) {
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getSupportFragmentManager()).beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private FragmentManager getSupportFragmentManager() {
        return null;
    }

}
