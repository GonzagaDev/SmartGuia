package auditsolution.com.br.smartguiabluetooth;

import android.app.Application;
import android.content.Context;

/**
 * Created by Win10-Home on 26/09/2016.
 */
public class App extends Application {
    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}