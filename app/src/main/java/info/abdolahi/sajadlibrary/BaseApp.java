package info.abdolahi.sajadlibrary;

import android.app.Application;

import com.flurry.android.FlurryAgent;

/**
 * Created by aliabdolahi on 10/24/15 AD.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, "GBWPV65CHHHNV7HMQ8Y9");
    }
}
