package com.btec.fpt.campus_expense_manager.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.DataStatic;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddExpenseFragment extends Fragment {


    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText, typeEditText, emailEditText;


    public AddExpenseFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        dbHelper = new DatabaseHelper(getContext());
        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        typeEditText = view.findViewById(R.id.typeEditText);
        emailEditText = view.findViewById(R.id.emailEditText);

        // Load budget from SharedPreferences
        getContext();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("BudgetPrefs", Context.MODE_PRIVATE);
        userBudget = sharedPreferences.getFloat("monthly_budget", 0);

        // Inflate the layout for this fragment

        Button addButton = view.findViewById(R.id.addButton);

        Button btnDisplay = view.findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(v -> loadFragment(new DisplayExpenseFragment()));

        addButton.setOnClickListener(v -> addExpense());

        // Find Spinner by ID
        Spinner spinner = view.findViewById(R.id.spinner);

        List<Category> categoryList = dbHelper.getAllCategoryByEmail(DataStatic.email);


        // Extract category names into an ArrayList
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getName()); // Assuming getCategoryName() is the getter for the name
        }

        // Convert the ArrayList to a String array, if needed
        String[] categoryNameArray = categoryNames.toArray(new String[0]);
        // Create a list of items for the Spinner


        // Create an ArrayAdapter
        @SuppressLint("UseRequireInsteadOfGet") ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()),
                android.R.layout.simple_spinner_item, // Default Spinner layout
                categoryNameArray
        );

        // Set the dropdown view resource for the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the Spinner
        spinner.setAdapter(adapter);

        // Set an Item Selected Listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                selectCategory = selectedItem;


                Toast.makeText(getContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected, if needed
            }
        });


        return view;
    }
    private float userBudget = 0;


    @Override
    public void onResume() {
        super.onResume();
        getContext();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("BudgetPrefs", Context.MODE_PRIVATE);
        userBudget = sharedPreferences.getFloat("monthly_budget", 0);
    }




    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    static String selectCategory = "Food" ;
    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountEditText.getText().toString());

            // Kiểm tra nếu số tiền vượt quá ngân sách
            if (amount > userBudget) {
                // Hiển thị cảnh báo
                new AlertDialog.Builder(getContext())
                        .setTitle("Over budget")
                        .setMessage("This transaction exceeds your set budget. Do you want to continue?")
                        .setPositiveButton("Continue", (dialog, which) -> saveExpense(amount))
                        .setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Transaction has been cancelled.", Toast.LENGTH_SHORT).show())
                        .show();
            } else {
                // Nếu không vượt ngân sách, tiếp tục lưu giao dịch
                saveExpense(amount);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid amount!", Toast.LENGTH_SHORT).show();
        }
    }
    // Lưu giao dịch vào cơ sở dữ liệu
    private void saveExpense(double amount) {
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();
        int type = Integer.parseInt(typeEditText.getText().toString());
        String email = DataStatic.email;

        boolean inserted = dbHelper.insertTransaction(amount, description, date, type, email, selectCategory);
        if (inserted) {
            Toast.makeText(getContext(), "Transaction added.", Toast.LENGTH_SHORT).show();
            amountEditText.setText("");
            descriptionEditText.setText("");
            dateEditText.setText("");
            typeEditText.setText("");
            emailEditText.setText("");
        } else {
            Toast.makeText(getContext(), "Error adding transaction.", Toast.LENGTH_SHORT).show();
        }
    }

}
