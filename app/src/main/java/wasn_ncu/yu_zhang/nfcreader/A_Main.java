package wasn_ncu.yu_zhang.nfcreader;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.util.Log;

import android.text.method.ScrollingMovementMethod;

import org.json.JSONArray;
import org.json.JSONObject;

public class A_Main extends Activity
{
    // Android Components
    private Button button_get_record;
    private Button button_check;
    private TextView t_id, sp_name , r_key;
    NfcAdapter mNFCAdapter = null;

    // Connection Informaion to the Database
    EditText ip1, ip2, ip3, ip4, port = null;
    EditText sp = null;

    // Application Arguments
    private String result;
    private boolean DBaccessed = false;

    // Operation Arguments
    String db_host = "", RK = "";
    int Csp = 0;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ip1 =(EditText) findViewById( R.id.ip1 );
        ip2 =(EditText) findViewById( R.id.ip2 );
        ip3 =(EditText) findViewById( R.id.ip3 );
        ip4 =(EditText) findViewById( R.id.ip4 );
        port = (EditText) findViewById( R.id.port );
        sp = (EditText) findViewById( R.id.sp );
        t_id = (TextView) findViewById( R.id.t_id );
        sp_name = (TextView) findViewById( R.id.sp_name );
        r_key = (TextView) findViewById( R.id.reader_key );
        this.r_key.setMovementMethod( ScrollingMovementMethod.getInstance() );

        button_get_record = (Button) findViewById( R.id.get_record );
        button_check = (Button) findViewById( R.id.check );
        button_get_record.setOnClickListener( getDBRecord );
        button_check.setOnClickListener( sendData );

        this.setupNFC();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    private void setupNFC()
    {
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);

        try
        {
            if (!mNFCAdapter.isEnabled())
                Toast.makeText(this, "Please Activate NFC!", Toast.LENGTH_LONG).show();
        }
        catch (NullPointerException NPE)
        { Toast.makeText(this.getApplicationContext(), "No NFC on this device!", Toast.LENGTH_LONG).show(); }
    }

    public void clear( View v )
    {
        this.t_id.setText( "" );
        this.sp_name.setText( "" );
        this.r_key.setText( "" );
        this.DBaccessed = false;
    }

    private Button.OnClickListener getDBRecord = new Button.OnClickListener()
    {
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if( sp.getText().toString().equalsIgnoreCase("") )
            {
                toast( "Please set the sp no.!!" );
                return;
            }

            db_host = ip1.getText().toString() + "." + ip2.getText().toString() + "."
                    + ip3.getText().toString() + "." + ip4.getText().toString() + ":" + port.getText().toString();
            toast( db_host );
            t_id.setText( "" );
            sp_name.setText( "" );
            r_key.setText("");

            try
            {
                result = call_database.executeQuery(db_host, "SELECT * FROM `SP_Infos` WHERE `SP_No` = " + sp.getText().toString());
                /*
                    SQL 結果有多筆資料時使用JSONArray
                    只有一筆資料時直接建立JSONObject物件
                    JSONObject jsonData = new JSONObject(result);
                */
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    try
                    {
                        t_id.setText( sp.getText().toString() );
                        sp_name.setText( jsonData.getString("SP_Name") );
                        RK = jsonData.getString("SP_ReaderKey");
                        r_key.append( RK + "\n");
                        Csp = Integer.parseInt( jsonData.getString("Reader_Counter") );
                        Log.d( "Operation", "RK = " + RK );
                        Log.d( "Operation", "RCsp = " + Csp );
                    }
                    catch ( Exception e) { toast( "Input Error!!" ); }
                }
                toast("success");

                DBaccessed = true;
            }
            catch(Exception e)
            {
                Log.e("log_tag", e.toString());
            }
        }
    };

    private Button.OnClickListener sendData = new Button.OnClickListener()
    {
        public void onClick(View v) {
            if ( DBaccessed )
            {
                Intent intent = new Intent();
                intent.setClass(A_Main.this, A_Reader.class);

                //result = call_database.executeQuery(db_host, "SELECT * FROM `infos`");
                Bundle data = new Bundle();
                data.putString("rk", RK);
                //data.putString("data", result);
                data.putString("db_host", db_host);
                data.putInt( "Csp", Csp );
                intent.putExtras(data);

                startActivity(intent);
            }
            else { toast( "Please access to DB first!" ); }
        }
    };

    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}