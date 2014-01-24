package nl.futureworks.shopofthefuture.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import nl.futureworks.shopofthefuture.exception.FieldValidationException;
import nl.futureworks.shopofthefuture.logic.FieldValidator;
import nl.futureworks.shopofthefuture.registry.Registry;
import nl.futureworks.shopofthefuture.R;

public class LoginActivity extends Activity {

    EditText fieldEmail;
    EditText fieldPassword;
    private SharedPreferences preferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fieldEmail = (EditText) findViewById(R.id.email);
        fieldPassword = (EditText) findViewById(R.id.password);
        preferences = getSharedPreferences(Registry.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Finish logging in
     */
    private void finishLogin(String loginToken) {
        Editor editor = preferences.edit();
        editor.putString(Registry.LOGIN_TOKEN, loginToken);
        editor.putBoolean(Registry.APP_LOGIN, true);
        editor.commit();
        Log.d("DEBUG", preferences.getString(Registry.LOGIN_TOKEN, ""));

        setResult(Registry.RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        setResult(Registry.RESULT_CANCELLED);
        finish();
    }

    public void attemptLogin(View view)
    {
        Boolean validInput=true;
        EditText[] fieldsToValidate={fieldEmail, fieldPassword};
        for(EditText textField : fieldsToValidate)
        {
            textField.setError(null);
            try {
                FieldValidator.validateTextField(textField);
            } catch (FieldValidationException e) {
                textField.setError(getString(e.getIndex()));
                validInput=false;
            }
        }
        if(validInput)
        {
            finishLogin("token");
            //TODO: Attempt api login
            //TODO: Save login token
        }
    }
}
