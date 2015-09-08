package dominicjoas.dev.notpunktlist.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsMarkList;
import dominicjoas.dev.notpunktlist.classes.clsSharedPreference;

public class actMain extends AppCompatActivity {

    RelativeLayout rlMain;
    TableLayout tblSettings;
    ImageButton cmdExp, cmdSearch;
    EditText txtMaxPoints, txtSearch;
    RadioButton optQuarterMarks, optTenthMarks, optMarks, optPoints;
    CheckBox chkHalfPoints, chkDictatMode;
    GridView lvMarkList, lvHeader;
    TextView lblMaxPoints;

    int tblHeight;
    ArrayAdapter<String> adapter, adapterHeader = null;
    List<String> ls = new ArrayList<>();
    List<String> lsHeader = new ArrayList<>();
    clsMarkList marks = null;
    clsSharedPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmain);
        try {
            pref = new clsSharedPreference(getApplicationContext());
            rlMain = (RelativeLayout) findViewById(R.id.rlMain);
            tblSettings = (TableLayout) findViewById(R.id.tblSettings);
            if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                cmdExp = (ImageButton) findViewById(R.id.cmdOpenClose);
            }
            cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            txtMaxPoints = (EditText) findViewById(R.id.txtMaxPoints);
            txtSearch = (EditText) findViewById(R.id.txtSearch);
            optQuarterMarks = (RadioButton) findViewById(R.id.optQuarterMarks);
            optTenthMarks = (RadioButton) findViewById(R.id.optTenthMarks);
            optMarks = (RadioButton) findViewById(R.id.optMarks);
            optPoints = (RadioButton) findViewById(R.id.optPoints);
            chkHalfPoints = (CheckBox) findViewById(R.id.chkHalfPoints);
            chkDictatMode = (CheckBox) findViewById(R.id.chkDictatMode);
            lvMarkList = (GridView) findViewById(R.id.grdMarkList);
            lvHeader = (GridView) findViewById(R.id.lvHeader);
            lblMaxPoints = (TextView) findViewById(R.id.lblMaxPoints);
            lvHeader.setEnabled(false);
            txtMaxPoints.setText(pref.getMaxPoints());
            optQuarterMarks.setChecked(pref.getQuarterMarks());
            chkHalfPoints.setChecked(pref.getHalfPoints());
            chkDictatMode.setChecked(pref.getDictMode());
            changeSearchText();
            updateBackgrounds();

            if(chkDictatMode.isChecked()) {
                optPoints.setText(getString(R.string.mistakes));
            } else {
                optPoints.setText(getString(R.string.points));
            }
        } catch (Exception ex) {
            clsHelper.createToast(getApplicationContext(), getString(R.string.errorProblemsWithSettings));
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ls);
        adapterHeader = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lsHeader);
        createList();
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
            }
        });

        optQuarterMarks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                createList();
            }
        });

        optTenthMarks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                createList();
            }
        });

        chkHalfPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createList();
            }
        });

        chkDictatMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkDictatMode.isChecked()) {
                    lblMaxPoints.setText(getString(R.string.maxMistakes));
                    optPoints.setText(getString(R.string.mistakes));
                } else {
                    lblMaxPoints.setText(getString(R.string.maxPoints));
                    optPoints.setText(getString(R.string.points));
                }
                createList();
                changeSearchText();
            }
        });

        cmdSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find();
            }
        });

        optPoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeSearchText();
            }
        });

        optMarks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeSearchText();
            }
        });

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    find();
                    closeSoftKeyBoard();
                    return true;
                }
                return false;
            }
        });

        txtMaxPoints.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                closeSoftKeyBoard();
                return true;
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
            pref.setMaxPoints(txtMaxPoints.getText().toString());
            pref.setQuarterMarks(optQuarterMarks.isChecked());
            pref.setHalfPoints(chkHalfPoints.isChecked());
            pref.setDictMode(chkDictatMode.isChecked());
            pref.save();
            clsHelper.createToast(getApplicationContext(), getString(R.string.infoSettingsSaved));
        }

        if(id == R.id.menExport) {
            try {
                Intent intent = new Intent(this.getApplicationContext(), actExport.class);
                intent.putExtra(getString(R.string.prefMaxPoints), txtMaxPoints.getText().toString());
                intent.putExtra(getString(R.string.prefQuarterMarks), optQuarterMarks.isChecked());
                intent.putExtra(getString(R.string.prefHalfPoints), chkHalfPoints.isChecked());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0) {
            updateBackgrounds();
        }
    }

    private void find() {
        try {
            if (!txtSearch.getText().toString().equals("")) {
                txtSearch.setText(txtSearch.getText().toString().replace(",", "."));
                if (optPoints.isChecked()) {
                    if(chkDictatMode.isChecked()) {
                        clsHelper.createInfoDialog(actMain.this, optPoints.getText().toString(), "Für " + getString(R.string.mark) + " " + txtSearch.getText().toString() + " braucht man " + String.valueOf(marks.findPoints(createList(), Double.parseDouble(txtSearch.getText().toString()))) + " " + getString(R.string.mistakes));
                    } else {
                        clsHelper.createInfoDialog(actMain.this, optPoints.getText().toString(), "Für " + getString(R.string.mark) + " " + txtSearch.getText().toString() + " braucht man " + String.valueOf(marks.findPoints(createList(), Double.parseDouble(txtSearch.getText().toString()))) + " " + getString(R.string.points));
                    }
                } else {
                    if(chkDictatMode.isChecked()) {
                        clsHelper.createInfoDialog(actMain.this, optMarks.getText().toString(), "Für " + txtSearch.getText().toString() + " " + getString(R.string.mistakes) + " bekommt man " + getString(R.string.mark) + " " + String.valueOf(marks.findMark(createList(), Double.parseDouble(txtSearch.getText().toString()))));
                    } else {
                        clsHelper.createInfoDialog(actMain.this, optMarks.getText().toString(), "Für " + txtSearch.getText().toString() + " " + getString(R.string.points) + " bekommt man " + getString(R.string.mark) + " " + String.valueOf(marks.findMark(createList(), Double.parseDouble(txtSearch.getText().toString()))));
                    }
                }
            }
        } catch (Exception ex) {
            clsHelper.createToast(this, getString(R.string.errorSearchFailed));
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

    private List<String> createList() {
        if(!txtMaxPoints.getText().toString().equals("")) {
            try {
                adapter.clear();
                adapterHeader.clear();
                if(Double.parseDouble(txtMaxPoints.getText().toString())>9999) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorNumberTooBig));
                    return null;
                }
                boolean dictat = chkDictatMode.isChecked();
                marks = new clsMarkList(Double.parseDouble(txtMaxPoints.getText().toString()), !chkHalfPoints.isChecked(), optTenthMarks.isChecked());
                List<String> lsMarks = marks.generateList();
                if(!dictat) {
                    adapterHeader.add(getString(R.string.points));
                    Collections.reverse(lsMarks);
                    for (String item : lsMarks) {
                        adapter.add(item.split(getString(R.string.sysSplitChar))[0]);
                        adapter.add(item.split(getString(R.string.sysSplitChar))[1]);
                    }
                } else {
                    adapterHeader.add(getString(R.string.mistakes));
                    List<String> lsCorrect = new ArrayList<>();
                    for (String item : lsMarks) {
                        String cur = String.valueOf(Double.parseDouble(txtMaxPoints.getText().toString()) - Double.parseDouble(item.split(getString(R.string.sysSplitChar))[0]));
                        adapter.add(cur);
                        adapter.add(item.split(getString(R.string.sysSplitChar))[1]);
                        lsCorrect.add(cur + getString(R.string.sysSplitChar) + item.split(getString(R.string.sysSplitChar))[1]);
                    }
                    lsMarks = lsCorrect;
                }
                adapterHeader.add(getString(R.string.mark));
                lvMarkList.setAdapter(adapter);
                lvHeader.setAdapter(adapterHeader);
                return lsMarks;
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
    }

    private void changeSearchText() {
        if (optPoints.isChecked()) {
            txtSearch.setHint(getString(R.string.mark));
        } else {
            if (chkDictatMode.isChecked()) {
                txtSearch.setHint(getString(R.string.countMistakes));
            } else {
                txtSearch.setHint(getString(R.string.countPoints));
            }
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
