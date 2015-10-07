package dominicjoas.dev.notpunktlist.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import dominicjoas.dev.notpunktlist.R;

/**
 * Created by Dominic Joas on 01.09.2015.
 */
public class clsSharedPreference {
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Context ctx;

    public clsSharedPreference(Context ctx) {
        this.ctx = ctx;
        this.preference = this.ctx.getSharedPreferences(ctx.getString(R.string.sysSharedPref), Context.MODE_PRIVATE);
    }

    public void setMaxPoints(String maxPoints) {
        implementEditor();
        editor.putString(this.ctx.getString(R.string.prefMaxPoints), maxPoints);
    }

    public String getMaxPoints() {
        return preference.getString(this.ctx.getString(R.string.prefMaxPoints), "20");
    }

    public void setPoints(long index) {
        implementEditor();
        editor.putLong(this.ctx.getString(R.string.points), index);
    }

    public long getPoints() {
        return preference.getLong(this.ctx.getString(R.string.points), 0);
    }

    public void setMarks(long index) {
        implementEditor();
        editor.putLong(this.ctx.getString(R.string.mark), index);
    }

    public long getMarks() {
        return preference.getLong(this.ctx.getString(R.string.mark), 0);
    }

    public void setViews(long index) {
        implementEditor();
        editor.putLong(this.ctx.getString(R.string.view), index);
    }

    public long getViews() {
        return preference.getLong(this.ctx.getString(R.string.view), 0);
    }

    public void setMode(long index) {
        implementEditor();
        editor.putLong(this.ctx.getString(R.string.mode), index);
    }

    public long getMode() {
        return preference.getLong(this.ctx.getString(R.string.mode), 0);
    }

    public void setBestMarkAt(float bestMark) {
        implementEditor();
        editor.putFloat(this.ctx.getString(R.string.bestAt), bestMark);
    }

    public float getBestMarkAt() {
        return preference.getFloat(this.ctx.getString(R.string.bestAt), Float.parseFloat(preference.getString(this.ctx.getString(R.string.prefMaxPoints), "20")));
    }

    public void setWorstMarkAt(float worstMark) {
        implementEditor();
        editor.putFloat(this.ctx.getString(R.string.worstAt), worstMark);
    }

    public float getWorstMarkAt() {
        return preference.getFloat(this.ctx.getString(R.string.worstAt), 0.0F);
    }

    public void setUserPoint(float points) {
        implementEditor();
        editor.putFloat(this.ctx.getString(R.string.userpoints), points);
    }

    public float getUserPoint() {
        return preference.getFloat(this.ctx.getString(R.string.userpoints), Float.parseFloat(preference.getString(this.ctx.getString(R.string.prefMaxPoints), "20"))/2);
    }

    public void setUserMark(float mark) {
        implementEditor();
        editor.putFloat(this.ctx.getString(R.string.usermark), mark);
    }

    public float getUserMark() {
        return preference.getFloat(this.ctx.getString(R.string.usermark), 3.5F);
    }

    public void setDictMode(boolean dictMode) {
        implementEditor();
        editor.putBoolean(this.ctx.getString(R.string.prefDictMode), dictMode);
    }

    public boolean getDictMode() {
        return preference.getBoolean(this.ctx.getString(R.string.prefDictMode), false);
    }

    public void setBackground(int drawable) {
        implementEditor();
        editor.putInt(this.ctx.getString(R.string.prefBackground), drawable);
    }

    public int getBackground() {
        return preference.getInt(this.ctx.getString(R.string.prefBackground), R.drawable.light_bg_texture_04);
    }

    public void setCTRLCenter(int drawable) {
        implementEditor();
        editor.putInt(this.ctx.getString(R.string.prefCTRLCenter), drawable);
    }

    public int getCTRLCenter() {
        return preference.getInt(this.ctx.getString(R.string.prefCTRLCenter), R.drawable.dark_bg_texture_04);
    }

    public void setHeader(int drawable) {
        implementEditor();
        editor.putInt(this.ctx.getString(R.string.prefHeader), drawable);
    }

    public int getHeader() {
        return preference.getInt(this.ctx.getString(R.string.prefHeader), R.drawable.medium_bg_texture_04);
    }

    public void setPath(String path) {
        implementEditor();
        editor.putString(this.ctx.getString(R.string.prefPath), path);
    }

    public String getPath() {
        return preference.getString(this.ctx.getString(R.string.prefPath), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
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
