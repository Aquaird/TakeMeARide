package com.example.ridemepassenger;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import publicFunctions.static_saving;
import Data.Data;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	private ImageButton back;
	private TextView title;
	private Button create;
	private TextView telephone_text;
	private TextView password_text;
	private EditText telephone_edit;
	private EditText password_edit;
	private Button forget;
	private Button login;
	private String phone;
	private String password;
	private Dialog alertDialog;
	ProgressDialog dialog;
	
	class login_in extends AsyncTask<String, Void, String>{
		@Override
		 protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("请稍等");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
		}
		
	    protected String doInBackground(String... arg0) 
		{
	    	 String URL = "http://123.57.69.156/user/api/signin/";
	    	 String result="";
	    	 HttpPost httpRequest = new HttpPost(URL);
	    	 List <NameValuePair> params=new ArrayList<NameValuePair>(); 
	    //添加的ID号
	    	 params.add(new BasicNameValuePair("phone",phone));
	    	 params.add(new BasicNameValuePair("password",password));
	    	 
	    	 try 
	    	 {
	    		 httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
	    		 HttpResponse httpResponse = static_saving.httpClient.execute(httpRequest);
	    		 getCookie(static_saving.httpClient);
	    		 System.out.println(httpResponse.getStatusLine().getStatusCode());
	    		 if (httpResponse.getStatusLine().getStatusCode() == 200) 
	    		 {
	    			 result = EntityUtils.toString(httpResponse.getEntity());
	    				    			 
	    		 }
	             else
	             {
	            	 result = EntityUtils.toString(httpResponse.getEntity());
	            	 System.out.println(httpResponse.getStatusLine().getStatusCode());
	                  return "";
	             }
	    	 } catch (Exception e) {

	    	 }	
	    	 return result;
	    }  

	      @Override
		protected void onPostExecute(String result) 
	      {  
	    	  
		      if(result.equals("")||result==null)
		      {
		    	  alertDialog = new Dialog(LoginActivity.this);
    	    	  alertDialog.show();
    	    	  alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    	    	  LayoutInflater inflater = (LayoutInflater) LoginActivity.this  
	   	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	   			 
	   			       View tmp_view = inflater.inflate(R.layout.dialog_login, null); 
	   			       
	   			       TextView tmp_textview = (TextView)tmp_view.findViewById(R.id.dialog_login_text);
	   			       tmp_textview.setText("通讯失败，请检查网络");
    	    	  alertDialog.setContentView(tmp_view);
		    	  return;
		      }
		      if(null!=result&&!"".equals(result))
		      {
		    	  //Log.i("gygyPerson", result);
		    	  try 
		    	  {
		    	      JSONObject JO_result = new JSONObject(result);
		    	      String code = JO_result.getString("code");
		    	      if(code.equals("0")) //成功！！跳转到下一个页面
		    	      {
		    	    	 
		    	    	  String str_person = JO_result.getString("message");
		    	    	  JSONObject person = new JSONObject(str_person);
		    	    	  
		    	    	  String phone = person.getString("p_phone");
		    	    	  String username = person.getString("username");
		    	    	  String alipay = person.getString("alipay");
		    	    	  String avatar = person.getString("avatar");
		    	    	  Data.getInstance().setmPhone(phone);
		    	    	  Data.getInstance().setmName(username);
		    	    	  Data.getInstance().setmAvatar(avatar);
		    	    	  Data.getInstance().setmAlipay(alipay);
		    	    	  
		    	    	  Intent intent = new Intent();
		    	    	  intent.setClass(LoginActivity.this,MainActivity.class);  
		    	    	  intent.putExtra(MainActivity.EXTRA_LOGGEDIN,true);
		                  startActivity(intent);
		                  finish();
		    	      }
		    	      else
		    	      {
		    	    	  String message = JO_result.getString("message");//错误信息
		    	    	  alertDialog = new Dialog(LoginActivity.this);
		    	    	  alertDialog.show();
		    	    	  alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		    	    	  LayoutInflater inflater = (LayoutInflater) LoginActivity.this  
	    	   	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	    	   			 
	    	   			       View tmp_view = inflater.inflate(R.layout.dialog_login, null); 
	    	   			       
	    	   			       TextView tmp_textview = (TextView)tmp_view.findViewById(R.id.dialog_login_text);
	    	   			       tmp_textview.setText("帐号不存在或密码错误");
		    	    	  alertDialog.setContentView(tmp_view);
		    	    	  
		    	      }
	
		    	  } catch (JSONException e) 
		    	  {
		    		  Log.i("212121", result);

		    	  }
		      }

	      }

	}
	
	/**
	* 保存Cookie
	*/
	public static void savePreference(Context context,String key, String value) {
		SharedPreferences preference = context.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(key, value);
		Log.i("WWQQ","saving preferences.   key:"+key +" value:"+value);
		editor.commit();//
	}
	
	private void getCookie(HttpClient httpClient) {
		Log.i("WWQQ","IN GETCOOKIE");
		List cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cookies.size(); i++) {
			Cookie cookie = (Cookie) cookies.get(i);
			String cookieName = cookie.getName();
			String cookieValue = cookie.getValue();
			if (!TextUtils.isEmpty(cookieName)&& !TextUtils.isEmpty(cookieValue)) {
				sb.append(cookieName + "=");
				sb.append(cookieValue+";");
			}
		}
		Log.i("WWQQ","cookie is :"+sb);
		savePreference(this,"SID", sb.toString());
	}
	
	
	 @SuppressLint("NewApi") @Override
	    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_login);
	       Typeface face = Typeface.createFromAsset (getAssets() , "fonts/hjt.TTF" );
			
	        title = (TextView)findViewById(R.id.login_title);
	        title.setTypeface (face);
	        
	        create = (Button)findViewById(R.id.login_bt_create);
	        create.setTypeface (face);
	        create.setOnClickListener(clickListener);
	        
	        telephone_text = (TextView)findViewById(R.id.login_telephone_text);
	        telephone_text.setTypeface (face);
	        
	        telephone_edit = (EditText)findViewById(R.id.login_telephone_edit);
	        telephone_edit.setTypeface (face);
	        
	        password_text = (TextView)findViewById(R.id.login_password_text);
	        password_text.setTypeface (face);
	        
	        password_edit = (EditText)findViewById(R.id.login_password_edit);
	        password_edit.setTypeface (face);
	        
	        forget = (Button)findViewById(R.id.login_forget);
	        forget.setTypeface(face);
	        forget.setOnClickListener(clickListener);
	        
	        
	        login = (Button)findViewById(R.id.login_login);
	        login.setTypeface(face);
	        login.setOnClickListener(clickListener);
	 }
	 
	 OnClickListener clickListener = new OnClickListener()
	    {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(view == back)
				{
					LoginActivity.this.finish();
				}
				else if(view == create)
				{
					Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
					startActivity(intent);
					
				}
				else if(view == login)
				{
					phone = telephone_edit.getText().toString();
					password = password_edit.getText().toString();
					new login_in().execute();
				}
			}
	    };

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(dialog != null)
			dialog.dismiss();
		super.onStop();
	}
	    
}
