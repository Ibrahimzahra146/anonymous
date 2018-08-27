package com.example.rabee.breath.fragments.SignUpFragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import com.example.rabee.breath.Activities.SignUpActivity;
import com.example.rabee.breath.R;


public class GenderFragment extends android.app.Fragment {
    Button Nextbtn;
    RadioButton male, female;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_gender_fragment, container, false);
        male = (RadioButton) view.findViewById(R.id.radioM);
        female = (RadioButton) view.findViewById(R.id.radioF);
        Nextbtn = (Button) view.findViewById(R.id.nextBtn);

        Nextbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (male.isChecked()) {
                    ((SignUpActivity) getActivity()).setUserGender("male");
                    android.app.Fragment f = new PasswordFragment();
                    ((SignUpActivity) getActivity()).replaceFragmnets(f);

                } else if (female.isChecked()) {
                    ((SignUpActivity) getActivity()).setUserGender("female");
                    android.app.Fragment f = new PasswordFragment();
                    ((SignUpActivity) getActivity()).replaceFragmnets(f);

                }
            }
        });
        return view;
    }
}
