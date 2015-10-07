package dominicjoas.dev.notpunktlist.activities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.clsMarkList;
import dominicjoas.dev.notpunktlist.classes.clsSharedPreference;

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
        clsSharedPreference pref =  new clsSharedPreference(ctx);
        clsMarkList list = new clsMarkList(Integer.parseInt(pref.getMaxPoints()));
        Map<Double, Double> ls = list.generateList(clsMarkList.Sorting.bestMarkFirst, clsMarkList.Mode.linear);
        String[] text = new String[2];
        if(pref.getDictMode()) {
            text[0] = ctx.getString(R.string.mistakes) + "              " + ctx.getString(R.string.mark) + "\n";
        } else {
            text[0] = ctx.getString(R.string.points) + "              " + ctx.getString(R.string.mark) + "\n";
        }
        text[1] = "";
        for(double key : ls.keySet()) {
            String before = String.valueOf(key);
            String after = String.valueOf(ls.get(key));
            if(pref.getDictMode()) {
                before = String.valueOf(Double.parseDouble(pref.getMaxPoints()) - Double.parseDouble(before));
            }
            if(pref.getDictMode()) {
                text[1] += ctx.getString(R.string.mistakes) + ": " + before + " | " + ctx.getString(R.string.mark) + ":" + after + " ||";
            } else {
                text[1] += ctx.getString(R.string.points) + ": " + before + " | " + ctx.getString(R.string.mark) + ":" + after + " ||";
            }
        }
        return text;
    }
}

