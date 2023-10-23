package com.dd.app.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.dd.app.util.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class BebuWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        UiUtils.log("Remote Widget","Data: "+ Arrays.toString(intent.getStringArrayListExtra("array").toArray()));
        return new WidgetDataProvider(this,intent);
    }
}