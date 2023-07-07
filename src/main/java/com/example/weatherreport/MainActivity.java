package com.example.weatherreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import static android.widget.Toast.LENGTH_LONG;
public class MainActivity extends AppCompatActivity {
    private TextView tv1,tv2,tv3;
    private Button b1;
    private ImageView iv1;
    private EditText ed1;
    private  String weatherText,weatherCode,weatherTemperature,diqu;
    private int[] image={R.drawable.a0,R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6,R.drawable.a7,R.drawable.a8,R.drawable.a9,R.drawable.a10,R.drawable.a11,R.drawable.a12,R.drawable.a13,R.drawable.a14,R.drawable.a15,R.drawable.a16,R.drawable.a17,R.drawable.a18,R.drawable.a19,R.drawable.a20,
            R.drawable.a21,R.drawable.a22,R.drawable.a23,R.drawable.a24,R.drawable.a25,R.drawable.a26,R.drawable.a27,R.drawable.a28,R.drawable.a29,R.drawable.a30,R.drawable.a31,R.drawable.a32,
            R.drawable.a33, R.drawable.a34, R.drawable.a35, R.drawable.a36, R.drawable.a37, R.drawable.a38, R.drawable.a99};
    public  class MyHandler extends Handler{
        public MyHandler(){
        }
    }
    MyHandler myHandler = new  MyHandler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Bundle bundle =(Bundle)msg.obj;
                tv2.setText(bundle.getString("text"));
                tv3.setText(bundle.getString("temperature")+"℃");
                tv1.setText("当前地区:"+bundle.getString("loc"));
                iv1.setImageResource(image[Integer.valueOf(bundle.getString("code")).
                        intValue()]);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=(TextView)this.findViewById(R.id.tv1);
        tv2=(TextView)this.findViewById(R.id.tv2);
        tv3=(TextView)this.findViewById(R.id.tv3);
        b1=(Button) this.findViewById(R.id.b1);
        iv1= (ImageView) this.findViewById(R.id.iv1);
        ed1=(EditText) this.findViewById(R.id.ed1);
        ed1.setText("无锡");
         sendHttpRequest();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHttpRequest();
                Toast.makeText(MainActivity.this,"正在查询",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void sendHttpRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(
                        "https://api.seniverse.com/v3/weather/now.json?key=S_VslFjbpPojnd6ro&location="+
                                ed1.getText().toString()+"&language=zh-Hans&unit=c"
                ).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responsestr = response.body().string();
                    JSONObject object = new JSONObject(responsestr);
                    JSONArray resultsarry = object.getJSONArray("results");
                    Log.d("JYPC", resultsarry+"sac");
                    JSONObject now = resultsarry.getJSONObject(0).getJSONObject("now");
                    JSONObject location =resultsarry.getJSONObject(0).getJSONObject("location");
                    weatherText = now.getString("text");
                    weatherCode = now.getString("code");
                    weatherTemperature = now.getString("temperature");
                    diqu=location.getString("name");
                    Bundle bundle = new Bundle();
                    bundle.putString("text", weatherText);
                    bundle.putString("code", weatherCode);
                    bundle.putString("temperature", weatherTemperature);
                    bundle.putString("loc",diqu);
                    Log.d("JYPC", diqu+"sac");
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = bundle;
                    myHandler.sendMessage(msg);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

}
