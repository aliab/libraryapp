package info.abdolahi.sajadlibrary.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Utility Class
 */
public class NikUtility {
	
	private Context context;
	public NikUtility(Context _context) {
		
		context = _context;
	}
	public String getDeviceName() {
		
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		
		if ( model.startsWith(manufacturer) ) {
			
			return capitalize(model);
			
		}else{
			
			return capitalize(manufacturer) + " " + model;
			
		}
		
	}
		
	public String capitalize(String s) {
		
		if (s == null || s.length() == 0) {
			
			return "";
			
		}
		
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			
			return s;
		
		} else {
			
			return Character.toUpperCase(first) + s.substring(1);
		
		}
		
	}

	public String getDeviceMACAddress() {
		
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		String address = info.getMacAddress();
		
		return address;
		
	}

	public String getCarrierName() {
		
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String carrierName = manager.getNetworkOperatorName();
		return carrierName;
		
	}

	public String getApplicationVersion() {
		
		PackageInfo pInfo;
		
		try {
			
			pInfo = context.getPackageManager().getPackageInfo(  context.getPackageName(), 0);
			String versionName = pInfo.versionName;
			return versionName;
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return "";
		
	}

    public int getApplicationVersionCode() {

        PackageInfo pInfo;

        try {

            pInfo = context.getPackageManager().getPackageInfo(  context.getPackageName(), 0);
            int versionCode = pInfo.versionCode;
            return versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }
	
	public String getApplicationSecureID() {
		
		String deviceID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
		return deviceID;
		
	}
	
	public String getAndroidVersion() {
		
		return Build.VERSION.RELEASE;
		
	}
	
	public boolean hasInternetConnection() {
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo ( ConnectivityManager.TYPE_WIFI );
		if ( wifiNetwork != null && wifiNetwork.isConnected() ) {
			
			return true;
			
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo ( ConnectivityManager.TYPE_MOBILE );
		if ( mobileNetwork != null && mobileNetwork.isConnected() ) {
			
			return true;
			
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if ( activeNetwork != null && activeNetwork.isConnected() ) {
			
			return true;
			
		}

		return false;
		
	}
	
	public boolean isLocationServiceEnabled(){
		
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
	}

    public int dpToPx(int dp) {

        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;

    }

    public void imageViewAsButton(ImageView imageView) {

        //set the ontouch listener
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                /**
                 * If you return true from the onTouch method,
                 * Android will consider it consumed and not pass it on to the other various touch handlers (which I am assuming will include the ClickListener).
                 * http://stackoverflow.com/questions/5283293/ontouch-works-but-onclicklistener-doesnt
                 */
                return false;
            }
        });

    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public int getFontSizeAdaptedInDP(int dp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        int pixels = (int) (metrics.density * dp + 0.5f);
        return pixels;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[32 * 1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void closeKeyboard(){
        // hiding soft keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int convertPersianNumberToEnglishNumber(String number){
        StringBuffer enNumSB = new StringBuffer();

        for(int i = 0; i < number.length(); i++){
            switch(number.charAt(i)){
                case '۰':
                    enNumSB.append('0');
                    break;
                case '۱':
                    enNumSB.append('1');
                    break;
                case '۲':
                    enNumSB.append('2');
                    break;
                case '۳':
                    enNumSB.append('3');
                    break;
                case '۴':
                    enNumSB.append('4');
                    break;
                case '۵':
                    enNumSB.append('5');
                    break;
                case '۶':
                    enNumSB.append('6');
                    break;
                case '۷':
                    enNumSB.append('7');
                    break;
                case '۸':
                    enNumSB.append('8');
                    break;
                case '۹':
                    enNumSB.append('9');
                    break;

            }
        }

        if(enNumSB.length() == 0){
            return 0;
        }else{
            return Integer.parseInt(enNumSB.toString());
        }
    }

    public String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }

    public String sha1(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data)).toLowerCase();
    }

    public String getDefaultMailAddress(){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        String possibleEmail = null;
        for (Account account : accounts) {
            Log.d("account", account.name);

            if (emailPattern.matcher(account.name).matches()) {
                 possibleEmail= account.name;
                if (possibleEmail.endsWith("gmail.com")){
                    Log.d("PossibleEmail", possibleEmail);
                    return possibleEmail;
                }
            }
        }
        return possibleEmail;
    }

}
