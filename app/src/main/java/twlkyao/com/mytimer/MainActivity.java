package twlkyao.com.mytimer;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private Button btn_start_stop;

    private Button btn_pause_resume;

    /**
     * The <code>TextView</code> to show the time in seconds
     */
    private TextView textView;

    private boolean isStop = true;

    private boolean isPause = false;

    private Timer timer = null;

    private TimerTask timerTask = null;

    private final static int PERIOD = 1000;

    private final static int DELAY = 0;

    /**
     * The type to update <code>TextView</code>
     */
    private final static int UPDATE_TEXTVIEW = 1;

    /**
     * Used to record the time in seconds,<br>
     * because the timer delay is 1 second, so the start will be 1.
     */
    private int count = 1;

    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage msg.what=" + msg.what);
            if (UPDATE_TEXTVIEW == msg.what) {
                if (null != textView) {
                    Log.d(TAG, "count=" + count);
                    textView.setText(getString(R.string.time_elapse, count));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViews();
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findViews() {
        btn_start_stop = (Button) findViewById(R.id.start_stop);
        btn_pause_resume = (Button) findViewById(R.id.pause_resume);
        textView = (TextView) findViewById(R.id.textview);
    }

    private void setListeners() {
        btn_start_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isStop = !isStop;
                if (!isStop) {
                    Log.d(TAG, "stop");
                    starTimer();
                    btn_start_stop.setText("stop");
                } else {
                    Log.d(TAG, "start");
                    stopTimer();
                    btn_start_stop.setText("start");
                    count = 0;
                    mHandler.sendEmptyMessage(UPDATE_TEXTVIEW);
                }
            }
        });

        btn_pause_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "isPause=" + isPause);
                isPause = !isPause;
                if (isPause) {
                    btn_pause_resume.setText("resume");
                } else {
                    btn_pause_resume.setText("pause");
                }
            }
        });
    }

    /**
     * Create a <code>Timer</code> and a <code>TimerTask</code> and start the task.
     */
    private void starTimer() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == timerTask) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(UPDATE_TEXTVIEW);
                   do{
                        try {
                            Log.d(TAG, "sleep " + PERIOD);
                            Thread.sleep(PERIOD);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while(isPause);
                    count++;
                }
            };
        }
            timer.schedule(timerTask, DELAY, PERIOD);
    }

    /**
     * Stop the <code>Timer</code> and the <code>TimerTask</code>.
     */
    private void stopTimer() {
        if (null != timer) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
