package com.smartyvpn.tfsmarty;

import android.net.Uri;

import com.smartyvpn.tfsmarty.R;
import com.smartyvpn.tfsmarty.model.Server;

public class Utils {

    public static Server selectedServer;

    /**
     * Convert drawable image resource to string
     *
     * @param resourceId drawable image resource
     * @return image path
     */
    public static String getImgURL(int resourceId) {

        // Use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }
}
