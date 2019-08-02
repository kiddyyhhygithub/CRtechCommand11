package yh.hy;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class MainActivity extends AppCompatActivity {

    public static Handler handler;
    private TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

//    @SuppressLint("HandlerLeak")
    private void init(){
       handler = new Handler(new Handler.Callback() {
           @Override
           public boolean handleMessage(Message message) {
               tv1.setText(message.obj.toString());
              return true;
           }
       });


       tv1 = findViewById(R.id.tv1);
    }
    public void btn1_click(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.obj = urlpost();
                handler.sendMessage(msg);
            }
        }).start();
    }

    private String urlpost() {
        int code_result = 0;
        String result = "";
        String json = "{\"name\":\"yanhua\"}";

//        HttpURLConnection conn;
        BufferedReader reader  = null;
        try {
            URL url = new URL("");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            //设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            //设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (json != null && !TextUtils.isEmpty(json)) {
                byte[] writebytes = json.getBytes();
                //设置长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }
//            String result;
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return result;
    }
//    static class Myhandler extends Handler{
//
//    }
}
