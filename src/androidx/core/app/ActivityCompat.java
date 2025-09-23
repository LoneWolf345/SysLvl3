package androidx.core.app;

import android.app.Activity;

public final class ActivityCompat {
    private ActivityCompat() {
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (activity == null || permissions == null) {
            return;
        }
        activity.requestPermissions(permissions, requestCode);
    }
}
