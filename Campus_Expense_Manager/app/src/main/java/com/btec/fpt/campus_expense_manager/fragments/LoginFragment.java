package com.btec.fpt.campus_expense_manager.fragments;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.HomeActivity;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    // Initialize SharedPreferences


    DatabaseHelper databaseHelper = null;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_login, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        databaseHelper = new DatabaseHelper(getContext());
        // Find buttons
        Button loginButton = view.findViewById(R.id.login_button);
        Button registerButton = view.findViewById(R.id.goto_register_button);
        Button changePasswordButton = view.findViewById(R.id.goto_change_password_button);


        EditText edtEmail = view.findViewById(R.id.email);
        EditText edtPassword = view.findViewById(R.id.password);

        loginButton.setOnClickListener(v -> {



            String email = edtEmail.getText().toString();
            String pwd = edtPassword.getText().toString();

            if(!email.isEmpty() && !pwd.isEmpty()){


              boolean check =   databaseHelper.signIn(email, pwd);

              if(check){
                  // Luu mat khau va email
                  editor.putString("email", email);
                  editor.putString("password", pwd);// Store hashed/encrypted version instead
                  editor.putBoolean("isLoggedIn", true);
                  editor.apply();  // or use commit() for synchronous saving


                  Intent intent = new Intent(getActivity(), HomeActivity.class);
                  startActivity(intent);
                  requireActivity().finish();

              }else {

                  showToastCustom("Email or password incorrect!");

              }


            }else {
                showToastCustom("Email or password is invalid !!!");
            }


        });

        // Set up button to go to RegisterFragment
        registerButton.setOnClickListener(v -> loadFragment(new RegisterFragment()));

        // Set up button to go to ForgotPasswordFragment
        changePasswordButton.setOnClickListener(v -> loadFragment(new ChangePasswordFragment()));

        return view;
    }

    void showToastCustom(String message){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_layout));
// Set the icon
        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.icon_x);  // Set your desired icon

// Set the text
        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

// Create and show the toast
        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}

