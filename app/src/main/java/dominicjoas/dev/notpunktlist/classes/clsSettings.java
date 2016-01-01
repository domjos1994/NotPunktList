package dominicjoas.dev.notpunktlist.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import dominicjoas.dev.notpunktlist.R;

/**
 * Created by NB-DJ on 01.01.2016.
 */
public class clsSettings {
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Context ctx;

    public clsSettings(Context ctx) {
        this.ctx = ctx;
        this.preference = this.ctx.getSharedPreferences(ctx.getString(R.string.sysSharedPref), Context.MODE_PRIVATE);
        if(this.editor == null) {
            this.editor = this.preference.edit();
        }
    }

    public void setMaxPoints(String maxPoints) {
        editor.putString(this.ctx.getString(R.string.prefMaxPoints), maxPoints);
        editor.commit();
    }

    public String getMaxPoints() {
        return preference.getString(this.ctx.getString(R.string.prefMaxPoints), "20");
    }

    public void setMarkMultiplier(float multi) {
        editor.putFloat(this.ctx.getString(R.string.prefMarkMultiplier), multi);
        editor.commit();
    }

    public float getMarkMultiplier() {
        return preference.getFloat(this.ctx.getString(R.string.prefMarkMultiplier), 0.1f);
    }

    public void setPointsMultiplier(float multi) {
        editor.putFloat(this.ctx.getString(R.string.prefPointsMultiplier), multi);
        editor.commit();
    }

    public float getPointsMultiplier() {
        return preference.getFloat(this.ctx.getString(R.string.prefPointsMultiplier), 0.5f);
    }

    public void setBestMarkAt(float bestMark) {
        editor.putFloat(this.ctx.getString(R.string.prefBestMarkAt), bestMark);
        editor.commit();
    }

    public float getBestMarkAt() {
        return preference.getFloat(this.ctx.getString(R.string.prefBestMarkAt), 20.0f);
    }

    public void setWorstMarkTo(float worstMark) {
        editor.putFloat(this.ctx.getString(R.string.prefWorstMarkTo), worstMark);
        editor.commit();
    }

    public float getWorstMarkTo() {
        return preference.getFloat(this.ctx.getString(R.string.prefWorstMarkTo), 0.0f);
    }

    public void setCustomMark(float mark) {
        editor.putFloat(this.ctx.getString(R.string.prefCustomMark), mark);
        editor.commit();
    }

    public float getCustomMark() {
        return preference.getFloat(this.ctx.getString(R.string.prefCustomMark), 3.5f);
    }

    public void setCustomPoints(float Points) {
        editor.putFloat(this.ctx.getString(R.string.prefCustomPoints), Points);
        editor.commit();
    }

    public float getCustomPoints() {
        return preference.getFloat(this.ctx.getString(R.string.prefCustomPoints), 10.0f);
    }

    public void setMode(int mode) {
        editor.putInt(this.ctx.getString(R.string.prefMode), mode);
        editor.commit();
    }

    public int getMode() {
        return preference.getInt(this.ctx.getString(R.string.prefMode), 0);
    }

    public void setMarkPoints(boolean markPoints) {
        editor.putBoolean(this.ctx.getString(R.string.prefMarkPoints), markPoints);
        editor.commit();
    }

    public boolean getMarkPoints() {
        return preference.getBoolean(this.ctx.getString(R.string.prefMarkPoints), false);
    }

    public void setView(int view) {
        editor.putInt(this.ctx.getString(R.string.prefView), view);
        editor.commit();
    }

    public int getView() {
        return preference.getInt(this.ctx.getString(R.string.prefView), 0);
    }

    public void setDictMode(boolean dictMode) {
        editor.putBoolean(this.ctx.getString(R.string.prefDictMode), dictMode);
        editor.commit();
    }

    public boolean getDictMode() {
        return preference.getBoolean(this.ctx.getString(R.string.prefDictMode), false);
    }

    public void setMarkSign(boolean sign) {
        editor.putBoolean(this.ctx.getString(R.string.prefMarkSigns), sign);
        editor.commit();
    }

    public boolean getMarkSign() {
        return preference.getBoolean(this.ctx.getString(R.string.prefMarkSigns), false);
    }

    public void setPath(String path) {
        editor.putString(this.ctx.getString(R.string.prefPath), path);
        this.editor.commit();
    }

    public String getPath() {
        return preference.getString(this.ctx.getString(R.string.prefPath), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
    }

    public void setBackground(int drawable) {
        editor.putInt(this.ctx.getString(R.string.prefBackground), drawable);
        this.editor.commit();
    }

    public int getBackground() {
        return preference.getInt(this.ctx.getString(R.string.prefBackground), R.drawable.light_bg_texture_04);
    }

    public void setCTRLCenter(int drawable) {
        editor.putInt(this.ctx.getString(R.string.prefCTRLCenter), drawable);
        this.editor.commit();
    }

    public int getCTRLCenter() {
        return preference.getInt(this.ctx.getString(R.string.prefCTRLCenter), R.drawable.dark_bg_texture_04);
    }

    public void setHeader(int drawable) {
        editor.putInt(this.ctx.getString(R.string.prefHeader), drawable);
        this.editor.commit();
    }

    public int getHeader() {
        return preference.getInt(this.ctx.getString(R.string.prefHeader), R.drawable.medium_bg_texture_04);
    }

    public int getInt(String key) {
        return preference.getInt(key, 0);
    }
}
