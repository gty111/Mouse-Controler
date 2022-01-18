package android.example.mousecontroler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.*;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    double x,y,dx,dy;
    static String IP;
    static String Port;
    static Socket socket;
    static PrintWriter out = null;
    static boolean flag = false;
    int count;
    String sharedPrefFile = "android.example.mousecontroler";
    SharedPreferences mPreferences;
    SharedPreferences.Editor preferencesEditor;
    AVLoadingIndicatorView loading;
    FloatingActionButton fab;
    Button button,click,up,down;
    int mode = 0;
    boolean which_click=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.Layout).setOnTouchListener(this);
        mPreferences = getSharedPreferences(
                sharedPrefFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        loading = findViewById(R.id.loading);
        fab = findViewById(R.id.floatingActionButton);
        button = findViewById(R.id.button);
        click = findViewById(R.id.click);
        up = findViewById(R.id.button_up);
        down = findViewById(R.id.button_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(flag){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void op(MotionEvent event, int flag){
        dx = event.getX() - x;
        dy = event.getY() - y;
        x = event.getX();
        y = event.getY();
        new Thread(){
            public void run(){
                try{
                    if(out==null){
                        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    }
                    out.println(dx);
                    out.println(dy);
                    if(flag==0&&mode==1){
                        out.println(100000);
                        out.println(100000);
                    }else if(flag==2&&mode==1){
                        out.println(100001);
                        out.println(100001);
                    }else if(mode==0&&flag==2&&count<=5){
                        if(!which_click){
                            out.println(100002);
                            out.println(100002);
                        }else {
                            out.println(100003);
                            out.println(100003);
                            which_click = false;
                        }
                    }

                    out.flush();
                    new CheckConnect(MainActivity.this,fab,button,click,up,down).execute();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
        if(!which_click){
            click.setText("L");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(flag){
            switch(event.getAction()){
                //点击开始位置
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    op(event,0);
                    count = 0;
                    //Log.d("qwer","0"+event.getX()+","+event.getY());
                    break;
                //触屏实时位置
                case MotionEvent.ACTION_MOVE:
                    op(event,1);
                    count ++;
                    //Log.d("qwer","1"+event.getX()+","+event.getY());
                    break;
                //离开屏幕位置
                case MotionEvent.ACTION_UP:
                    op(event,2);
                    //Log.d("qwer","2"+event.getX()+","+event.getY());
                    break;
            }
        }
        return true;
    }

    /*建立网路连接*/
    public void fab_onclick(View view) {
        Context context = view.getContext();

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog,null);
        final EditText text_ip = textEntryView.findViewById(R.id.ip);
        final EditText text_port = textEntryView.findViewById(R.id.port);

        text_ip.setText(mPreferences.getString("IP",""));
        text_port.setText(mPreferences.getString("PORT",""));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("建立连接");    //设置对话框标题
        //builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标

        builder.setView(textEntryView);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IP = text_ip.getText().toString();
                Port = text_port.getText().toString();
                preferencesEditor.putString("IP",IP);
                preferencesEditor.putString("PORT",Port);
                preferencesEditor.apply();
                loading.smoothToShow();
                new MyAsyncTask(MainActivity.this,loading,fab,button,click,up,down).execute();
                //Toast.makeText(context, "你输入的是: " + edit.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(context, "你点了取消", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        AlertDialog dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }

    public void button_process(View view) {
        if(button.getText().toString().equals("P")){
            button.setText("R");
            button.setBackgroundColor(getResources().getColor(R.color.purple_700));
            click.setBackgroundColor(getResources().getColor(R.color.grey));
            click.setEnabled(false);
            mode = 1;
        }else{
            button.setText("P");
            button.setBackgroundColor(getResources().getColor(R.color.purple_200));
            click.setBackgroundColor(getResources().getColor(R.color.purple_700));
            click.setEnabled(true);
            mode = 0;
        }
    }

    public void click_process(View view){
        if(click.getText().toString().equals("L")){
            click.setText("R");
            which_click = true;
        }else{
            click.setText("L");
            which_click = false;
        }
    }

    public void up_process(View view) {
        new Thread(){
            public void run(){
                try{
                    if(out==null){
                        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    }
                    out.println(100005);
                    out.println(100005);
                    out.flush();
                    new CheckConnect(MainActivity.this,fab,button,click,up,down).execute();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void down_process(View view) {
        new Thread(){
            public void run(){
                try{
                    if(out==null){
                        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    }
                    out.println(100004);
                    out.println(100004);
                    out.flush();
                    new CheckConnect(MainActivity.this,fab,button,click,up,down).execute();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}