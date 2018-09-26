package com.ysy.pc4100;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.ychmi.sdk.YcApi;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;



public class NetworkActivity extends Activity{
	
	private static final String TAG = "XXXX:";
	int gEthflag=0;
	YcApi ycapi; 
	
	private static final String net_cfg_path = "/data/network/eth0/";
 
	public String onReadFileValue(String path)
    {
       	File awakeTimeFile = new File(path);
        FileReader fr;
        String str=null;
        if(!awakeTimeFile.exists())
        {
			try {
				awakeTimeFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
        }
        try
        {
             fr = new FileReader(awakeTimeFile);
             BufferedReader br=new BufferedReader(fr);
             try{
            	 str=br.readLine();  
            	 fr.close();
            	 //Log.d(TAG,"LLLLLLLLLLLLLLLL= \n"+str+ "\n");
             }catch(IOException e){
            	 e.printStackTrace();
             }            
        } 
        catch (FileNotFoundException e)
        {
             e.printStackTrace();
        }
        return str;
    }
	public void onWriteFileValue(String path,String str)
	{
		File awakeTimeFile = new File(path);
	    FileWriter fr;
	    try
	    {
	         fr = new FileWriter(awakeTimeFile);
	         fr.write(str); 
	         fr.write("\n"); 
	         fr.close();
	    } 
	    catch (IOException e)
	    {
	         e.printStackTrace();
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		ycapi = new YcApi();		
		
        ycapi.HideNviBar(getWindow().getDecorView());	
		
		NetworkInit();
		
		//���ذ�ť�Ĳ���
        Button mButtonReturn=(Button)findViewById(R.id.buttonreturn);
        mButtonReturn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NetworkActivity.this.finish();
			}
		});
	}
	private void GetNetworkMac(int addr[])
	{
			int[] data = new int[4];
			int i;
			int[] buf=new int[16];
			int[] tmp=new int[8];
			
			ycapi.GetBoardID(data);
			
			Log.d(TAG,"LLLLLLLLLLLLLLLL= \n"+String.format("%08x", data[0])+ "\n");
			Log.d(TAG,"LLLLLLLLLLLLLLLL= \n"+String.format("%08x", data[1])+ "\n");
			Log.d(TAG,"LLLLLLLLLLLLLLLL= \n"+String.format("%08x", data[2])+ "\n");
			Log.d(TAG,"LLLLLLLLLLLLLLLL= \n"+String.format("%08x", data[3])+ "\n");
		    
			
			for(i=0;i<4;i++)
			{
				buf[i]=(int)((data[0]>>(i*8))&0xff);
			}
			for(i=4;i<8;i++)
			{
				buf[i]=(int)((data[1]>>((i-4)*8))&0xff);
			}
			for(i=8;i<12;i++)
			{
				buf[i]=(int)((data[2]>>((i-8)*8))&0xff);
			}
			for(i=12;i<16;i++)
			{
				buf[i]=(int)((data[3]>>((i-12)*8))&0xff);
			}
			
		
			for(i=0;i<8;i++)
			{
				tmp[i]=(int)(((buf[i*2]<<4)+buf[i*2+1])&0xff);
				if(tmp[i]==255)
					tmp[i]=(int)(255-(i+1)*2);
			}
			tmp[4]=(int)((tmp[4]+tmp[6])&0xff);
			tmp[5]=(int)((tmp[5]+tmp[7])&0xff);
			
			for(i=3;i<6;i++)
			{
				tmp[i]=(int)(((tmp[i-3]<<4)+tmp[i])&0xff);
				if(tmp[i]==255)
					tmp[i]=(int)(255-(i+1)*2);
			}
			tmp[0]=(int)(0x7C);
			tmp[1]=(int)(0xDD);
			tmp[2]=(int)(0x90);
			
			for(i=0;i<6;i++)
				addr[i]=tmp[i];					
	}
	private void NetworkInit()
    {  	
		final EditText editTextIp = (EditText)findViewById(R.id.editTextIP);	
		final EditText editTextSubMask = (EditText)findViewById(R.id.editTextSubMask);	
		final EditText editTextGateWay = (EditText)findViewById(R.id.editTextGateWay);	
		final EditText editTextDNS1 = (EditText)findViewById(R.id.editTextDNS1);	
		final EditText editTextDNS2 = (EditText)findViewById(R.id.editTextDNS2);
		final EditText editTextMacaddr = (EditText)findViewById(R.id.editTextMacaddr);
		final Button buttonGet = (Button)findViewById(R.id.buttonGetMac);
		String ipStr=null;
		String submaskStr=null;
		String gatewayStr=null;
		String dns1Str=null;
		String dns2Str=null;
		String dhcpStr=null;
		int[] addr =new int[6];

		
		final Button buttonApply = (Button)findViewById(R.id.buttonApplyNet);
				
		
		RadioGroup mRadioGroupDhcp = (RadioGroup)findViewById(R.id.radioGroupDHCP);
		final RadioButton mRadioButtonStaticIp = (RadioButton)findViewById(R.id.radiostaticip);
		final RadioButton mRadioButtonDhcp = (RadioButton)findViewById(R.id.radiodhcp);	
		
		
		ipStr=onReadFileValue(net_cfg_path+"ip");
		if(ipStr==null)
		{
			onWriteFileValue(net_cfg_path+"ip","IP=192.168.1.253");
			ipStr = "192.168.1.253";
		}
		else
		{
			ipStr=ipStr.replace("IP=","");
		}
		submaskStr=onReadFileValue(net_cfg_path+"submask");
		if(submaskStr==null)
		{
			onWriteFileValue(net_cfg_path+"submask","Mask=255.255.255.0");
			submaskStr = "255.255.255.0";
		}
		else
		{
			submaskStr=submaskStr.replace("Mask=","");
		}
		gatewayStr=onReadFileValue(net_cfg_path+"gateway");
		if(gatewayStr==null)
		{
			onWriteFileValue(net_cfg_path+"gateway","Gateway=192.168.1.1");
			gatewayStr = "192.168.1.1";
		}
		else
		{
			gatewayStr=gatewayStr.replace("Gateway=","");
		}
		dns1Str=onReadFileValue(net_cfg_path+"dns1");
		if(dns1Str==null)
		{
			onWriteFileValue(net_cfg_path+"dns1","DNS1=202.96.134.33");
			dns1Str = "202.96.134.33";
		}
		else
		{
			dns1Str=dns1Str.replace("DNS1=","");
		}
		dns2Str=onReadFileValue(net_cfg_path+"dns2");
		if(dns2Str==null)
		{
			onWriteFileValue(net_cfg_path+"dns2","DNS2=192.168.1.1");
			dns2Str = "192.168.1.1";
		}
		else
		{
			dns2Str=dns2Str.replace("DNS2=","");
		}
		dhcpStr=onReadFileValue(net_cfg_path+"dhcp");
		if(dhcpStr==null)
		{
			onWriteFileValue(net_cfg_path+"dhcp","DHCP=0");
			dhcpStr = "0";
		}
		else
		{
			dhcpStr=dhcpStr.replace("DHCP=","");
		}
		
		GetNetworkMac(addr);
	
				
		//�õ�������������в���	
		editTextMacaddr.setText(String.format("%02x", addr[0])+":"+String.format("%02x", addr[1])+":"+String.format("%02x", addr[2])+":"+String.format("%02x", addr[3])
				+":"+String.format("%02x", addr[4])+":"+String.format("%02x", addr[5]));
		editTextIp.setText(ipStr);
		editTextSubMask.setText(submaskStr);
		editTextGateWay.setText(gatewayStr);
		editTextDNS1.setText(dns1Str);
		editTextDNS2.setText(dns2Str);		
	
    	if(dhcpStr.equals("1")) 
    	{
    		mRadioButtonDhcp.setChecked(true);
    		mRadioButtonStaticIp.setChecked(false);
    		editTextIp.setEnabled(false);
			editTextSubMask.setEnabled(false);	
			editTextGateWay.setEnabled(false);	
			editTextDNS1.setEnabled(false);	
			editTextDNS2.setEnabled(false);	
			buttonApply.setEnabled(false);
    	}
    	else if(dhcpStr.equals("0"))
    	{
    		mRadioButtonStaticIp.setChecked(true);    
    		mRadioButtonDhcp.setChecked(false);
    		editTextIp.setEnabled(true);
			editTextSubMask.setEnabled(true);	
			editTextGateWay.setEnabled(true);	
			editTextDNS1.setEnabled(true);	
			editTextDNS2.setEnabled(true);	
			buttonApply.setEnabled(true);
    	}     
		
    	//��������DHCP StaticIP�ļ����¼�
		mRadioGroupDhcp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
        {			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) 
			{
				String ipStr=null;
				String submaskStr=null;
				String gatewayStr=null;
				String dns1Str=null;
				String dns2Str=null;
				if(arg0.getCheckedRadioButtonId()==mRadioButtonStaticIp.getId())
				{
					editTextIp.setEnabled(true);
					editTextSubMask.setEnabled(true);	
					editTextGateWay.setEnabled(true);	
					editTextDNS1.setEnabled(true);	
					editTextDNS2.setEnabled(true);	
					buttonApply.setEnabled(true);
					ipStr=onReadFileValue(net_cfg_path+"ip");
					ipStr=ipStr.replace("IP=", "");	
					
					submaskStr=onReadFileValue(net_cfg_path+"submask");
					submaskStr=submaskStr.replace("Mask=", "");	
					
					gatewayStr=onReadFileValue(net_cfg_path+"gateway");
					gatewayStr=gatewayStr.replace("Gateway=", "");	
					
					dns1Str=onReadFileValue(net_cfg_path+"dns1");
					dns1Str=dns1Str.replace("DNS1=", "");	
					
					dns2Str=onReadFileValue(net_cfg_path+"dns2");
					dns2Str=dns2Str.replace("DNS2=", "");
				
					onWriteFileValue(net_cfg_path+"dhcp","DHCP=0");
					editTextIp.setText(ipStr);
					editTextSubMask.setText(submaskStr);
					editTextGateWay.setText(gatewayStr);
					editTextDNS1.setText(dns1Str);
					editTextDNS2.setText(dns2Str);
				}
				else if(arg0.getCheckedRadioButtonId()==mRadioButtonDhcp.getId())
				{
					editTextIp.setEnabled(false);
					editTextSubMask.setEnabled(false);	
					editTextGateWay.setEnabled(false);	
					editTextDNS1.setEnabled(false);	
					editTextDNS2.setEnabled(false);	
					buttonApply.setEnabled(false);					
					onWriteFileValue(net_cfg_path+"dhcp","DHCP=1");	
					ycapi.SetNetWork(0, 1, "", "", "", "", "");
					
				}					
			}
		});   
		
		//�����������õ�Ӧ�ð�ť�����¼�
		buttonApply.setOnClickListener(new View.OnClickListener()
		{			
			public void onClick(View arg0) 
			{ 
				String str=null;	        
		
				onWriteFileValue(net_cfg_path+"ip","IP="+editTextIp.getText().toString());
				onWriteFileValue(net_cfg_path+"submask","Mask="+editTextSubMask.getText().toString());
				onWriteFileValue(net_cfg_path+"gateway","Gateway="+editTextGateWay.getText().toString());
				onWriteFileValue(net_cfg_path+"dns1","DNS1="+editTextDNS1.getText().toString());
				onWriteFileValue(net_cfg_path+"dns2","DNS2="+editTextDNS2.getText().toString());	
				onWriteFileValue("/system/etc/network/eth0/dns2","DNS2=xxxx");
			
					
				ycapi.StartEth(0, 0, editTextIp.getText().toString(),
									   editTextSubMask.getText().toString(),
									   editTextGateWay.getText().toString(),
									   editTextDNS1.getText().toString(),
									   editTextDNS2.getText().toString());
				
							
			} 
		}); 
		
		//MacAddr��������ť�����¼�
		buttonGet.setOnClickListener(new View.OnClickListener()
		{			
			public void onClick(View arg0) 
			{ 					
				//editTextMacaddr.setText(onReadFileValue("/system/etc/data/network/eth0/macaddr"));
			}
		});	
	
	
    }
}
