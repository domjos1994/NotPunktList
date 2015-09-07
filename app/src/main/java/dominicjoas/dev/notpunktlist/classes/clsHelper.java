package dominicjoas.dev.notpunktlist.classes;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.List;

import dominicjoas.dev.notpunktlist.R;

/**
 * Created by domjo_000 on 29.08.2015.
 */
public class clsHelper {

    public static void fillList(Context context, ListView lv, List<String> ls) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1, ls);
        lv.setAdapter(adapter);
    }

    public static void fillSpinner(Context context, Spinner lv, List<String> ls) {
        SpinnerAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item, ls);
        lv.setAdapter(adapter);
    }

    public static void fillGrid(Context context, GridView lv, List<Drawable> ls) {
        ArrayAdapter<Drawable> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1, ls);
        lv.setAdapter(adapter);
    }

    public static Drawable getBitmapDrawable(Context ctx, int value) {
        Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), value);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(ctx.getResources(), bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return bitmapDrawable;
    }

    public static void createToast(Context ctx, String value) {
        Toast toast = Toast.makeText(ctx, value, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void createInfoDialog(Context ctx, String title, String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.actMainSearchAfter) + " " + title);
        builder.setIcon(R.drawable.search);
        builder.setMessage(value);
        builder.create().show();
    }
}
