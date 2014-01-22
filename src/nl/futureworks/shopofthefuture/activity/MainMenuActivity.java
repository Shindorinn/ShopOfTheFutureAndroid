package nl.futureworks.shopofthefuture.activity;

import com.futureworks.shopofthefuture.R;

import android.os.Bundle;
import android.app.Activity;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.initializeActivity();
	}

	
	
	
	
	private void initializeActivity(){
		this.setContentView(R.layout.activity_main_menu);
	}

}
