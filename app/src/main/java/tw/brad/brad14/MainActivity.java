package tw.brad.brad14;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private EditText x,y;
    private File sdroot, approot;
    private ProgressDialog pDialog;
    private UIHandler handler;
    private static final int UI_MESG_SHOW_PROGRESS = 0;
    private static final int UI_MESG_CLOSE_PROGRESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new UIHandler();
        tv = (TextView)findViewById(R.id.tv);
        x = (EditText)findViewById(R.id.x);
        y = (EditText)findViewById(R.id.y);

        sdroot = Environment.getExternalStorageDirectory();
        approot = new File(sdroot, "Android/data/" + getPackageName() + "/");
        if (!approot.exists()){
            approot.mkdirs();
        }

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Download...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    public void test1(View v){
        new Thread(){
            @Override
            public void run() {
                handler.sendEmptyMessage(UI_MESG_SHOW_PROGRESS);
                http1("http://www.brad.tw");
                handler.sendEmptyMessage(UI_MESG_CLOSE_PROGRESS);

            }
        }.start();

    }

    private void http1(String urlString) { //官方
        try {
            URL url = new URL("http://pdfmyurl.com/?url=" + urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            FileOutputStream fout = new FileOutputStream(
                    new File(approot, "brad.pdf"));

            byte[] buf = new byte[4096]; int len;
            InputStream in = conn.getInputStream();
            while ( (len = in.read(buf)) != -1){
                fout.write(buf, 0, len);
            }
            in.close();

            fout.flush();
            fout.close();
            Log.i("brad", "OK");

        } catch (Exception e) {
            Log.i("brad", e.toString());
        }
    }

    public void test2(View v){
        new Thread(){
            @Override
            public void run() {
                getYahoo();
            }
        }.start();
    }

    private void getYahoo(){
        try {
            MultipartUtility mu =
                    new MultipartUtility("https://tw.yahoo.com", "UTF-8");
            List<String> ret = mu.finish();
            for (String line : ret){
                Log.i("brad", line);
            }
        }catch(Exception ee){
            Log.i("brad", ee.toString());
        }
    }

    public void test3(View v){
        new Thread(){
            @Override
            public void run() {
                doTest3();
            }
        }.start();
    }

    private void doTest3(){
        String inputx = x.getText().toString();
        String inputy = y.getText().toString();
        try{
            MultipartUtility mu = new MultipartUtility(
                    "http://10.0.3.2/iii2001/brad03.php?x=" +
                            inputx + "&y=" + inputy,"UTF-8");
//            MultipartUtility mu = new MultipartUtility(
//                    "http://data.coa.gov.tw/Service/OpenData/EzgoTravelFoodStay.aspx","UTF-8");
            List<String> ret = mu.finish();
            for (String line : ret){
                Log.i("brad", line);
            }
        }catch(Exception e){

        }
    }
    public void test4(View view){
        new Thread(){
            @Override
            public void run() {
            doTest4();

            }
        }.start();
    }
    private void doTest4(){
        String inputx = x.getText().toString();
        try {
            MultipartUtility multipartUtility = new MultipartUtility("http://10.0.3.2/ming0531/brad04.php?x="+inputx,"UTF-8");
            List<String> ret = multipartUtility.finish();
            for(String line :ret){
                Log.i("ming",line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void test5(View view){
        new Thread(){
            @Override
            public void run() {
                dotest5();
            }
        }.start();
    }
    private void dotest5(){
        String inputx = x.getText().toString();
        String inputy = y.getText().toString();
        File uploadFile = new File(sdroot,"0412.jpg");
        try {
            MultipartUtility multipartUtility =
                    new MultipartUtility("http://10.0.3.2/ming0531/brad07.php","UTF-8");
            multipartUtility.addFormField("account",inputx);
            multipartUtility.addFormField("passwd",inputy);
            multipartUtility.addFilePart("upload",uploadFile);
            List<String> ret = multipartUtility.finish();
            for(String line :ret){
                Log.i("ming",line);
            }
        } catch (IOException e) {

        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case UI_MESG_SHOW_PROGRESS:
                    pDialog.show();
                    break;
                case UI_MESG_CLOSE_PROGRESS:
                    if (pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                    break;

            }
        }
    }


}
