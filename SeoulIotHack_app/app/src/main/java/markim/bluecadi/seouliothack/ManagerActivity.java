package markim.bluecadi.seouliothack;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManagerActivity extends AppCompatActivity {

    ListView listFloor;
    ImageView imgFloor;
    ListViewAdapter adapter;

    ImageButton btnDirection;
    ImageButton btnDirection2;
    TextView txtStateView;


    /**
     *
    String msg3;
    String floor;
    JSONArray array;

    String aFloor;
    String aState;
    String aPerson;
     */

    private DrawerLayout drawerLayout;
    private View drawerView;

    // 이미지 배열
    int[] imgs = {R.drawable.floor1, R.drawable.floor2,R.drawable.floor3, R.drawable.floor4,R.drawable.floor5, R.drawable.floor6};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        listFloor = (ListView) findViewById(R.id.listFloor);
        imgFloor = (ImageView) findViewById(R.id.imgFloor);

        txtStateView = (TextView) findViewById(R.id.txtStateView);

        adapter = new ListViewAdapter();
        listFloor.setAdapter(adapter);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        drawerLayout.openDrawer(drawerView);

        drawerLayout.addDrawerListener(myDrawerListener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });



        btnDirection = (ImageButton) findViewById(R.id.btnDirection);
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        btnDirection2 = (ImageButton) findViewById(R.id.btnDirection2);
        btnDirection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        adapter.addItem("1층","0", "0명 예상");
        adapter.addItem("2층","0","0명 예상");
        adapter.addItem("3층","0","0명 예상");
        adapter.addItem("4층","1","2명 예상");
        adapter.addItem("5층","0","0명 예상");
        adapter.addItem("6층","0","0명 예상");

        listFloor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // imgFloor 이미지 변경 설정
                imgFloor.setImageResource(imgs[position]);
                drawerLayout.closeDrawer(drawerView);
                switch (position){
                    case 0:
                        txtStateView.setText("1층, 0명 예상, 화재상황아님");
                        break;
                    case 1:
                        txtStateView.setText("2층, 0명 예상, 화재상황아님");
                        break;
                    case 2:
                        txtStateView.setText("3층, 0명 예상, 화재상황아님");
                        break;
                    case 3:
                        txtStateView.setText("4층, 2명 예상, 화재상황");
                        break;
                    case 4:
                        txtStateView.setText("5층, 0명 예상, 화재상황아님");
                        break;
                    case 5:
                        txtStateView.setText("6층, 0명 예상, 화재상황아님");
                        break;
                }
            }
        });


    }
    DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener() {

        public void onDrawerClosed(View drawerView) {
            //txtPrompt.setText("onDrawerClosed");
        }

        public void onDrawerOpened(View drawerView) {
            //txtPrompt.setText("onDrawerOpened");
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
            //txtPrompt.setText("onDrawerSlide: "
            //       + String.format("%.2f", slideOffset));
        }

        public void onDrawerStateChanged(int newState) {
            String state;
            switch (newState) {
                case DrawerLayout.STATE_IDLE:
                    state = "STATE_IDLE";
                    break;
                case DrawerLayout.STATE_DRAGGING:
                    state = "STATE_DRAGGING";
                    break;
                case DrawerLayout.STATE_SETTLING:
                    state = "STATE_SETTLING";
                    break;
                default:
                    state = "unknown!";
            }

            //txtPrompt2.setText(state);
        }
    };


    /*
    public class ListTask extends AsyncTask<String, Void, String> {
        String msg = "";

        // 백그라운드 작업이 시작되기 전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        //pre실행되고 나서 동작
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];

            if (type.compareTo("1") == 0) {

                // 쌍으로 키와 값을 저장하는 HashMap
                Map<String, String> map = new HashMap<>();
                // 키 id에 id를 put(입력)
                map.put("msg", msg3);

                JSONObject json = SC.Json_Messenger("monitor", SC.GET, map);

                try {
                    // 데이터 긁어오기
                    // 총 층수
                    floor = json.get("floor").toString();
                    // 층수와 해당 층수 화재상태, 감지인원을 담은 array
                    array = json.getJSONArray("array");

                    //
                    // array split 과정
                    //

                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject jsonObject = array.getJSONObject(i);
                        aFloor = jsonObject.get("aFloor").toString();
                        aState = jsonObject.get("aState").toString();
                        aPerson = jsonObject.get("aPerson").toString();

                        adapter.addItem(aFloor, aState, aPerson);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "1";
            }
            return null;
        }


        // 백그라운드 작업 완료후 결과값을 얻음.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.compareTo("1") == 0) {
                //로그인 성공
            }
        }

    }
    */


}
