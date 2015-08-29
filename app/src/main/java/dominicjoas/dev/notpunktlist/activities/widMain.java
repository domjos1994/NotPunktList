package dominicjoas.dev.notpunktlist.activities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.clsMarkList;

/**
 * Implementation of App Widget functionality.
 */
public class widMain extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            updateAppWidget(context,appWidgetManager, appWidgetIds[i]);
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
        SharedPreferences pref =  ctx.getSharedPreferences("NOTPUNKTLIST",Context.MODE_PRIVATE);
        double maxPoints = Double.parseDouble(pref.getString(String.valueOf(R.string.maximumPoints), "20"));
        boolean quarterMarks = pref.getBoolean(String.valueOf(R.string.quarterMarks), false);
        boolean halfPoints = pref.getBoolean(String.valueOf(R.string.halfPoints), false);
        boolean dictatMode = pref.getBoolean(String.valueOf(R.string.dictatMode), false);
        clsMarkList list = new clsMarkList(maxPoints, !halfPoints, !quarterMarks);
        List<String> ls = list.generateList();
        String[] text = new String[2];
        if(dictatMode) {
            text[0] = "Fehler              Note\n";
        } else {
            text[0] = "Punkte              Note\n";
        }
        text[1] = "";
        for(String item : ls) {
            String before = item.split(";")[0];
            String after = item.split(";")[1];
            if(dictatMode) {
                before = String.valueOf(maxPoints - Double.parseDouble(before));
            }
            if(dictatMode) {
                text[1] += "Fehler: " + before + " | Note:" + after + " ||";
            } else {
                text[1] += "Punkte: " + before + " | Note:" + after + " ||";
            }
        }
        return text;
    }
}

