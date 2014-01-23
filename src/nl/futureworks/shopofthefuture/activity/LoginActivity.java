package nl.futureworks.shopofthefuture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.exception.FieldValidationException;
import nl.futureworks.shopofthefuture.logic.FieldValidator;


public class LoginActivity extends Activity {

    EditText fieldEmail;
    EditText fieldPassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fieldEmail = (EditText) findViewById(R.id.email);
        fieldPassword = (EditText) findViewById(R.id.password);
    }

    public void onPause()
    {
        super.onPause();
        this.finish();
    }

    /**
     * Starts the marketplace intent
     */
    private void startIntent() {
        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        startActivity(intent);
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
            startIntent();
            //TODO: Attempt api login
            //TODO: Save login token
        }
    }
}
