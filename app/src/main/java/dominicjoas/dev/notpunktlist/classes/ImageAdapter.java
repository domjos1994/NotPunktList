package dominicjoas.dev.notpunktlist.classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import dominicjoas.dev.notpunktlist.R;

/**
 * Created by domjo_000 on 29.08.2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public int getCount() {
        return mThumbIds.length;
    }
    public Object getItem(int position) {
        return mThumbIds[position];
    }
    public long getItemId(int position) {
        return 0;
    }
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else{
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    private Integer[] mThumbIds = {
            R.drawable.cold_bg_texture_01,
            R.drawable.cold_bg_texture_02,
            R.drawable.cold_bg_texture_03,
            R.drawable.cold_bg_texture_04,
            R.drawable.cold_bg_texture_05,
            R.drawable.cold_bg_texture_06,
            R.drawable.cold_bg_texture_07,
            R.drawable.medium_bg_texture_01,
            R.drawable.medium_bg_texture_02,
            R.drawable.medium_bg_texture_03,
            R.drawable.medium_bg_texture_04,
            R.drawable.medium_bg_texture_05,
            R.drawable.medium_bg_texture_06,
            R.drawable.medium_bg_texture_07,
            R.drawable.dark_bg_texture_01,
            R.drawable.dark_bg_texture_02,
            R.drawable.dark_bg_texture_03,
            R.drawable.dark_bg_texture_04,
            R.drawable.dark_bg_texture_05,
            R.drawable.dark_bg_texture_06,
            R.drawable.dark_bg_texture_07,
            R.drawable.light_bg_texture_01,
            R.drawable.light_bg_texture_02,
            R.drawable.light_bg_texture_03,
            R.drawable.light_bg_texture_04,
            R.drawable.light_bg_texture_05,
            R.drawable.light_bg_texture_06,
            R.drawable.light_bg_texture_07,
            R.drawable.warm_bg_texture_01,
            R.drawable.warm_bg_texture_02,
            R.drawable.warm_bg_texture_03,
            R.drawable.warm_bg_texture_04,
            R.drawable.warm_bg_texture_05,
            R.drawable.warm_bg_texture_06,
            R.drawable.warm_bg_texture_07
    };
}

