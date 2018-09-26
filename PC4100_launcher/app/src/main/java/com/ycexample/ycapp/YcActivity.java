package com.ycexample.ycapp;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.ysy.pc4100.R;
import com.ychmi.sdk.YcApi;



public class YcActivity extends Activity {
	YcApi ycapi;
	
	public boolean mBeepStatus=false;
	public boolean mLedStatus=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yc);
		ycapi = new YcApi(); 
		ycapi.HideNviBarFull();	
		
		//ycapi.HideNviBar(getWindow().getDecorView());		
		
		//蜂鸣器初始化
		BeepInit();
		
		//状态灯初始化
		LedInit();		
			
		//(4)E2PROM初始化
		E2promInit();
		
		//(5)看门狗初始化
		WdogInit();
		
		//(6)IO初始化
		BoardIDInit();
		
		//(7)串口初始化
		SerialInit();
		
		StartAndroidLauncher();	
		
		PowerOff();
		

		//返回按钮的操作
        Button mButtonReturn=(Button)findViewById(R.id.buttonreturn);
        mButtonReturn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				ycapi.ReturnLauncher(YcActivity.this);							
			}
		});
        addShortCut(YcActivity.this);
	}
	
	public void onBackPressed()//监测返回键的函数
	{
		ycapi.ReturnLauncher(YcActivity.this);
	}
	
	
	//(1)蜂鸣器初始化
	private void BeepInit()
	{
		ImageButton btnBeep = (ImageButton)findViewById(R.id.imageButtonBeep);          
	    btnBeep.setOnTouchListener(new View.OnTouchListener(){   
	    	@Override
	        public boolean onTouch(View v, MotionEvent event) {               
	                if(event.getAction() == MotionEvent.ACTION_DOWN){       
	                   //重新设置按下时的背景图片  
	                	if(mBeepStatus)
	                	{
	                		((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.beep2)); 
	                		mBeepStatus=false;
	                		ycapi.SetBeep(false);
	                	}
	                	else
	                	{
	                		((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.beep)); 
	                		mBeepStatus=true;
	                		ycapi.SetBeep(true);	                		
	                	}
	                }	        
	                return false;       
	        }      
	    });  
	}
	
	
	private void StartAndroidLauncher()
	{
		final ImageButton button= (ImageButton)findViewById(R.id.imageButtonLauncher);
    	button.setOnClickListener(new View.OnClickListener() 
    	{
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_MAIN); 
				intent.addCategory(Intent.CATEGORY_LAUNCHER);             
				ComponentName cn = new ComponentName("com.android.launcher", "com.android.launcher2.Launcher");             
				intent.setComponent(cn); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(intent);	 
			}
		});		
	}

	//(2)状态灯初始化
	private void LedInit()
	{
		ImageButton btnLed = (ImageButton)findViewById(R.id.imageButtonLed);          
		btnLed.setOnTouchListener(new View.OnTouchListener(){   
	    	@Override
	        public boolean onTouch(View v, MotionEvent event) {               
	                if(event.getAction() == MotionEvent.ACTION_DOWN){       
	                   //重新设置按下时的背景图片  
	                	if(mLedStatus)
	                	{
	                		((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.led2)); 
	                		mLedStatus=false;
	                		ycapi.SetLed(false);
	                		//ycapi.HideNviBarFull();
	                	}
	                	else
	                	{
	                		((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.led)); 
	                		mLedStatus=true;
	                		ycapi.SetLed(true);
	                		//ycapi.ShowNviBarFull();
	                	}
	                }	        
	                return false;       
	        }      
	    });  
	}
	
	//(2)关机
		private void PowerOff()
		{
			ImageButton btnLed = (ImageButton)findViewById(R.id.imageButtonPowerOff);          
			btnLed.setOnTouchListener(new View.OnTouchListener(){   
		    	@Override
		        public boolean onTouch(View v, MotionEvent event) {               
		                if(event.getAction() == MotionEvent.ACTION_DOWN)
		                {       
		                   
		                	 new AlertDialog.Builder(YcActivity.this).setTitle("系统提示")//设置对话框标题  		                	   
		                	      .setMessage("确定关机吗？")//设置显示的内容  		                	   
		                	      .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮    
		                	          @Override  		                	   
		                	          public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  		                	   
		                	              // TODO Auto-generated method stub 
		                	              ycapi.PowerOff();
		                	              finish();
		                	          }  		                	   
		                	      }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮  
		                	   
		                	          @Override  		                	   
		                	          public void onClick(DialogInterface dialog, int which) {//响应事件  		                	   
		                	              // TODO Auto-generated method stub  		                	   
		                	             // Log.i("alertdialog"," 请保存数据！");  		                	   
		                	          }  		                	   
		                	      }).show();//在按键响应事件中显示此对话框  
		                	  } 
		                		
		                        
		                return false;       
		        }      
		    });  
		}

	//(4)E2PROM初始化
	public void E2promInit()
	{
		final ImageButton button = (ImageButton)findViewById(R.id.imageButtonE2prom);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(YcActivity.this, E2promActivity.class));
			}
		});		
	}
	//(5)看门狗初始化
	public void WdogInit()
	{		
		final ImageButton button = (ImageButton)findViewById(R.id.imageButtonWDog);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				startActivity(new Intent(YcActivity.this, WdogActivity.class));
			}
		});
	}
	//(6)IO初始化
	public void BoardIDInit() 
	{		
		final ImageButton button = (ImageButton)findViewById(R.id.imageButtonID);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				startActivity(new Intent(YcActivity.this, IDActivity.class));
			} 
		});
	}
	//(7) 串口初始化   
	public void SerialInit() 
	{		
		final ImageButton button = (ImageButton)findViewById(R.id.imageButtonSerial);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				startActivity(new Intent(YcActivity.this, SerialActivity.class));
			}
		});
	}

	
	
	public void addShortCut(Context context) 
	{
		// 添加桌面快捷图标的一个意图对象,对应上面Receiver的intent-filter中的action
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
		// 不允许重复创建
		shortcut.putExtra("duplicate", false);
		// Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
		Intent intent = new Intent(context, context.getClass());
		intent.setAction(Intent.ACTION_MAIN);
		// 指定快捷方式指向的启动程序对象为intent
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 指定快捷方式图标
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
		Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
		context.sendBroadcast(shortcut);
	}

}
