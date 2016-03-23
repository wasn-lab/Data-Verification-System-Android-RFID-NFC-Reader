package wasn_ncu.yu_zhang.nfcreader;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by jason on 2015/3/29.
 */
public class call_database {
    public static String executeQuery(String db_host, String query_string) {
        String result = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://"+db_host+"/rfid/get_data.php");

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query_string", query_string));

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            System.out.println(query_string);
            System.out.println(httpPost);

            HttpResponse httpResponse = httpClient.execute(httpPost);//**

            //view_account.setText(httpResponse.getStatusLine().toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();

            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");

                System.out.println(line);

            }
            inputStream.close();
            result = builder.toString();
            Log.e("log_tag", result);

            Log.e("log_tag", "DB connect success");
        } catch(Exception e) {

            Log.e("log_tag", e.toString());
        }

        return result;
    }
}