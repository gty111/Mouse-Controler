package android.example.mousecontroler;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;


public class MyAsyncTask extends AsyncTask<Void,Void,Void> {

    private WeakReference<Context> context;
    private WeakReference<AVLoadingIndicatorView> loading;
    private WeakReference<FloatingActionButton> fab;
    private WeakReference<Button>button,click,up,down;

    public MyAsyncTask(Context context,
                       AVLoadingIndicatorView loading,
                       FloatingActionButton fab,
                       Button button,
                       Button click,
                       Button up,
                       Button down){
        this.context = new WeakReference<>(context);
        this.loading = new WeakReference<>(loading);
        this.fab = new WeakReference<>(fab);
        this.button = new WeakReference<>(button);
        this.click = new WeakReference<>(click);
        this.up = new WeakReference<>(up);
        this.down = new WeakReference<>(down);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            MainActivity.socket = new Socket(MainActivity.IP,
                    Integer.parseInt(MainActivity.Port));
            MainActivity.flag = true;
        } catch (Exception e) {
            MainActivity.socket = null;
            MainActivity.flag = false;
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        if(MainActivity.flag){
            Toast.makeText(context.get(),"连接成功", Toast.LENGTH_SHORT).show();
            fab.get().setVisibility(View.INVISIBLE);
            button.get().setVisibility(View.VISIBLE);
            click.get().setVisibility(View.VISIBLE);
            up.get().setVisibility(View.VISIBLE);
            down.get().setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(context.get(),"连接失败", Toast.LENGTH_SHORT).show();
            fab.get().setVisibility(View.VISIBLE);
            button.get().setVisibility(View.INVISIBLE);
            click.get().setVisibility(View.INVISIBLE);
            up.get().setVisibility(View.INVISIBLE);
            down.get().setVisibility(View.INVISIBLE);
        }

        loading.get().smoothToHide();
    }
}
