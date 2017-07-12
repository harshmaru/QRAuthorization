package tk.romdroid.ibankauthenticator;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import tk.romdroid.ibankauthenticator.barcode.BarcodeCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    private TextView mResultTextView;

    AlertDialog alertDialog;

    SharedPreferences sp1;

    String Ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            sp1 = this.getSharedPreferences("Login", 0);
            String status = sp1.getString("status", null);
            Ano = sp1.getString("Ano",null);
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Shared Preferences");
            alertDialog.setMessage("status = "+status+" Ano = "+Ano);
            alertDialog.show();

            if (status == null || status.equalsIgnoreCase("false")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        }catch (Exception e)
        {

        }

        mResultTextView = (TextView) findViewById(R.id.result_textview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    mResultTextView.setText("Transaction validated");//barcode.displayValue);
                    Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                    String Url="http://192.168.43.140/fys/validate.php?Ano="+Ano+"&athKey="+barcode.displayValue;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Shared Preferences");
                    alertDialog.setMessage(Url);
                    alertDialog.show();
                    intent.putExtra("url",Url);
                    startActivity(intent);
                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_Logout)
        {
            SharedPreferences.Editor Ed=sp1.edit();
            Ed.putString("status","false");
            Ed.commit();
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
