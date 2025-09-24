package com.StupidRat.SysLvl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.StupidRat.SysLvl.legacy.LegacyServiceLocator;
import com.StupidRat.SysLvl.legacy.data.Span;
import com.StupidRat.SysLvl.legacy.data.SpanRepository;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class SysLvlScreen extends SysLvlActivity {

        private static final int ACTIVITY_CREATE = 0;
        private SpanRepository spanRepository;
        private NewQAAdapter adapter;
        private final ArrayList<String> spanSummaries = new ArrayList<String>();
        private final ArrayList<Span> spans = new ArrayList<Span>();
        private final SpanRepository.Observer spanObserver = new SpanRepository.Observer() {
                @Override
                public void onSpansChanged(List<Span> updatedSpans) {
                        spans.clear();
                        if (updatedSpans != null) {
                                spans.addAll(updatedSpans);
                        }
                        rebuildSpanSummaries();
                }
        };

    /** Called when the activity is first created. **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syslvl);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
                setSupportActionBar(toolbar);
                toolbar.setTitle(R.string.syslvl);
        }

        adapter = new NewQAAdapter(this);
        adapter.setData(new String[0]);

        try {
                spanRepository = LegacyServiceLocator.provideSpanRepository(this);
                spanRepository.addObserver(spanObserver);
        } catch (SQLException e) {
                Log.e(SysLvlActivity.DEBUG_TAG, "Unable to load spans", e);
        }

        ListView listView = (ListView) findViewById(R.id.l_list);
        listView.setAdapter(adapter);
        configureListInteractions(listView);

        MaterialButton addSpanButton = (MaterialButton) findViewById(R.id.ButtonAddSpan);
        addSpanButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                        createSpan();
                }
        });

        fillData();

    }

    public void onResume(){
        super.onResume();
        fillData();
    }

    public void createSpan(){
        if (spanRepository == null) {
                return;
        }
        try {
                spanRepository.createSpan(100, "P3 .625", "26v2p", 0, 0, 0, 0);
                spanRepository.updateAllSpanAttenuation();
        } catch (SQLException e) {
                Log.e(SysLvlActivity.DEBUG_TAG, "Failed to create span", e);
        }

        //Intent i = new Intent(this, SpanDetails.class);
        //startActivityForResult(i, ACTIVITY_CREATE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        fillData();
    }

    public void fillData(){



        final SharedPreferences prefs = getSharedPreferences(SYSLVL_PREFS , MODE_PRIVATE);
        TextView TextViewAmpOut = (TextView) findViewById(R.id.TextViewAmpOut);

        int AmpOutHigh = prefs.getInt("HighFreqOutput", 45);
        int AmpOutLow = prefs.getInt("LowFreqOutput", 36);
        //int AmpOutLow = prefs.getInt("LowFreqOutput", 0);
        String AmpOutText = "Starting "+AmpOutHigh+"/"+AmpOutLow;
        TextViewAmpOut.setText(AmpOutText);

        Log.v(SysLvlActivity.DEBUG_TAG, "Begin fillData");

        if (spanRepository != null) {
                try {
                        spanRepository.updateAllSpanAttenuation();
                } catch (SQLException e) {
                        Log.e(SysLvlActivity.DEBUG_TAG, "Failed to refresh spans", e);
                }
        }

    }

    private void configureListInteractions(ListView mList) {
        Log.v(SysLvlActivity.DEBUG_TAG, "Create the items that appear on the pop-up");
        final ActionItem detailsAction = new ActionItem();

                detailsAction.setTitle("Details");
                detailsAction.setIcon(getResources().getDrawable(R.drawable.information));

                final ActionItem upAction = new ActionItem();

                upAction.setTitle("Up");
                upAction.setIcon(getResources().getDrawable(R.drawable.up));

                final ActionItem downAction = new ActionItem();

                downAction.setTitle("Down");
                downAction.setIcon(getResources().getDrawable(R.drawable.down));


                final ActionItem deleteAction = new ActionItem();

                deleteAction.setTitle("Delete");
                deleteAction.setIcon(getResources().getDrawable(R.drawable.delete));

                /**
                 * Set the onClickListeners for spans
                 */
                Log.v(SysLvlActivity.DEBUG_TAG, "Set the onClickListeners for the spans");
                mList.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                final long row = position;
                                final long span_id = getSpanIdAtPosition(row);
                                if (span_id < 0) {
                                        return;
                                }

                                Log.v(SysLvlActivity.DEBUG_TAG, "Span clicked at position:"+row);
                                //Toast.makeText(SysLvlMainActivity.this, "Span clicked at position:"+row, Toast.LENGTH_SHORT).show();

                                final QuickAction mQuickAction  = new QuickAction(view);
                                final ImageView mMoreImage              = (ImageView) view.findViewById(R.id.i_more);
                                mMoreImage.setImageResource(R.drawable.ic_list_more_selected);

                                detailsAction.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                Log.v(SysLvlActivity.DEBUG_TAG, "Details button press for span "+row);

                                        try{
                                                Log.v(SysLvlActivity.DEBUG_TAG, "Setting up intent for SpanDetails on row "+row);
                                                Intent i = new Intent(SysLvlScreen.this, SpanDetails.class);

                                                Log.v(SysLvlActivity.DEBUG_TAG, "Set extras.");
                                                Log.v(SysLvlActivity.DEBUG_TAG, "mRowId: "+span_id);
                                                Log.v(SysLvlActivity.DEBUG_TAG, "mPosition: "+row);

                                                i.putExtra(SysLvlDbAdapter.KEY_ROWID, span_id);
                                                i.putExtra(SysLvlDbAdapter.KEY_POSITION, row);

                                                Log.v(SysLvlActivity.DEBUG_TAG, "Launching intent for SpanDetails on row "+row);
                                                startActivity(i);
                                        }catch (Exception e) {
                                                Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
                                                }
                                                mQuickAction.dismiss();
                                        }
                                });

                                upAction.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                Log.v(SysLvlActivity.DEBUG_TAG, "Up button press for span "+row);

                                                if (spanRepository == null) {
                                                        return;
                                                }
                                                try{
                                                        Log.v(SysLvlActivity.DEBUG_TAG, "Initiate MoveSpanUp");
                                                        spanRepository.moveSpanUp(row);
                                                        spanRepository.updateAllSpanAttenuation();
                                                }catch(SQLException e){
                                                        Log.e(SysLvlActivity.DEBUG_TAG, "up action failed");
                                                        Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
                                                }

                                                //Toast.makeText(SysLvlMainActivity.this, "Move span from "+row+" to "+(row - 1), Toast.LENGTH_SHORT).show();
                                                mQuickAction.dismiss();
                                        }
                                });

                                downAction.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                Log.v(SysLvlActivity.DEBUG_TAG, "Down button press for span "+row);

                                                if (spanRepository == null) {
                                                        return;
                                                }
                                                try {
                                                spanRepository.moveSpanDown(row);
                                                spanRepository.updateAllSpanAttenuation();
                                                }catch(SQLException e){
                                                        Log.e(SysLvlActivity.DEBUG_TAG, "down action failed");
                                                        Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
                                                }

                                                //Toast.makeText(SysLvlMainActivity.this, "Move span from "+(row)+" to "+(row + 1), Toast.LENGTH_SHORT).show();
                                                mQuickAction.dismiss();
                                        }
                                });

                                deleteAction.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                Log.v(SysLvlActivity.DEBUG_TAG, "Delete button press for span "+row);
                                                //Toast.makeText(SysLvlMainActivity.this, "Delete span: "+row, Toast.LENGTH_SHORT).show();
                                    try{

                                                if (spanRepository != null) {
                                                        spanRepository.deleteSpan(span_id);
                                                        spanRepository.updateSpanPositions();
                                                        spanRepository.updateAllSpanAttenuation();
                                                }
                                        }catch(SQLException e){
                                                Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
                                        }

                                                mQuickAction.dismiss();
                                        }
                                });

                                mQuickAction.addActionItem(detailsAction);
                                mQuickAction.addActionItem(upAction);
                                mQuickAction.addActionItem(downAction);
                                mQuickAction.addActionItem(deleteAction);

                                mQuickAction.setAnimStyle(QuickAction.ANIM_AUTO);

                                mQuickAction.setOnDismissListener(new OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                                mMoreImage.setImageResource(R.drawable.ic_list_more);
                                        }
                                });

                                mQuickAction.show();
                        }
                });
    }

    private long getSpanIdAtPosition(long position) {
        if (spanRepository == null) {
                return -1L;
        }
        for (Span span : spans) {
                if (span.getPosition() == position) {
                        return span.getId();
                }
        }
        return -1L;
    }

    private void rebuildSpanSummaries() {
        spanSummaries.clear();
        for (Span span : spans) {
                spanSummaries.add(formatSpan(span));
        }
        if (adapter != null) {
                adapter.setData(spanSummaries.toArray(new String[spanSummaries.size()]));
                adapter.notifyDataSetChanged();
        }
    }

    private String formatSpan(Span span) {
        return span.getDistance() + "ft of " + span.getCableName() + " to " + span.getDeviceName() +
                "\n Tap: " + span.getTapHigh() + "/" + span.getTapLow() + " | Out: " + span.getHotHigh() + "/" + span.getHotLow();
    }

    @Override
    protected void onDestroy() {
        if (spanRepository != null) {
                spanRepository.removeObserver(spanObserver);
                spanRepository.close();
        }
        super.onDestroy();
    }
}
