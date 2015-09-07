package dominicjoas.dev.notpunktlist.classes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RemoteViewsFactory listProvidder = new CallsListRemoteViewsFactory(this.getApplicationContext(), intent);
        return listProvidder;
    }
}
