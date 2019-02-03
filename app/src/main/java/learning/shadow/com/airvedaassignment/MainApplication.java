package learning.shadow.com.airvedaassignment;

import android.app.Application;
import learning.shadow.com.airvedaassignment.adapter.ConnectivityReceiver;

public class MainApplication extends Application {

    private static MainApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}