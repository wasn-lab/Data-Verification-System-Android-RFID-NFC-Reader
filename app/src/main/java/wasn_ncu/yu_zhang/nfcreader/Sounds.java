package wasn_ncu.yu_zhang.nfcreader;

/**
 * Created by Yu-Zhang on 1/22/15.
 */
import android.content.Context;
import android.media.MediaPlayer;

public class Sounds
{
	boolean silent = false;

	//TODO: move mediaplayers to initialization

	public static void PlayFailed( Context context , boolean silent )
	{
		if( !silent )
		{
			MediaPlayer failedPlayer = MediaPlayer.create( context , R.raw.failed );

			failedPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener()
			{
				public void onCompletion(MediaPlayer mp)
				{
					mp.release();
				}
			});

			if( failedPlayer != null ) failedPlayer.start();
		}

	}

	public static void PlaySuccess( Context context , boolean silent )
	{
		if( !silent )
		{
			MediaPlayer player = MediaPlayer.create( context , R.raw.success );

			player.setOnCompletionListener( new MediaPlayer.OnCompletionListener()
			{
				public void onCompletion( MediaPlayer mp )
				{
					mp.release();
				}
			});

			if( player != null ) player.start();
		}
	}
}