package dominicjoas.dev.notpunktlist.classes;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dominicjoas.dev.notpunktlist.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CallsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<String> mCallsList;
    private Context mContext = null;

    public CallsListRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mCallsList.size();
    }

    @Override
    public long getItemId(int position) {
        return mCallsList.indexOf(mCallsList.get(position));
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.wid_detailed);
        String callItem = mCallsList.get(position);
        row.setTextViewText(R.id.lblHeader, callItem);
        return row;
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onCreate() {
        clsSharedPreference pref =  new clsSharedPreference(mContext);
        clsMarkList list = new clsMarkList(Double.parseDouble(pref.getMaxPoints()), !pref.getHalfPoints(), !pref.getQuarterMarks());
        mCallsList = new ArrayList<>();
        for(String item : list.generateList()) {
            String[] arrItem = item.split(";");
            String strItem = arrItem[0];
            for(int i = strItem.length(); i<=20; i++) {
                strItem += " ";
            }
            strItem += arrItem[1];
            mCallsList.add(strItem);
        }
        Collections.reverse(mCallsList);
    }

    @Override
    public void onDataSetChanged() {
        clsSharedPreference pref =  new clsSharedPreference(mContext);
        clsMarkList list = new clsMarkList(Double.parseDouble(pref.getMaxPoints()), !pref.getHalfPoints(), !pref.getQuarterMarks());
        mCallsList = new ArrayList<>();
        for(String item : list.generateList()) {
            String[] arrItem = item.split(";");
            String strItem = arrItem[0];
            for(int i = strItem.length(); i<=20; i++) {
                strItem += " ";
            }
            if(pref.getDictMode()) {
                arrItem[1] = String.valueOf(Double.parseDouble(pref.getMaxPoints()) - Double.parseDouble(arrItem[1]));
            }
            strItem += arrItem[1];
            mCallsList.add(strItem);
        }
        Collections.reverse(mCallsList);
    }

    @Override
    public void onDestroy() {
    }
}
