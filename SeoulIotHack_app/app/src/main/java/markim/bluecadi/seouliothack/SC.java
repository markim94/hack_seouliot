package markim.bluecadi.seouliothack;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class SC {
    public static final String URL = "http://52.78.112.67:8080/";
    public static final String GET = "GET"; //조회
    public static final String POST = "POST"; //생성
    public static final String PUT = "PUT"; //수정
    public static final String DELETE = "DELETE"; //삭제



    /**
     * JSON 형식으로 주고 받아서 리턴한다.
     * @param order URL 뒤에 붙는 주소
     * @param _case GET 조회, POST 생성, PUT 수정, DELETE 삭제
     * @param send_data 보낼 인자
     * @return
     */
    public static JSONObject Json_Messenger(String order, String _case, Map<String, String> send_data){

        try {
            java.net.URL u = new URL(URL + order);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod(_case);

            Log.e("SC_SEND ", order + ":  " + _case);
            Iterator<String> iterator = send_data.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();

                Log.e("SC_SEND ",key+":  "+send_data.get(key));
                huc.setRequestProperty(key, URLEncoder.encode(send_data.get(key), "UTF-8"));
            }

            huc.connect();

            String line = null;
            BufferedReader rd = new BufferedReader(new InputStreamReader(huc.getInputStream(), "UTF-8"));

            while ((line = rd.readLine()) != null) {
                Log.i("SC_GET MSG", "" + line);
                return new JSONObject(line);
            }
            rd.close();

        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e) {
            System.out.println("Exception...");
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public static JSONArray JSONArray_Messenger(String order, String _case, Map<String, String> send_data){

        try {
            Log.i("#SC_SEND MSG", "" + URL + order+" "+_case);
            java.net.URL u = new URL(URL + order);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod(_case);

            Iterator<String> iterator = send_data.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                huc.setRequestProperty(key, send_data.get(key));
            }

            huc.connect();

            InputStream is = huc.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            String line = null;
            BufferedReader rd = new BufferedReader(new InputStreamReader(huc.getInputStream(), "UTF-8"));

            while ((line = rd.readLine()) != null) {
                Log.i("#SC_GET MSG", "" + line);
                return new JSONArray(line);
            }
            rd.close();

        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e) {
            System.out.println("Exception...");
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
