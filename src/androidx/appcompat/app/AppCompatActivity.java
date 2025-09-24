package androidx.appcompat.app;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

public class AppCompatActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setSupportActionBar(Toolbar toolbar) {
        // No-op stub for non-Android environment.
    }
}
