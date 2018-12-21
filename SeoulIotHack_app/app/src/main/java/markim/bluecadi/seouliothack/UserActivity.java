package markim.bluecadi.seouliothack;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.minew.beacon.MinewBeaconManager;

import java.util.Timer;
import java.util.TimerTask;

public class UserActivity extends AppCompatActivity {

    ////////////// 복사시작 /////////////////
    ImageView img_direction;
    ImageView img_direction_left;
    ImageView img_direction_right;
    ImageView img_direction_down;

    private MinewBeaconManager minewBeaconManager;
    private static final int REQUEST_ENABLE_BT = 2;

    static int counter = 0;
    private boolean isScanning = false;
    private final Handler handler = new Handler();

    ////////////// 복사 끝 /////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        img_direction = (ImageView)findViewById(R.id.img_direction);
        img_direction_left = (ImageView)findViewById(R.id.img_direction_left);
        img_direction_right = (ImageView)findViewById(R.id.img_direction_right);
        img_direction_down = (ImageView)findViewById(R.id.img_direction_down);

        findMyWay();

    }

    ///////// 복사 시작 ///////////////////

    private void findMyWay()
    {
        img_direction.setVisibility(View.INVISIBLE);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                showDirection();
                counter++;
            }
        };

        Timer timer = new Timer();
        timer.schedule(tt, 0, 5000);
    }

    private void showDirection()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(counter == 1)
                {
                    img_direction.setVisibility(View.VISIBLE);
                }
                else if(counter == 2)
                {
                    img_direction.setVisibility(View.INVISIBLE);
                    img_direction_left.setVisibility(View.VISIBLE);
                }
                else if(counter == 4)
                {
                    img_direction_left.setVisibility(View.INVISIBLE);
                    img_direction_down.setVisibility(View.VISIBLE);
                }

                else if(counter == 5)
                {
                    img_direction_down.setVisibility(View.INVISIBLE);
                    img_direction_right.setVisibility(View.VISIBLE);
                }
            }
        };
        handler.post(runnable);
    }

    //////// 복사 끝 ////////////////////////

    ///////// 복사 시작 /////////////////////
    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isScanning) {
            minewBeaconManager.stopScan();
        }
    }

    //////// 복사 끝 ///////////////////////////
}
