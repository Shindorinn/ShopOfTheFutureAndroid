package nl.futureworks.shopofthefuture.activity;

import android.widget.Toast;
import com.futureworks.shopofthefuture.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class ShoppingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		this.initializeActivity();
	}

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            Log.d("DEBUG", scanResult.getContents());
            Toast toast = Toast.makeText(this, scanResult.getContents(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
    
	public void scanBarcode(View v){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    
    private void initializeActivity(){
		setContentView(R.layout.activity_shopping);
    }
    
}
