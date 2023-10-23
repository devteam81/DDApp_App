package com.dd.app.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dd.app.R;
import com.dd.app.util.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor cursor;
    private ArrayList<String> widgetData = new ArrayList<>();
    private Intent intent;

    //For obtaining the activity's context and intent
    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    private void initCursor(){
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/
        /*cursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},null);*/
        UiUtils.log("Adapter Widget","Data: "+ Arrays.toString(intent.getStringArrayListExtra("array").toArray()));
        widgetData = intent.getStringArrayListExtra("array");
        /*widgetData.add("Harshit");
        widgetData.add("Bheda");
        widgetData.add("Harshit");
        widgetData.add("Bheda");
        widgetData.add("Harshit");
        widgetData.add("Bheda");*/

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        initCursor();
        if (cursor != null) {
            cursor.moveToFirst();
        }
    }

    @Override
    public void onDataSetChanged() {
        /** Listen for data changes and initialize the cursor again **/
        initCursor();
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        //return cursor.getCount();
        return widgetData.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        /** Populate your widget's single list item **/
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        //cursor.moveToPosition(i);
        remoteViews.setTextViewText(R.id.stock_symbol, widgetData.get(i));//cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL)));
        //remoteViews.setTextViewText(R.id.bid_price,cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
        //remoteViews.setTextViewText(R.id.change,cursor.getString(cursor.getColumnIndex(QuoteColumns.CHANGE)));
        /*if (cursor.getString(cursor.getColumnIndex(QuoteColumns.ISUP)).equals("1")) {
            remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
        } else {
            remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
        }*/
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}