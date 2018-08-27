package com.example.rabee.breath.fragments.SignUpFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.example.rabee.breath.Activities.SignUpActivity;
import com.example.rabee.breath.R;

public class PasswordFragment extends android.app.Fragment {

    EditText Password, ConfirmPassword;
    Button SignUpBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        View view = inflater.inflate(R.layout.singup_password_fragment, container, false);
        Password = (EditText) view.findViewById(R.id.password);
        ConfirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        SignUpBtn = (Button) view.findViewById(R.id.SignUpBtn);

        SignUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if ((Password.getText().toString().trim().equals(""))) {
                    Password.setError("Password is required");
                } else if (Password.getText().toString().length() < 8) {
                    Password.setError("Password must contain at least 8 characters");
                } else if (!((ConfirmPassword.getText().toString()).equals(Password.getText().toString()))) {
                    ConfirmPassword.setError("Confirm password  doesn't match password!");
                } else {
                    ((SignUpActivity) getActivity()).setPassword(Password.getText().toString());
                    ((SignUpActivity) getActivity()).SignUp();
                }
            }
        });
        return view;
    }
}
