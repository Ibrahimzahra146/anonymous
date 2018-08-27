package com.example.rabee.breath.fragments.UpdatePassword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rabee.breath.Activities.UpdatePasswordActivity;
import com.example.rabee.breath.R;

public class CheckCodeFragment extends android.app.Fragment {
    Button btn;
    int counter = 1;
    EditText code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.update_password_check_code_fragment, container, false);
        code = (EditText) view.findViewById(R.id.code);
        btn = (Button) view.findViewById(R.id.btn);
        final String uniqueId = ((UpdatePasswordActivity) getActivity()).getUniqueID();

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String codeString = code.getText().toString();

                if (codeString.equals(uniqueId)) {
                    android.app.Fragment f = new newPasswordFragment();
                    ((UpdatePasswordActivity) getActivity()).replaceFragmnets(f);
                } else if (counter <= 3){
                    code.setError("Incorrect Code");
                    counter++;
                }


            }
        });
        return view;
    }


}
