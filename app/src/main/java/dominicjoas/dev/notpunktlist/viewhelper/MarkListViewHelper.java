package dominicjoas.dev.notpunktlist.viewhelper;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dominicjoas.dev.notpunktlist.R;

public class MarkListViewHelper extends dominicjoas.dev.notpunktlist.classes.clsMarkList implements Runnable {
    private GridView grd, header;
    private ArrayAdapter<Double> adapter;
    private ArrayAdapter<String> headeradapter;
    private ArrayAdapter<String> namedMarksAdapter;
    private List<Double> ls = new ArrayList<>();
    private List<String> lsheader = new ArrayList<>();
    private List<String> lsNamed = new ArrayList<>();
    private Context ctx;
    private Sorting srt;
    private Mode md;
    private Activity act;

    public MarkListViewHelper(Activity act, int maxPoints, GridView grd, Context ctx) {
        super(maxPoints);
        this.grd = grd;
        this.ctx = ctx;
        adapter = new ArrayAdapter<>(ctx,android.R.layout.simple_list_item_1, ls);
        this.act = act;
    }

    public MarkListViewHelper(Activity act, int maxPoints, GridView grd, GridView header, Context ctx) {
        super(maxPoints);
        this.grd = grd;
        this.ctx = ctx;
        this.header = header;
        adapter = new ArrayAdapter<>(ctx,android.R.layout.simple_list_item_1, ls);
        this.act = act;
    }

    public void update(Sorting srt, Mode md) {
        this.srt = srt;
        this.md = md;
    }

    @Override
    public void run() {
        if(header!=null) {
            headeradapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, lsheader);
            if(srt==Sorting.bestMarkFirst || srt==Sorting.worstMarkFirst) {
                headeradapter.add(ctx.getString(R.string.mark));
                if(super.isDictatModeEnabled()) {
                    headeradapter.add(ctx.getString(R.string.mistakes));
                } else {
                    headeradapter.add(ctx.getString(R.string.points));
                }
            } else {
                if(super.isDictatModeEnabled()) {
                    headeradapter.add(ctx.getString(R.string.mistakes));
                } else {
                    headeradapter.add(ctx.getString(R.string.points));
                }
                headeradapter.add(ctx.getString(R.string.mark));
            }
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    header.setAdapter(headeradapter);
                }
            });
        }

        if(this.boolNamedMarksIsEnabled) {
            namedMarksAdapter = new ArrayAdapter<>(this.ctx, android.R.layout.simple_list_item_1, lsNamed);
            namedMarksAdapter.clear();
            Map<String, String> list = super.generateList(srt, md);
            for(String key : list.keySet()) {
                namedMarksAdapter.add(key);
                namedMarksAdapter.add(list.get(key));
            }
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    grd.setAdapter(namedMarksAdapter);
                }
            });
        } else {
            adapter.clear();
            Map<Double, Double> list = super.generateList(srt, md);
            for(double key : list.keySet()) {
                adapter.add(key);
                adapter.add(list.get(key));
            }
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    grd.setAdapter(adapter);
                }
            });
        }
    }
}
