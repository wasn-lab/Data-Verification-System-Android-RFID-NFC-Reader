package wasn_ncu.yu_zhang.nfcreader;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Yu-Zhang on 6/4/15.
 */
public class A_Reader extends Activity
{
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private boolean silent = false , writeProtect = false , canWrite = false , canRead = false;

    private boolean gResumed = true;
    private boolean gWriteMode = true;
    private static final String TAG = "nfcproject";

    final Handler handler = new Handler();

    private NfcAdapter mNFCAdapter = null;
    protected PendingIntent PIntent = null;
    private IntentFilter F_list[];

    private JSONArray jsonArray;

    Random rand = new Random();

    // Operation Arguments
    String Signature = "";
    int Nsp = 0 , Csp = 0;

    Dialog About = null;
    TextView MSG = null , fromTag = null;
    EditText setMsg = null;

    int index = 0;
    String db_host, RK;
    List<String> tagid_list = new ArrayList<String>();
    List<String> data_list = new ArrayList<String>();
    List<String> ram_num_list = new ArrayList<String>();
    List<String> tcount_list = new ArrayList<String>();
    String data, ram_num_t;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_reader);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        this.MSG = (TextView) findViewById( R.id.message );
        this.fromTag = (TextView) findViewById( R.id.fromTag );
        this.setMsg = (EditText) findViewById( R.id.new_info_to_tag );

        this.setupNFC();
        this.setData();
    }

    private void setupNFC()
    {
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);

        try
        {
            if( !mNFCAdapter.isEnabled() )
                Toast.makeText(this, "Please Activate NFC!", Toast.LENGTH_LONG).show();
            else
            {
                Toast.makeText( this , "NFC!" , Toast.LENGTH_LONG ).show();

                this.PIntent = PendingIntent.getActivity( this , 0 , new Intent( this , this.getClass() ).addFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP ) , 0 );

                IntentFilter tagDetected = new IntentFilter( this.mNFCAdapter.ACTION_TAG_DISCOVERED );
                IntentFilter ndefDected = new IntentFilter( this.mNFCAdapter.ACTION_NDEF_DISCOVERED );
                IntentFilter techDected = new IntentFilter( this.mNFCAdapter.ACTION_TECH_DISCOVERED );

                this.F_list = new IntentFilter[]{ tagDetected , ndefDected , techDected };

                // Close the reader first!
                this.canWrite = false;
                this.canRead = false;
            }
        }
        catch( NullPointerException NPE)
        {
            MSG.setText( "No NFC on this device!" );
            Toast.makeText( this.getApplicationContext() , "No NFC on this device!" , Toast.LENGTH_LONG ).show();
        }
    }

    private void setData()
    {
        Bundle data_bundle = this.getIntent().getExtras();
        db_host = data_bundle.getString("db_host");
        RK = data_bundle.getString("rk");


//        try {
//            jsonArray = new JSONArray(data_bundle.getString("data"));
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonData = jsonArray.getJSONObject(i);
//
//                fromTag.append(jsonData.getString("signature_by_sp") + "\n");
//            }
//        }
//        catch(Exception e){
//            Log.e("log_tag", e.toString());
//        }

    }

    public void Read( View v )
    {
        this.MSG.setText( "Read the Tag." );
        this.canRead = true;
        this.canWrite = false;
    }

    public void Write( View v )
    {
        if( setMsg.getText().toString().equalsIgnoreCase( "" ) )
            this.MSG.setText("NO MESSAGE SET UP!");
        else
        {
            // The Random Num (Nsp, j)
            // 300 is the maximum and the 1 is our minimum
            Nsp = rand.nextInt( 300 ) + 1;
            // Increment of the Reader counter
            Csp++;

            this.MSG.setText( "Operation Start." );
            this.canWrite = true;
            this.canRead = false;
        }
    }

    public void Update( View v )
    {
        Log.e( "Oper_Upload", "Upload clicked!" );

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String CurrentTime = sdf.format(cal.getTime()).toString();

//        if( jsonArray != null )
//        {
        try
        {
            for ( int i = 0 ; i < data_list.size() ; i++ )
            {
//                    call_update.executeQuery(db_host, "UPDATE `infos` SET `signature_by_tag` = \"" + data_list.get(i) + "\" WHERE `tag_id` = \"" + jsonArray.getJSONObject(i).getString("tag_id") + "\"");
//                    call_update.executeQuery(db_host, "UPDATE `infos` SET `ran_num_by_tag` = \"" + ram_num_list.get(i) + "\" WHERE `tag_id` = \"" + jsonArray.getJSONObject(i).getString("tag_id") + "\"");
//                    call_update.executeQuery(db_host, "UPDATE `infos` SET `tag_counter` = \"" + tcount_list.get(i) + "\" WHERE `tag_id` = \"" + jsonArray.getJSONObject(i).getString("tag_id") + "\"");
//                    call_update.executeQuery(db_host, "INSERT INTO infos (id, tag_id, signature_by_sp, ran_num_by_sp, reader_key, sp_counter, data, signature_by_tag, ran_num_by_tag, tag_counter, remember_token, created_at, updated_at ) VALUES (\""+ jsonArray.getJSONObject(i).getString("tag_id") + "\", \""+ jsonArray.getJSONObject(i).getString("tag_id") +"\", ’test’, '438', '123', '0', '',\"" + data_list.get(i) +"\", \"" + ram_num_list.get(i) + "\", \"" + tcount_list.get(i) + "\", NULL, 20150606, 20150606);" );
//                    call_update.executeQuery(db_host, "INSERT INTO test (id, data) VALUES ('2', 'hippo')");
//                    call_update.executeQuery(db_host, "UPDATE `test` SET `data` = \"" + tcount_list.get(i) + "\" WHERE `id` = 0;");

                String sql =
                        "INSERT INTO Operation (" +
                        "Oper_no," +
                        "Tag_ID," +
                        "SP_RK," +
                        "SP_Signature," +
                        "RanNum_SP," +
                        "Counter_SP," +
                        "Data," +
                        "Tag_Signature," +
                        "RanNum_Tag," +
                        "Counter_Tag," +
                        "Created," +
                        "Updated )" +
                        "VALUES ( \'" +
                        Csp + "\', \'" +
                        tagid_list.get(Csp - 1) + "\', \'" +
                        RK + "\', \'" +
                        Signature + "\', \'" +
                        Nsp +"\', \'" +
                        Csp + "\', \'" +
                        setMsg.getText().toString() + "\', \'" +
                        data_list.get(Csp - 1) + "\', \'" +
                        ram_num_list.get(Csp - 1) + "\', \'" +
                        tcount_list.get(Csp - 1) + "\', \'" +
                        CurrentTime + "\', \'" +
                        CurrentTime + "\')";
                call_update.executeQuery( db_host , sql );
                Log.d( "SQL" , sql );
            }
            MSG.setText("update DB success ");
        }
        catch(Exception e)
        {
            Log.e("log_tag", e.toString());
        }
//        }
    }

    public void clear( View v )
    {
        this.fromTag.setText( "" );
        this.MSG.setText("");
        this.canRead = false;
        this.canWrite = false;

        this.MSG.setText("Terminated.");
    }

    public void back( View v )
    {
        super.onBackPressed();
    }

    private void callHandler( final String text )
    {
        handler.post( new Runnable()
        {
            int  counter = 2;
            public void run()
            {
                if ( counter > 0)
                {
                    counter--;
                    handler.postDelayed(this, 1000);
                    MSG.setText(text);
                }
                else
                {
                    MSG.setText("");
                }
            }
        });
    }

    private void handleIntent( Intent intent )
    {
        // TODO: handle Intent
        String action = intent.getAction();

        if( this.mNFCAdapter.ACTION_TAG_DISCOVERED.equals( action ) )
        {
            Tag tag;
            if( !this.canWrite && !this.canRead )
                return;
            else
            {
                tag = intent.getParcelableExtra( this.mNFCAdapter.EXTRA_TAG );
                this.MSG.setText( "TAG!" );
            }

            if( this.canRead )
            {
                NdefReaderTask NRT = new NdefReaderTask( this , this.handler , null );
                NRT.execute( tag );
            }
            if( this.canWrite )
            {
                // validate that this tag can be written
                if( supportedTechs( tag.getTechList() ) )
                {
                    // check if tag is writable (to the extent that we can
                    if( writableTag( tag ) )
                    {
                        //writeTag here
                        WriteResponse wr = writeTag( getTagAsNdef() , tag );
                        String message = (wr.getStatus() == 1 ? "Success!\n" : "Failed!\n") + wr.getMessage();
                        this.fromTag.setText( message );
//						Sounds.PlaySuccess( this , silent );
                    }
                    else
                    {
                        this.fromTag.setText( "This tag is not writable" );
//						Sounds.PlayFailed( this , silent );
                    }
                }
                else
                {
                    this.fromTag.setText( "This tag type is not supported" );
//					Sounds.PlayFailed( this , silent );
                }
            }
        }
//		else if( this.mNFCAdapter.ACTION_TECH_DISCOVERED.equals( action ) )
//		{
//			// In case we would still use the Tech Discovered Intent
//			Tag tag = intent.getParcelableExtra( this.mNFCAdapter.EXTRA_TAG );
//			String[] techList = tag.getTechList();
//			String searchedTech = Ndef.class.getName();
//
//			for( String tech : techList )
//			{
//				if( searchedTech.equals( tech ) )
//				{
//					callHandler( "TECH Get!!" );
//					NdefReaderTask NRT = new NdefReaderTask( this , this.handler , null );
//					NRT.execute( tag );
//
//					break;
//				}
//			}
//		}
    }

    private NdefMessage getTagAsNdef()
    {
        // ***** The Operation H( Rk | Data | Csp | Nsp ) *****
        String uniqueId;
        // The Data prepared to write to the Tag!!!!!!!!!!!!!
        if ( Csp < 10 )
            uniqueId = this.RK + this.setMsg.getText().toString().replace("\n", "").replace("\r", "") + "0" + Csp + Nsp;
        else if ( Csp > 99)
            uniqueId = this.RK + this.setMsg.getText().toString().replace("\n", "").replace("\r", "") + "99" + Nsp;
        else
            uniqueId = this.RK + this.setMsg.getText().toString().replace("\n", "").replace("\r", "") + Csp + Nsp;

        Log.d( "W Data (non-MD5)" , uniqueId );
        Signature = uniqueId = MD5.getMD5( uniqueId );
        Log.d( "W Data (MD5ed)" , uniqueId );

//        try
//        {
//            JSONObject jsonData = jsonArray.getJSONObject(index);
//            uniqueId = jsonData.getString("signature_by_sp");
//            toast( uniqueId );
//        }
//        catch (Exception e) { Log.e("log_tag", e.toString()); }

        byte[] uriField = uniqueId.getBytes( Charset.forName("UTF-8") );

        // add 1 for the URI Prefix
        byte[] payload = new byte[ (uriField.length + 1) ];
//
//		// prefixes http://www. to the URI
//		payload[ 0 ] = 0x01;

        System.arraycopy( uriField , 0 , payload , 1 , uriField.length ); // appends URI to payload

//        for( int i = 0 ; i < (uriField.length + 1) ; i++ )
//            Log.d( TAG, "Hippo to write " + i + ": " + payload[ i ] );

        NdefRecord rtdUriRecord = new NdefRecord( NdefRecord.TNF_WELL_KNOWN , NdefRecord.RTD_TEXT , new byte[ 0 ] , payload );

//		String Text = this.setMsg.getText().toString();
//		byte[] payload = Text.getBytes( Charset.forName( "UTF-8" ) );
//		NdefRecord rtdUriRecord = new NdefRecord( NdefRecord.TNF_WELL_KNOWN , NdefRecord.RTD_TEXT , new byte[ 0 ] , payload );

        return new NdefMessage( new NdefRecord[] { rtdUriRecord });
    }

    public WriteResponse writeTag( NdefMessage message , Tag tag )
    {
        int size = message.toByteArray().length;
        String mess = "";

        try
        {
            Ndef ndef = Ndef.get(tag);
            if( ndef != null )
            {
                ndef.connect();
                if( !ndef.isWritable() )
                    return new WriteResponse( 0 , "Tag is read-only" );

                if( ndef.getMaxSize() < size )
                {
                    mess = "Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size + " bytes.";
                    return new WriteResponse( 0 , mess );
                }
                ndef.writeNdefMessage( message );

                if( writeProtect ) ndef.makeReadOnly();

                mess = "Wrote message to pre-formatted tag.";
                return new WriteResponse( 1 , mess );
            }
            else
            {
                NdefFormatable format = NdefFormatable.get( tag );
                if( format != null )
                {
                    try
                    {
                        format.connect();
                        format.format( message );
                        mess = "Formatted tag and wrote message";
                        return new WriteResponse( 1 , mess );
                    }
                    catch( IOException e )
                    {
                        mess = "Failed to format tag.";
                        return new WriteResponse( 0 , mess );
                    }
                }
                else
                {
                    mess = "Tag doesn't support NDEF.";
                    return new WriteResponse( 0 , mess );
                }
            }
        }
        catch( Exception e )
        {
            mess = "Failed to write tag";
            return new WriteResponse( 0 , mess );
        }
    }

    private boolean writableTag( Tag tag )
    {
        try
        {
            Ndef ndef = Ndef.get( tag );
            if( ndef != null )
            {
                ndef.connect();
                if( !ndef.isWritable() )
                {
                    Toast.makeText( this , "Tag is read-only." , Toast.LENGTH_SHORT ).show();
//					Sounds.PlayFailed( this , silent );
                    ndef.close();
                    return false;
                }
                ndef.close();
                return true;
            }
        }
        catch( Exception e )
        {
            Toast.makeText( this , "Failed to read tag" , Toast.LENGTH_SHORT ).show();
//			Sounds.PlayFailed( this , silent );
        }
        return false;
    }

    public static boolean supportedTechs( String[] techs )
    {
        for( String tech : techs )
        {
            Log.d( TAG , "Tech : " + tech );
            if( tech.equals( "android.nfc.tech.MifareUltralight" ) )
                return true;
            if(tech.equals( "android.nfc.tech.IsoDep" ) )
                return true;
            if( tech.equals( "android.nfc.tech.NfcA" ) )
                return true;
            if( tech.equals( "android.nfc.tech.NfcF" ) )
                return true;
            if( tech.equals( "android.nfc.tech.Ndef" ) || tech.equals( "android.nfc.tech.NdefFormatable" ) )
                return true;
        }
        return false;
    }

    private class WriteResponse
    {
        int status;
        String message;

        public WriteResponse( int Status , String Message )
        {
            this.status = Status;
            this.message = Message;
        }

        public int getStatus()
        {
            return status;
        }

        public String getMessage()
        {
            return message;
        }
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag , Void , String>
    {
        private Context context;
        private Handler handler;
        private String msg = null;

        public NdefReaderTask( Context context , Handler handler , String data )
        {
            this.context = context;
            this.handler = handler;
            this.msg = data;
        }

        @Override
        protected String doInBackground( Tag... params )
        {
            Tag tag = params[ 0 ];

            Ndef ndef = Ndef.get( tag );
            // NDEF is not supported by this Tag.
            if( ndef == null )
                return null;
            Log.d( TAG, "NDEF!!!" );
            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();

            String result = "";
            for( NdefRecord ndefRecord : records )
            {
                try
                {
                    result = readText( ndefRecord );
//                    resetContent( ndefMessage );
                }
                catch( UnsupportedEncodingException e )
                {
                    Log.e( TAG , "Unsupported Encoding" , e );
                }

//				if( ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals( ndefRecord.getType(), NdefRecord.RTD_TEXT ) )
//				{
//
//				}
            }
            if( !result.equals( "" ) )
                return result;
            else
                return null;
        }

        /**
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */
        private String readText( NdefRecord record ) throws UnsupportedEncodingException
        {
            Log.d( TAG , "Reading!!" );
            byte[] payload = record.getPayload();
            String textEncoding = "UTF-8";

//			// Get the Text Encoding
//			textEncoding = ((payload[ 0 ] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = 0;
            try
            {
                // Get the Language Code
                languageCodeLength = payload[ 0 ] & 0063;
                Log.d( TAG , "Hippo: " + String.valueOf( payload[ 0 ] ) );
            }
            catch( Exception exp )
            {   return "Message format ERROR!"; }

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            msg = new String( payload , (languageCodeLength + 1) , (payload.length - languageCodeLength - 1) , Charset.forName( textEncoding ) );
            Log.d(TAG, msg);

            String body = new String(msg);
            if ( body.length() == 25 )
            {
                tagid_list.add(body.substring(21,25));
                data_list.add(body.substring(0,16));
                ram_num_list.add(body.substring(16,19));
                tcount_list.add(body.substring(19,21));
                Log.e("Debug", body.substring(0, 16));
                Log.e("Debug", body.substring(16,19));
                Log.e("Debug", body.substring(19,21));
            }
            else
            {
                data_list.add("null");
                ram_num_list.add("0");
            }

            return msg;
        }

//        private void resetContent(final NdefMessage msg)
//        {
//            String body = new String(msg.getRecords()[0].getPayload());
//
//            if ( body.length() == 19 )
//            {
//                data_list.add(body.substring(0,15));
//                ram_num_list.add(body.substring(15,18));
//                Log.e("Debug", body.substring(0,15));
//                Log.e("Debug", body.substring(15,18));
//            }
//            else
//            {
//                data_list.add("null");
//                ram_num_list.add("0");
//            }
//        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result != null)
            {
                fromTag.setText( result );
            }
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        if( this.mNFCAdapter != null )
            this.mNFCAdapter.disableForegroundDispatch( this );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        if( this.mNFCAdapter != null )
            this.mNFCAdapter.enableForegroundDispatch(this, this.PIntent, this.F_list, null);
    }

    @Override
    protected void onNewIntent( Intent intent )
    {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent( intent );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id == R.id.action_settings )
        {
            About = new Dialog( A_Reader.this , R.style.Dialog );
            About.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            About.setTitle( "Login" );
            About.setContentView( R.layout.comp_dialog_about);
            About.show();

            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}