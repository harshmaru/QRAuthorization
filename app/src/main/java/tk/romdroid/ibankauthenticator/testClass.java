package tk.romdroid.ibankauthenticator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

//import java.util.HashMap;

public class testClass extends AppCompatActivity{

    TextView txtTestResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        txtTestResult = (TextView) findViewById(R.id.txtTestResult);
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, "12340001", "1164f9e5b8cef768396dcd5374e4b6eb");
    }

}
