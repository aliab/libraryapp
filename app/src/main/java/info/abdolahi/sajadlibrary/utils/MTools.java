package info.abdolahi.sajadlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class MTools {

	/*
	 * base config
	 */
	public static Boolean debug = true;
	public static String app_name = "sajad library";

	Context mContext;
	public static SharedPreferences sharedpreferences;
	public static Editor editor;

	public MTools(Context context) {
		mContext = context;

	}

	public void mToast(String text) {

		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	public static void mToast(Context mContext, String text) {

		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	public static void mToast(Context mContext, int text) {

		Toast.makeText(mContext, String.valueOf(text), Toast.LENGTH_SHORT)
				.show();
	}

	public static void mToastResource(Context mContext, int ResourceID) {
		String text = mContext.getResources().getString(ResourceID);

		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	public static void mlog(String text) {
		if (debug) {
			Log.i(app_name, text);
		}

	}

	public static int mRandInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}


}
