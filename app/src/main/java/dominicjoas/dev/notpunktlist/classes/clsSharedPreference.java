package dominicjoas.dev.notpunktlist.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import dominicjoas.dev.notpunktlist.R;

public class clsSharedPreference {
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Context ctx;
    public String PREFMAXPOINTS, PREFPOINTMODE, PREFMARKMODE, PREFVIEWMODE, PREFMARKLISTMODE;
    public String PREFBESTAT, PREFWORSTAT, PREFCUSTOMPOINT, PREFCUSTOMMARK, PREFDICTMODE;
    public String PREFBACKGROUND, PREFCONTROLCENTER, PREFHEADER, PREFPATH;

    public clsSharedPreference(Context ctx) {
        this.ctx = ctx;
        this.preference = this.ctx.getSharedPreferences(ctx.getString(R.string.sysSharedPref), Context.MODE_PRIVATE);

        this.PREFMAXPOINTS = ctx.getString(R.string.prefMaxPoints);
        this.PREFMARKLISTMODE = ctx.getString(R.string.prefMarkListMode);
        this.PREFPOINTMODE = ctx.getString(R.string.prefPointMode);
        this.PREFMARKMODE = ctx.getString(R.string.prefMarkMode);
        this.PREFVIEWMODE = ctx.getString(R.string.prefViewMode);

        this.PREFBESTAT = ctx.getString(R.string.prefBestAt);
        this.PREFWORSTAT = ctx.getString(R.string.prefWorstAt);
        this.PREFCUSTOMPOINT = ctx.getString(R.string.prefCustomPoint);
        this.PREFCUSTOMMARK = ctx.getString(R.string.prefCustomMark);
        this.PREFDICTMODE = ctx.getString(R.string.prefDictMode);

        this.PREFBACKGROUND = ctx.getString(R.string.prefBackground);
        this.PREFCONTROLCENTER = ctx.getString(R.string.prefCTRLCenter);
        this.PREFHEADER = ctx.getString(R.string.prefHeader);
        this.PREFPATH = ctx.getString(R.string.prefPath);
    }

    public void setMaxPoints(int maxPoints) {
        implementEditor();
        editor.putInt(this.PREFMAXPOINTS, maxPoints);
    }

    public int getMaxPoints() {
        return preference.getInt(this.PREFMAXPOINTS, 20);
    }

    public void setPointMode(long index) {
        implementEditor();
        editor.putLong(this.PREFPOINTMODE, index);
    }

    public long getPointMode() {
        return preference.getLong(this.PREFPOINTMODE, 0);
    }

    public void setMarkMode(long index) {
        implementEditor();
        editor.putLong(this.PREFMARKMODE, index);
    }

    public long getMarkMode() {
        return preference.getLong(this.PREFMARKMODE, 0);
    }

    public void setViewMode(long index) {
        implementEditor();
        editor.putLong(this.PREFVIEWMODE, index);
    }

    public long getViewMode() {
        return preference.getLong(this.PREFVIEWMODE, 0);
    }

    public void setMarkListMode(long index) {
        implementEditor();
        editor.putLong(this.PREFMARKLISTMODE, index);
    }

    public long getMarkListMode() {
        return preference.getLong(this.PREFMARKLISTMODE, 0);
    }

    public void setBestMarkAt(int bestMark) {
        implementEditor();
        editor.putInt(this.PREFBESTAT, bestMark);
    }

    public int getBestMarkAt() {
        return preference.getInt(this.PREFBESTAT, 20);
    }

    public void setWorstMarkAt(int worstMark) {
        implementEditor();
        editor.putInt(this.PREFWORSTAT, worstMark);
    }

    public int getWorstMarkAt() {
        return preference.getInt(this.PREFWORSTAT, 0);
    }

    public void setCustomPoint(float points) {
        implementEditor();
        editor.putFloat(this.PREFCUSTOMPOINT, points);
    }

    public float getCustomPoint() {
        return preference.getFloat(this.PREFCUSTOMPOINT, 10.0F);
    }

    public void setCustomMark(float mark) {
        implementEditor();
        editor.putFloat(this.PREFCUSTOMMARK, mark);
    }

    public float getCustomMark() {
        return preference.getFloat(this.PREFCUSTOMMARK, 3.5F);
    }

    public void setDictMode(boolean dictMode) {
        implementEditor();
        editor.putBoolean(this.PREFDICTMODE, dictMode);
    }

    public boolean getDictMode() {
        return preference.getBoolean(this.PREFDICTMODE, false);
    }

    public void setBackground(int drawable) {
        implementEditor();
        editor.putInt(this.PREFBACKGROUND, drawable);
    }

    public int getBackground() {
        return preference.getInt(this.PREFBACKGROUND, R.drawable.light_bg_texture_04);
    }

    public void setCTRLCenter(int drawable) {
        implementEditor();
        editor.putInt(this.PREFCONTROLCENTER, drawable);
    }

    public int getCTRLCenter() {
        return preference.getInt(this.PREFCONTROLCENTER, R.drawable.dark_bg_texture_04);
    }

    public void setHeader(int drawable) {
        implementEditor();
        editor.putInt(this.PREFHEADER, drawable);
    }

    public int getHeader() {
        return preference.getInt(this.PREFHEADER, R.drawable.medium_bg_texture_04);
    }

    public void setPath(String path) {
        implementEditor();
        editor.putString(this.PREFPATH, path);
    }

    public String getPath() {
        return preference.getString(this.PREFPATH, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
    }

    public void setString(String key, String value) {
        implementEditor();
        editor.putString(key, value);
    }

    public void setInt(String key, int value) {
        implementEditor();
        editor.putInt(key, value);
    }

    public String getString(String key) {
        return preference.getString(key, "");
    }

    public int getInt(String key) {
        return preference.getInt(key, 0);
    }

    public void save() {
        this.editor.commit();
    }

    private void implementEditor() {
        if(this.editor == null) {
            this.editor = this.preference.edit();
        }
    }
}
