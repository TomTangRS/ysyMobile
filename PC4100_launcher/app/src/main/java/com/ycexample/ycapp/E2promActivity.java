package com.ycexample.ycapp;
import com.ychmi.sdk.YcApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.ysy.pc4100.R;


public class E2promActivity extends Activity{	

	YcApi ycapi; 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e2prom);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		ycapi = new YcApi();
		
		ycapi.HideNviBar(getWindow().getDecorView());
		
		E2promInit();
		
		//返回按钮的操作
        Button mButtonReturn=(Button)findViewById(R.id.buttonreturn);
        mButtonReturn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				E2promActivity.this.finish();
			}
		});
	}
	private void E2promInit()
    {  
		final EditText wE2promText = (EditText)findViewById(R.id.editTextWriteE2prom);
		final EditText rE2promText = (EditText)findViewById(R.id.editTextReadE2prom);
		Button wE2prom = (Button)findViewById(R.id.buttonWriteE2prom);
		wE2prom.setOnClickListener(new View.OnClickListener()
		{			
			public void onClick(View arg0) 
			{	
		        ycapi.WriteE2PROM(0, wE2promText.getText().toString(),wE2promText.getText().toString().length());		        
		        rE2promText.setText(ycapi.ReadE2PROM(0,wE2promText.getText().toString().length()));
			}
		});  
		    
    }
}
