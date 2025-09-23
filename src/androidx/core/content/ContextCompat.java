package androidx.core.content;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public final class ContextCompat {
    private ContextCompat() {
    }

    public static int checkSelfPermission(Context context, String permission) {
        if (context == null || permission == null) {
            return PackageManager.PERMISSION_DENIED;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(permission);
        }
        return PackageManager.PERMISSION_GRANTED;
    }
}
