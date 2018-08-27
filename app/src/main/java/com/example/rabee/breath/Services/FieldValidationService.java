package com.example.rabee.breath.Services;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rabee on 1/23/2018.
 */

public class FieldValidationService {
    public static boolean valid(String email, String password,EditText emailEditText, EditText passEditText) {
        boolean flag = true;
        if ((password.trim().equals(""))) {
            passEditText.setError("Password is required");
            flag = false;
        } else if (password.length() < 8) {
            passEditText.setError("Invalid password");
            flag = false;
        }
        if ((email.trim().equals(""))) {
            emailEditText.setError("Email is required");
            flag = false;
        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            Pattern p = Pattern.compile(emailPattern);
            Matcher m = p.matcher(email);
            boolean b = m.matches();
            if (!b) {
                emailEditText.setError("Invalid email");
                flag = false;
            }
        }
        return flag;
    }

}
