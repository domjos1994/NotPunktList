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

    public void setHalfPoints(boolean halfpoints) {
        implementEditor();
        editor.putBoolean(this.ctx.getString(R.string.prefHalfPoints), halfpoints);
    }

    public boolean getHalfPoints() {
        return preference.getBoolean(this.ctx.getString(R.string.prefHalfPoints), false);
    }

    public void setQuarterMarks(boolean quarterMarks) {
        implementEditor();
        editor.putBoolean(this.ctx.getString(R.string.prefQuarterMarks), quarterMarks);
    }

    public boolean getQuarterMarks() {
        return preference.getBoolean(this.ctx.getString(R.string.prefQuarterMarks), false);
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
