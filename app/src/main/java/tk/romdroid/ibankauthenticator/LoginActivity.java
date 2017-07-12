package tk.romdroid.ibankauthenticator;

/**
 * Created by developer69 on 25-12-2016.
 */
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import tk.romdroid.ibankauthenticator.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _emailText,_passwordText;
    Button _loginButton;
    TextView _signupLink;
    CheckBox chkBoxRem;
    ProgressDialog progressDialog;
    String email;
    BackgroundWorker bw = new BackgroundWorker(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bw.delegate=this;

        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        //_signupLink = (TextView) findViewById(R.id.link_signup);
        chkBoxRem = (CheckBox) findViewById(R.id.chkBoxRem);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

//        _signupLink.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Start the SignUp activity
////                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
////                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = _emailText.getText().toString();
        String password = computeMD5Hash(_passwordText.getText().toString());


        bw.execute("login",email,password);

    }

    @Override
    public void processFinish(String output) {

        progressDialog.hide();
//        AlertDialog  alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setTitle("Shared Preferences");
//        alertDialog.setMessage(email);
//        alertDialog.show();
        if(output.contains("true"))
        {
            SharedPreferences sp = getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("Ano",email+"");

            if(chkBoxRem.isChecked()) {
                Ed.putString("status", output);
            }
            Ed.commit();
            this.finish();
        }
        else
        {
            //Toast.makeText(this,output.toString(),Toast.LENGTH_LONG);
            onLoginFailed();
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed in onLoginFailed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("enter a valid Account Number");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public String computeMD5Hash(String password)
    {
        StringBuffer MD5Hash = new StringBuffer();
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();


            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            //result.setText("MD5 hash generated is: " + " " + MD5Hash);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return MD5Hash.toString();
    }
}