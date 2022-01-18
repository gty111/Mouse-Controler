package android.example.mousecontroler;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;

public class CheckConnect extends AsyncTask<Void,Void,Void> {

    private WeakReference<Context> context;
    private WeakReference<FloatingActionButton> fab;
    private WeakReference<Button>button,click,up,down;

    public CheckConnect(Context context,
                        FloatingActionButton fab,
                        Button button,
                        Button click,
                        Button up,
                        Button down){
        this.context = new WeakReference<>(context);
        this.fab = new WeakReference<>(fab);
        this.button = new WeakReference<>(button);
        this.click = new WeakReference<>(click);
        this.up = new WeakReference<>(up);
        this.down = new WeakReference<>(down);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            MainActivity.socket.getInputStream().read();
        } catch (java.net.SocketException e) {
            MainActivity.flag = false;
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        if(!MainActivity.flag){
            Toast.makeText(context.get(),"连接断开",Toast.LENGTH_LONG).show();
            MainActivity.out = null;
            fab.get().setVisibility(View.VISIBLE);
            button.get().setVisibility(View.INVISIBLE);
            click.get().setVisibility(View.INVISIBLE);
            up.get().setVisibility(View.INVISIBLE);
            down.get().setVisibility(View.INVISIBLE);
        }

    }
}
