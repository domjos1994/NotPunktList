package dominicjoas.dev.notpunktlist.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.ImageAdapter;
import dominicjoas.dev.notpunktlist.classes.clsHelper;

public class actSettings extends AppCompatActivity {

    TextView txtPath;
    GridView grdBackgrounds;
    Spinner lvBoxes;
    ImageButton cmdBackground;
    Button cmdSave, cmdBack, cmdSavePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actsettings);

        txtPath = (TextView) findViewById(R.id.txtPath);
        grdBackgrounds = (GridView) findViewById(R.id.grdBackgrounds);
        lvBoxes = (Spinner) findViewById(R.id.lvBoxes);

        cmdBackground = (ImageButton) findViewById(R.id.cmdBackgrounds);
        cmdSave = (Button) findViewById(R.id.cmdSave);
        cmdBack = (Button) findViewById(R.id.cmdBack);
        cmdSavePath = (Button) findViewById(R.id.cmdSavePath);

        clsHelper.fillSpinner(this, lvBoxes, Arrays.asList("Kopfbereich", "Hintergrund", "Kontrollzentrum"));
        grdBackgrounds.setAdapter(new ImageAdapter(this));
        final SharedPreferences sharedPref = getBaseContext().getSharedPreferences("NOTPUNKTLIST", Context.MODE_PRIVATE);
        txtPath.setText(sharedPref.getString("Pfad", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()));
        updateBackgrounds();

        grdBackgrounds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageAdapter adapt = (ImageAdapter) grdBackgrounds.getAdapter();
                cmdBackground.setImageResource((int) adapt.getItem(i));
                cmdBackground.setTag(adapt.getItem(i));
            }
        });

        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(lvBoxes.getSelectedItem().toString(), (int) cmdBackground.getTag());
                editor.commit();
                clsHelper.createToast(getApplicationContext(), lvBoxes.getSelectedItem().toString() + " gespeichert!");
            }
        });

        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0);
                finish();
            }
        });

        cmdSavePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new File(txtPath.getText().toString()).exists()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Pfad", txtPath.getText().toString());
                    editor.commit();
                    clsHelper.createToast(getApplicationContext(), "Pfad gespeichert!");
                } else {
                    clsHelper.createToast(getApplicationContext(), "Pfad existiert nicht!");
                }
            }
        });

        lvBoxes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateBackgrounds();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mensettings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateBackgrounds() {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("NOTPUNKTLIST",Context.MODE_PRIVATE);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cmdBackground.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, sharedPref.getInt(lvBoxes.getSelectedItem().toString(), R.drawable.light_bg_texture_01)));
        } else {
            cmdBackground.setBackground(clsHelper.getBitmapDrawable(this, sharedPref.getInt(lvBoxes.getSelectedItem().toString(), R.drawable.light_bg_texture_01)));
        }
    }
}
