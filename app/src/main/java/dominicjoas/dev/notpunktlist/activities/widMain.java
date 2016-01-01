package dominicjoas.dev.notpunktlist.activities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.Map;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsMarkPointList;

/**
 * Implementation of App Widget functionality.
 */
public class widMain extends AppWidgetProvider {

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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        String[] erg = getList(context);
        CharSequence text = String.valueOf(erg[1]);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wid_main);
        remoteViews.setTextViewText(R.id.txtHeader, erg[0]);
        remoteViews.setTextViewText(R.id.txtContent, text);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static String[] getList(Context ctx) {
        clsMarkPointList mpl =  clsHelper.prepareList(ctx);
        Map<Float, Float> list = clsHelper.prepareList(ctx).generateMarkPointList();
        String[] text = new String[2];
        if(mpl.isInDictatMode()) {
            text[0] = ctx.getString(R.string.mistakes) + "              " + ctx.getString(R.string.mark) + "\n";
        } else {
            text[0] = ctx.getString(R.string.points) + "              " + ctx.getString(R.string.mark) + "\n";
        }
        text[1] = "";
        for(Map.Entry<Float, Float> entry : list.entrySet()) {
            String before = entry.getKey().toString();
            String after = entry.getValue().toString();
            if(mpl.isInDictatMode()) {
                before = String.valueOf(Double.parseDouble(String.valueOf(mpl.getMaxPoints())) - Double.parseDouble(before));
            }
            if(mpl.isInDictatMode()) {
                text[1] += ctx.getString(R.string.mistakes) + ": " + before + " | " + ctx.getString(R.string.mark) + ":" + after + " ||";
            } else {
                text[1] += ctx.getString(R.string.points) + ": " + before + " | " + ctx.getString(R.string.mark) + ":" + after + " ||";
            }
        }
        return text;
    }
}

