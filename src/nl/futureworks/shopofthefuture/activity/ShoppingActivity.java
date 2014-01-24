package nl.futureworks.shopofthefuture.activity;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import nl.futureworks.shopofthefuture.R;

public class ShoppingActivity extends BaseActivity {

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
