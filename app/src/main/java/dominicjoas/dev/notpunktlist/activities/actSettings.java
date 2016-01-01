package dominicjoas.dev.notpunktlist.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.ImageAdapter;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsSettings;
import dominicjoas.dev.notpunktlist.classes.clsSharedPreference;

public class actSettings extends AppCompatActivity {

    TextView txtPath;
    GridView grdBackgrounds;
    Spinner lvBoxes;
    ImageButton cmdBackground;
    Button cmdSave, cmdBack, cmdSavePath;
    clsSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actsettings);

        settings = new clsSettings(getApplicationContext());
        txtPath = (TextView) findViewById(R.id.txtPath);
        grdBackgrounds = (GridView) findViewById(R.id.grdBackgrounds);
        lvBoxes = (Spinner) findViewById(R.id.lvBoxes);

        cmdBackground = (ImageButton) findViewById(R.id.cmdBackgrounds);
        cmdSave = (Button) findViewById(R.id.cmdSave);
        cmdBack = (Button) findViewById(R.id.cmdBack);
        cmdSavePath = (Button) findViewById(R.id.cmdSavePath);

        clsHelper.fillSpinner(this, lvBoxes, Arrays.asList(getString(R.string.prefHeader), getString(R.string.prefBackground), getString(R.string.prefCTRLCenter)));
        grdBackgrounds.setAdapter(new ImageAdapter(this));
        txtPath.setText(settings.getPath());
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
                try {
                    settings.setBackground((int) cmdBackground.getTag());
                    clsHelper.createToast(getApplicationContext(), getString(R.string.infoOptionsSaved));
                }catch(Exception ex) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorNoBackgroundChosen));
                }
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
                    settings.setPath(txtPath.getText().toString());
                    clsHelper.createToast(getApplicationContext(), getString(R.string.infoOptionsSaved));
                } else {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorPathDontExist));
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
        int id = item.getItemId();
        if (id == R.id.menBack) {
            setResult(0);
            finish();
            return true;
        }
        if(id==R.id.menClose) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateBackgrounds() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        cmdBackground.setImageBitmap(null);
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cmdBackground.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, settings.getInt(lvBoxes.getSelectedItem().toString())));
        } else {
            dangerousBG();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dangerousBG() {
        cmdBackground.setBackground(clsHelper.getBitmapDrawable(this, settings.getInt(lvBoxes.getSelectedItem().toString())));
    }
}
