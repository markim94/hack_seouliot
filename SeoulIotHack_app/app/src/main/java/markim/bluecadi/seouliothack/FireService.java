package markim.bluecadi.seouliothack;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FireService extends Service {

    String fireState;
    Thread thread;
    String fireFighter;


    @Nullable
    private final IBinder mBinder = new LocalBinder();

    class LocalBinder extends Binder {
        FireService getService() {
            return FireService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        fireFighter = intent.getStringExtra("firefighter");
        return startId;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_SHORT).show();
        new ServiceTask().execute("1");

    }

    /**

     // 서버에 5초마다 접근하여 상태 확인 스레드
     private class StateThread extends Thread {
     // 의미없는 string
     String msg2 = "";

     public StateThread() {
     // 초기화 작업
     }
     public void run() {
     while(true){
     try{
     Map<String, String> map = new HashMap<>();
     // 키 id에 id를 put(입력)
     // 의미없는 값 전달
     map.put("null", msg2);

     // urls "users"에 map 데이터 보냄. 요청방식은 get
     JSONObject json = SC.Json_Messenger("state", SC.GET, map);

     try {
     state = json.get("state").toString();
     } catch (JSONException e) {
     e.printStackTrace();
     }
     // 화재 발생시
     if(state.equals("1")){
     new ServiceTask().execute("1"); //로그인 시도

     } else { // 평상시


     }
     Thread.sleep(5000);
     } catch (InterruptedException e){
     e.printStackTrace();
     }
     }
     }
     }
     *
     */




    // asynctask onprogressupdate 미사용으로 void
    public class ServiceTask extends AsyncTask<String, Void, String> {
        String msg2 = "";

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
                map.put("msg", msg2);

                // urls "users"에 phonenumber 데이터 보냄. 요청방식은 get
                JSONObject json = SC.Json_Messenger("state", SC.GET, map);

                try {
                    // json에서 resultCode를 받아서 toString하고, 이를 resultCode 변수에 저장
                    // String resultCode = json.get("resultCode").toString();
                    // 로그 출력
                    // Log.i("SC_GET MSG 1", "" + json.get("resultCode"));

                    // result
                    //if(resultCode.equals("0")){
                    //    return "0";
                    //}

                    // 200 출력시 로그인 성공으로
                    //if(resultCode.equals("200")){
                    //로그인 성공. 정보 받기

                    // 데이터 긁어오기

                    while(true){
                        fireState = json.get("fireState").toString();
                        if(fireState.equals("1")){
                            // 화재발생시 onPostExecute로
                            return "1";
                        } else{
                            // 평상시 일시 5초 이후에 다시 상태확인
                            thread.sleep(500);
                        }

                    }

                    //}
                    // else{
                    //    return "100";
                    //}

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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
            // 1출력시 정상접속. 이전에 받아온 firefighter가 0과 1일경우 분류해서 인텐트 전환
            if (result.compareTo("1") == 0) {
                //로그인 성공
                if (fireFighter.equals("0")) { //일반 사용자
                    Intent intent2 = new Intent(FireService.this, UserActivity.class);
                    startActivity(intent2);
                } else if (fireFighter.equals("1")) { //소방관
                    Intent intent2 = new Intent(FireService.this, ManagerActivity.class);
                    startActivity(intent2);
                }
            }
        }

    }

}