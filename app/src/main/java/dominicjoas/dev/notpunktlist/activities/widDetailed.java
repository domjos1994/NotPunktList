package dominicjoas.dev.notpunktlist.activities;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.WidgetService;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsMarkPointList;

public class widDetailed extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        String[] erg = getList(context);
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wid_detailed);
        remoteViews.setTextViewText(R.id.lblHeader, erg[0]);
        remoteViews.setRemoteAdapter(R.id.lvMarkList, serviceIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static String[] getList(Context ctx) {
        clsMarkPointList mpl =  clsHelper.prepareList(ctx);
        String[] text = new String[2];
        if(mpl.isInDictatMode()) {
            text[0] = ctx.getString(R.string.mistakes) + "            " + ctx.getString(R.string.mark);
        } else {
            text[0] = ctx.getString(R.string.points) + "            " + ctx.getString(R.string.mark);
        }
        return text;
    }
}