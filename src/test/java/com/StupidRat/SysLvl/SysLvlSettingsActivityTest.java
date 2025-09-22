package com.StupidRat.SysLvl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.android.controller.ActivityController;

@RunWith(LenientRobolectricTestRunner.class)
@Config(sdk = 33)
public class SysLvlSettingsActivityTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();
        context.getSharedPreferences(SysLvlActivity.SYSLVL_PREFS, Context.MODE_PRIVATE).edit().clear().commit();
    }

    @Test
    public void focusChanges_persistAndReloadLaunchLevelPreferences() {
        SharedPreferences prefs = context.getSharedPreferences(SysLvlActivity.SYSLVL_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putInt("HighFreqOutput", 45).putInt("LowFreqOutput", 36).commit();

        ActivityController<TestSysLvlSettingsActivity> controller = Robolectric.buildActivity(TestSysLvlSettingsActivity.class).setup();
        TestSysLvlSettingsActivity activity = controller.get();

        EditText highInput = (EditText) activity.findViewById(R.id.editTextHighFreqPref);
        EditText lowInput = (EditText) activity.findViewById(R.id.editTextLowFreqPref);
        TextView currentLevels = (TextView) activity.findViewById(R.id.tvCurrentLvls);

        assertEquals("45", highInput.getText().toString());
        assertEquals("36", lowInput.getText().toString());
        assertEquals("Current starting levels: 45/36", currentLevels.getText().toString());

        View.OnFocusChangeListener highListener = highInput.getOnFocusChangeListener();
        assertNotNull(highListener);
        highInput.setText("50");
        highListener.onFocusChange(highInput, false);

        assertEquals(50, prefs.getInt("HighFreqOutput", -1));
        assertEquals("Current starting levels: 50/36", currentLevels.getText().toString());

        View.OnFocusChangeListener lowListener = lowInput.getOnFocusChangeListener();
        assertNotNull(lowListener);
        lowInput.setText("39");
        lowListener.onFocusChange(lowInput, false);

        assertEquals(39, prefs.getInt("LowFreqOutput", -1));
        assertEquals("Current starting levels: 50/39", currentLevels.getText().toString());

        lowInput.setText("");
        lowListener.onFocusChange(lowInput, false);
        assertEquals("39", lowInput.getText().toString());
        assertNotNull("Expect validation error when empty", lowInput.getError());

        controller.pause().stop().destroy();
    }

    private static class TestSysLvlSettingsActivity extends SysLvlSettingsActivity {
        private final java.util.Map<Integer, View> viewMap = new java.util.HashMap<Integer, View>();

        @Override
        public void setContentView(int layoutResID) {
            android.widget.LinearLayout root = new android.widget.LinearLayout(this);
            root.setOrientation(android.widget.LinearLayout.VERTICAL);

            android.widget.Button reset = new android.widget.Button(this);
            reset.setId(R.id.ResetButton);
            viewMap.put(R.id.ResetButton, reset);
            root.addView(reset);

            android.widget.EditText high = new android.widget.EditText(this);
            high.setId(R.id.editTextHighFreqPref);
            viewMap.put(R.id.editTextHighFreqPref, high);
            root.addView(high);

            android.widget.EditText low = new android.widget.EditText(this);
            low.setId(R.id.editTextLowFreqPref);
            viewMap.put(R.id.editTextLowFreqPref, low);
            root.addView(low);

            android.widget.TextView currentLevels = new android.widget.TextView(this);
            currentLevels.setId(R.id.tvCurrentLvls);
            viewMap.put(R.id.tvCurrentLvls, currentLevels);
            root.addView(currentLevels);

            super.setContentView(root);
        }

        @Override
        public View findViewById(int id) {
            View mapped = viewMap.get(id);
            if (mapped != null) {
                return mapped;
            }
            return super.findViewById(id);
        }
    }
}
