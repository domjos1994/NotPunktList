package dominicjoas.dev.notpunktlist.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsMarkPointList;
import dominicjoas.dev.notpunktlist.classes.clsSettings;
import dominicjoas.dev.notpunktlist.classes.clsSharedPreference;

public class actMainNew extends AppCompatActivity {

    private clsMarkPointList liste;
    private Spinner cmbMode, cmbView, cmbMarkMultiplier, cmbPointsMultiplier;
    private RelativeLayout rlMain;
    private TableLayout tblSettings;
    private TableRow row1, row2, row3;
    private ImageButton cmdExp, cmdSearch;
    private EditText txtMaxPoints, txtSearch, txtBestMarkAt, txtWorstMarkTo, txtCustomPoints, txtCustomMark;
    private CheckBox chkMarkPoints, chkDictatMode;
    private RadioButton optPoints;
    private GridView lvMarkList, lvHeader;
    private TextView lblMaxPoints;
    private SeekBar sbBestMarkAt, sbWorstMarkTo;
    private clsSettings settings;

    int tblHeight;
    ArrayAdapter<String> adapter, adapterHeader = null;
    List<String> ls = new ArrayList<>();
    List<String> lsHeader = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmainnew);

        try {
            this.settings = new clsSettings(getApplicationContext());
            ArrayAdapter<String> modeAdapter, viewAdapter, markMultiplierAdaper, pointsMultiplierAdaper;
            this.setTitle(this.getTitle() + " " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            cmbMode = (Spinner) findViewById(R.id.cmbMode);
            cmbView = (Spinner) findViewById(R.id.cmbView);
            cmbMarkMultiplier = (Spinner) findViewById(R.id.cmbMarkMultiplier);
            cmbPointsMultiplier = (Spinner) findViewById(R.id.cmbPointsMultiplier);
            modeAdapter = new ArrayAdapter<>(this, R.layout.simplespinneritem, getResources().getStringArray(R.array.MODE));
            viewAdapter = new ArrayAdapter<>(this, R.layout.simplespinneritem, getResources().getStringArray(R.array.VIEW));
            markMultiplierAdaper =  new ArrayAdapter<>(this, R.layout.simplespinneritem, getResources().getStringArray(R.array.markMultiplier));
            pointsMultiplierAdaper = new ArrayAdapter<>(this, R.layout.simplespinneritem, getResources().getStringArray(R.array.pointsMultiplier));
            cmbMode.setAdapter(modeAdapter);
            cmbView.setAdapter(viewAdapter);
            cmbMarkMultiplier.setAdapter(markMultiplierAdaper);
            cmbPointsMultiplier.setAdapter(pointsMultiplierAdaper);

            rlMain = (RelativeLayout) findViewById(R.id.rlMain);
            tblSettings = (TableLayout) findViewById(R.id.tblSettings);
            if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                cmdExp = (ImageButton) findViewById(R.id.cmdOpenClose);
            }
            cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            txtMaxPoints = (EditText) findViewById(R.id.txtMaxPoints);
            txtSearch = (EditText) findViewById(R.id.txtSearch);
            chkMarkPoints = (CheckBox) findViewById(R.id.chkMarkPoints);
            chkDictatMode = (CheckBox) findViewById(R.id.chkDictatMode);
            optPoints = (RadioButton) findViewById(R.id.optPoints);
            lvMarkList = (GridView) findViewById(R.id.grdMarkList);
            lvHeader = (GridView) findViewById(R.id.lvHeader);
            lblMaxPoints = (TextView) findViewById(R.id.lblMaxPoints);
            txtBestMarkAt = (EditText) findViewById(R.id.txtBestMarkAt);
            txtWorstMarkTo = (EditText) findViewById(R.id.txtWorstMarkTo);
            txtCustomMark = (EditText) findViewById(R.id.txtCustomMark);
            txtCustomPoints = (EditText) findViewById(R.id.txtCustomPoints);
            sbBestMarkAt = (SeekBar) findViewById(R.id.sbBestMarkAt);
            sbWorstMarkTo = (SeekBar) findViewById(R.id.sbWorstMarkAt);
            row1 = (TableRow) findViewById(R.id.row1);
            row2 = (TableRow) findViewById(R.id.row2);
            row3 = (TableRow) findViewById(R.id.row3);

            lvHeader.setEnabled(false);
            txtMaxPoints.setText(settings.getMaxPoints());
            chkDictatMode.setChecked(settings.getDictMode());
            chkMarkPoints.setChecked(settings.getMarkPoints());
            cmbMode.setSelection(settings.getMode());
            cmbView.setSelection(settings.getView());
            float mark = settings.getMarkMultiplier();
            if(mark == 0.1f) {
                cmbMarkMultiplier.setSelection(0);
            } else if(mark == 0.25f) {
                if(settings.getMarkSign()) {
                    cmbMarkMultiplier.setSelection(4);
                } else {
                    cmbMarkMultiplier.setSelection(1);
                }
            } else if(mark == 0.5f) {
                cmbMarkMultiplier.setSelection(2);
            } else {
                cmbMarkMultiplier.setSelection(3);
            }

            float points = settings.getPointsMultiplier();
            if(points == 0.1f) {
                cmbPointsMultiplier.setSelection(0);
            } else if(points == 0.25f) {
                cmbPointsMultiplier.setSelection(1);
            } else if(points == 0.5f) {
                cmbPointsMultiplier.setSelection(2);
            } else {
                cmbPointsMultiplier.setSelection(3);
            }

            sbBestMarkAt.setProgress((int) settings.getBestMarkAt());
            sbWorstMarkTo.setProgress((int) settings.getWorstMarkTo());
            txtWorstMarkTo.setText(String.valueOf(settings.getWorstMarkTo()));
            txtBestMarkAt.setText(String.valueOf(settings.getBestMarkAt()));
            txtCustomMark.setText(String.valueOf(settings.getCustomMark()));
            txtCustomPoints.setText(String.valueOf(settings.getCustomPoints()));

            txtBestMarkAt.setText(txtMaxPoints.getText());
            sbBestMarkAt.setMax(Integer.parseInt(txtBestMarkAt.getText().toString()));
            sbWorstMarkTo.setMax(Integer.parseInt(txtBestMarkAt.getText().toString()));
            sbBestMarkAt.setProgress(sbBestMarkAt.getMax());
            sbWorstMarkTo.setProgress(0);
            txtBestMarkAt.setEnabled(false);
            txtWorstMarkTo.setEnabled(false);
            txtCustomMark.setText("3.5");
            txtCustomPoints.setText(String.valueOf(Integer.parseInt(txtMaxPoints.getText().toString())/2));
            cmbPointsMultiplier.setSelection(2);


            controlWithCrease();

            updateBackgrounds();
        } catch (Exception ex) {
            clsHelper.createToast(getApplicationContext(), getString(R.string.errorProblemsWithSettings));
        }

        adapter = new ArrayAdapter<>(this, R.layout.marklistitem, ls);
        adapterHeader = new ArrayAdapter<>(this, R.layout.headeritem, lsHeader);
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            tblHeight = tblSettings.getLayoutParams().height;
            tblSettings.getLayoutParams().height = cmdExp.getLayoutParams().height;


            cmdExp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tblSettings.getLayoutParams().height == cmdExp.getLayoutParams().height) {
                        tblSettings.getLayoutParams().height = tblHeight;
                        cmdExp.setImageResource(R.drawable.exp_close);
                    } else {
                        tblSettings.getLayoutParams().height = cmdExp.getLayoutParams().height;
                        cmdExp.setImageResource(R.drawable.exp_open);
                    }
                    tblSettings.requestLayout();
                }
            });
        }
        createList();

        cmbMarkMultiplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (cmbMarkMultiplier.getSelectedItem() != null) {
                    createList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cmbPointsMultiplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(cmbPointsMultiplier.getSelectedItem()!=null) {
                    createList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cmbView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (cmbView.getSelectedItem() != null) {
                    createList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cmbMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(cmbMode.getSelectedItem()!=null) {
                    createList();
                    controlWithCrease();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        chkDictatMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                createList();
                if (chkDictatMode.isSelected()) {
                    optPoints.setText(getString(R.string.mistakes));
                    int progress = sbBestMarkAt.getProgress();
                    sbBestMarkAt.setProgress(sbWorstMarkTo.getProgress());
                    sbWorstMarkTo.setProgress(progress);
                    lblMaxPoints.setText(getString(R.string.maxMistakes));
                } else {
                    optPoints.setText(getString(R.string.points));
                    int progress = sbBestMarkAt.getProgress();
                    sbBestMarkAt.setProgress(sbWorstMarkTo.getProgress());
                    sbWorstMarkTo.setProgress(progress);
                    lblMaxPoints.setText(getString(R.string.maxPoints));
                }
            }
        });

        chkMarkPoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                createList();
            }
        });

        txtMaxPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createList();
                sbBestMarkAt.setMax(Integer.parseInt(txtMaxPoints.getText().toString()));
                sbWorstMarkTo.setMax(Integer.parseInt(txtMaxPoints.getText().toString()));
                txtCustomPoints.setText(String.valueOf(Integer.parseInt(txtMaxPoints.getText().toString()) / 2));
            }
        });

        txtCustomPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createList();
            }
        });

        txtCustomMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createList();
            }
        });

        cmdSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optPoints.isChecked()) {
                    if(chkDictatMode.isChecked()) {
                        clsHelper.createToast(getApplicationContext(), "Die Note " + txtSearch.getText() + " gibt es \nbei " + String.valueOf(liste.searchPoints(Float.parseFloat(txtSearch.getText().toString()), liste.getView(), createList())) + " " + getString(R.string.mistakes) + "n!");
                    } else {
                        clsHelper.createToast(getApplicationContext(), "Die Note " + txtSearch.getText() + " gibt es \nbei " + String.valueOf(liste.searchPoints(Float.parseFloat(txtSearch.getText().toString()), liste.getView(), createList())) + " " + getString(R.string.points) + "n!");
                    }
                } else {
                    if(chkDictatMode.isChecked()) {
                        clsHelper.createToast(getApplicationContext(), txtSearch.getText() + " Fehler gibt es \nbei der Note " + String.valueOf(liste.searchMark(Float.parseFloat(txtSearch.getText().toString()), liste.getView(), createList())) + "!");
                    } else {
                        clsHelper.createToast(getApplicationContext(), txtSearch.getText() + " Punkte gibt es \nbei der Note " + String.valueOf(liste.searchMark(Float.parseFloat(txtSearch.getText().toString()), liste.getView(), createList())) + "!");
                    }
                }
            }
        });

        sbWorstMarkTo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtWorstMarkTo.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                createList();
            }
        });

        sbBestMarkAt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtBestMarkAt.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                createList();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menCloseApp) {
            System.exit(0);
            return true;
        }

        if(id == R.id.menSaveSettings) {
            settings.setDictMode(chkDictatMode.isChecked());
            settings.setMarkPoints(chkMarkPoints.isChecked());
            settings.setBestMarkAt((float) sbBestMarkAt.getProgress());
            settings.setWorstMarkTo((float) sbWorstMarkTo.getProgress());
            settings.setCustomMark(Float.parseFloat(txtCustomMark.getText().toString()));
            settings.setCustomPoints(Float.parseFloat(txtCustomPoints.getText().toString()));
            switch (cmbMarkMultiplier.getSelectedItemPosition()) {
                case 0:
                    settings.setMarkSign(false);
                    settings.setMarkMultiplier(0.1f);
                    break;
                case 1:
                    settings.setMarkSign(false);
                    settings.setMarkMultiplier(0.25f);
                    break;
                case 2:
                    settings.setMarkSign(false);
                    settings.setMarkMultiplier(0.5f);
                    break;
                case 3:
                    settings.setMarkSign(false);
                    settings.setMarkMultiplier(1.0f);
                    break;
                case 4:
                    settings.setMarkSign(true);
                    settings.setMarkMultiplier(0.25f);
                    break;
            }
            switch (cmbPointsMultiplier.getSelectedItemPosition()) {
                case 0:
                    settings.setPointsMultiplier(0.1f);
                    break;
                case 1:
                    settings.setPointsMultiplier(0.25f);
                    break;
                case 2:
                    settings.setPointsMultiplier(0.5f);
                    break;
                case 3:
                    settings.setPointsMultiplier(1.0f);
                    break;
            }
            settings.setMode(cmbMode.getSelectedItemPosition());
            settings.setView(cmbView.getSelectedItemPosition());
            clsHelper.createToast(getApplicationContext(), getString(R.string.infoSettingsSaved));
        }

        if(id == R.id.menExport) {
            try {
                Intent intent = new Intent(this.getApplicationContext(), actExport.class);
                intent.putExtra(getString(R.string.prefMaxPoints), txtMaxPoints.getText().toString());
                intent.putExtra(getString(R.string.prefDictMode), chkDictatMode.isChecked());
                startActivityForResult(intent, 0);
            }catch (Exception ex) {
                CharSequence text = String.valueOf(ex.toString());
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                toast.show();
            }
        }

        if(id == R.id.menOptions) {
            Intent intent = new Intent(this.getApplicationContext(), actSettings.class);
            startActivityForResult(intent, 0);
        }

        if(id == R.id.menSearch) {
            if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                tblSettings.getLayoutParams().height = tblHeight;
                cmdExp.setImageResource(R.drawable.exp_close);
            }
            txtSearch.requestFocus();
        }

        return super.onOptionsItemSelected(item);
    }

    private Map<Float, Float> createList() {
        Map<Float, Float> marklist = new LinkedHashMap<>();
        if(!txtMaxPoints.getText().toString().equals("")) {
            try {
                adapter.clear();
                adapterHeader.clear();
                if(Double.parseDouble(txtMaxPoints.getText().toString())>9999) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorNumberTooBig));
                    return null;
                }
                boolean dictat = chkDictatMode.isChecked();
                float markMulti, pointMulti;
                boolean marksign = false;
                if(cmbMarkMultiplier.getSelectedItemPosition()==0) {
                    markMulti = 0.1f;
                } else if(cmbMarkMultiplier.getSelectedItemPosition()==1) {
                    markMulti = 0.25f;
                } else if(cmbMarkMultiplier.getSelectedItemPosition()==2) {
                    markMulti = 0.5f;
                } else if(cmbMarkMultiplier.getSelectedItemPosition()==3) {
                    markMulti = 1.0f;
                } else {
                    markMulti = 0.25f;
                    marksign = true;
                }
                if(cmbPointsMultiplier.getSelectedItemPosition()==0) {
                    pointMulti = 0.1f;
                } else if(cmbPointsMultiplier.getSelectedItemPosition()==1) {
                    pointMulti = 0.25f;
                } else if(cmbPointsMultiplier.getSelectedItemPosition()==2) {
                    pointMulti = 0.5f;
                } else {
                    pointMulti = 1.0f;
                }

                liste = new clsMarkPointList(Integer.parseInt(this.txtMaxPoints.getText().toString()), markMulti, pointMulti, dictat);
                liste.setMarkSigns(marksign);
                liste.setMarkPoints(chkMarkPoints.isChecked());
                if(cmbMode.getSelectedItemId()==0) {
                    liste.setMode(clsMarkPointList.Mode.linear);
                } else if(cmbMode.getSelectedItemId()==1) {
                    liste.setMode(clsMarkPointList.Mode.exponential);
                } else if(cmbMode.getSelectedItemId()==2) {
                    liste.setMode(clsMarkPointList.Mode.withCrease);
                } else {
                    liste.setMode(clsMarkPointList.Mode.ihk);
                }
                if(cmbView.getSelectedItemId()==0) {
                    liste.setView(clsMarkPointList.View.bestMarkFirst);
                } else if(cmbView.getSelectedItemId()==1) {
                    liste.setView(clsMarkPointList.View.worstMarkFirst);
                } else if(cmbView.getSelectedItemId()==2) {
                    liste.setView(clsMarkPointList.View.highestPointsFirst);
                } else {
                    liste.setView(clsMarkPointList.View.lowestPointsFirst);
                }

                liste.setBestMarkAt(sbBestMarkAt.getProgress());
                liste.setWorstMarkTo(sbWorstMarkTo.getProgress());
                liste.setCustomMark(Float.parseFloat(txtCustomMark.getText().toString()));
                liste.setCustomPoints(Float.parseFloat(txtCustomPoints.getText().toString()));

                if(liste.getMode()== clsMarkPointList.Mode.withCrease) {
                    if (liste.validateSettings()) {
                        row1.setBackgroundColor(Color.TRANSPARENT);
                        row2.setBackgroundColor(Color.TRANSPARENT);
                        marklist = liste.generateMarkPointList();
                    } else {
                        row1.setBackgroundColor(Color.RED);
                        row2.setBackgroundColor(Color.RED);
                    }
                } else {
                    row1.setBackgroundColor(Color.TRANSPARENT);
                    row2.setBackgroundColor(Color.TRANSPARENT);
                    marklist = liste.generateMarkPointList();
                }

                if(liste.getView()== clsMarkPointList.View.bestMarkFirst || liste.getView() == clsMarkPointList.View.worstMarkFirst) {
                    adapterHeader.add(getString(R.string.mark));
                    if(liste.isInDictatMode()) {
                        adapterHeader.add(getString(R.string.mistakes));
                    } else {
                        adapterHeader.add(getString(R.string.points));
                    }
                } else {
                    if(liste.isInDictatMode()) {
                        adapterHeader.add(getString(R.string.mistakes));
                    } else {
                        adapterHeader.add(getString(R.string.points));
                    }
                    adapterHeader.add(getString(R.string.mark));
                }

                if(!liste.isMarkSigns()) {
                    for (Map.Entry<Float, Float> entry : marklist.entrySet()) {
                        adapter.add(entry.getKey().toString());
                        adapter.add(entry.getValue().toString());
                    }
                } else {
                    Map<String, String> tmp = liste.getSignedList(marklist);
                    for (Map.Entry<String, String> entry : tmp.entrySet()) {
                        adapter.add(entry.getKey());
                        adapter.add(entry.getValue());
                    }
                }
                lvHeader.setAdapter(adapterHeader);
                lvMarkList.setAdapter(adapter);

            } catch(Exception ex) {
                adapter.clear();
                adapterHeader.clear();
                return null;
            }
        } else {
            adapterHeader.clear();
            adapter.clear();
            return null;
        }
        return marklist;
    }

    private void controlWithCrease() {
        if(cmbMode.getSelectedItemPosition()==2) {
            tblSettings.addView(row1, 4);
            tblSettings.addView(row2, 5);
            tblSettings.addView(row3, 6);
            tblSettings.requestLayout();
        } else {
            tblSettings.removeView(row1);
            tblSettings.removeView(row2);
            tblSettings.removeView(row3);
            tblSettings.requestLayout();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0) {
            updateBackgrounds();
        }
    }

    private void closeSoftKeyBoard(){
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch(NullPointerException ex) {
            clsHelper.createToast(this, getString(R.string.errorSomethingWentWrong) + "\n" + ex.getLocalizedMessage());
        }
    }

    private void updateBackgrounds() {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.sysSharedPref),Context.MODE_PRIVATE);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rlMain.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, sharedPref.getInt(getString(R.string.prefBackground), R.drawable.light_bg_texture_01)));
            lvHeader.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, sharedPref.getInt(getString(R.string.prefHeader), R.drawable.medium_bg_texture_04)));
            tblSettings.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, sharedPref.getInt(getString(R.string.prefCTRLCenter), R.drawable.dark_bg_texture_04)));
        } else {
            dangerousBG(sharedPref);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dangerousBG(SharedPreferences sharedPref) {
        rlMain.setBackground(clsHelper.getBitmapDrawable(this, sharedPref.getInt(getString(R.string.prefBackground), R.drawable.light_bg_texture_01)));
        lvHeader.setBackground(clsHelper.getBitmapDrawable(this, sharedPref.getInt(getString(R.string.prefHeader), R.drawable.medium_bg_texture_04)));
        tblSettings.setBackground(clsHelper.getBitmapDrawable(this, sharedPref.getInt(getString(R.string.prefCTRLCenter), R.drawable.dark_bg_texture_04)));
    }
}
