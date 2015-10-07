package dominicjoas.dev.notpunktlist.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsMarkList;
import dominicjoas.dev.notpunktlist.classes.clsSharedPreference;
import dominicjoas.dev.notpunktlist.viewhelper.MarkListViewHelper;
import dominicjoas.dev.notpunktlist.viewhelper.clsSurface;

public class actMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout rlMain, rlDiagram;
    TableLayout tblSettings;
    ImageButton cmdExp, cmdSearch;
    EditText txtMaxPoints, txtSearch, txtMark, txtPoints;
    RadioButton optMarks, optPoints;
    CheckBox chkDictatMode;
    GridView lvMarkList, lvHeader;
    TextView lblMaxPoints, lblPoints;
    Spinner spMarks, spPoints, spViews, spMode;
    SeekBar sbBestAt, sbWorstAt;
    clsSharedPreference pref;
    clsSurface surf;

    int tblHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            this.setTitle(this.getTitle() + " v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            pref = new clsSharedPreference(getApplicationContext());
            rlMain = (RelativeLayout) findViewById(R.id.rlMain);
            rlDiagram = (RelativeLayout) findViewById(R.id.rlDiagram);
            surf = new clsSurface(this,0,20);
            rlDiagram.addView(surf);
            tblSettings = (TableLayout) findViewById(R.id.tblSettings);
            if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                cmdExp = (ImageButton) findViewById(R.id.cmdOpenClose);
            }
            cmdSearch = (ImageButton) findViewById(R.id.cmdSearch);
            txtMaxPoints = (EditText) findViewById(R.id.txtMaxPoints);
            txtSearch = (EditText) findViewById(R.id.txtSearch);
            spMarks = (Spinner) findViewById(R.id.spMarks);
            spPoints = (Spinner) findViewById(R.id.spPoints);
            optMarks = (RadioButton) findViewById(R.id.optMarks);
            optPoints = (RadioButton) findViewById(R.id.optPoints);
            spViews = (Spinner) findViewById(R.id.spView);
            chkDictatMode = (CheckBox) findViewById(R.id.chkDictatMode);
            lvMarkList = (GridView) findViewById(R.id.grdMarkList);
            lvHeader = (GridView) findViewById(R.id.lvHeader);
            lblMaxPoints = (TextView) findViewById(R.id.lblMaxPoints);
            lblPoints = (TextView) findViewById(R.id.lblCurPoints);
            spMode = (Spinner) findViewById(R.id.spMode);
            sbBestAt = (SeekBar) findViewById(R.id.sbBestMark);
            sbWorstAt = (SeekBar) findViewById(R.id.sbWorst);
            txtMark = (EditText) findViewById(R.id.txtCurMark);
            txtPoints = (EditText) findViewById(R.id.txtCurPoints);
            lvHeader.setEnabled(false);
            txtMaxPoints.setText(pref.getMaxPoints());
            spMarks.setSelection(Integer.parseInt(String.valueOf(pref.getMarks())));
            spMode.setSelection(Integer.parseInt(String.valueOf(pref.getMode())));
            spPoints.setSelection(Integer.parseInt(String.valueOf(pref.getPoints())));
            spViews.setSelection(Integer.parseInt(String.valueOf(pref.getViews())));
            sbBestAt.setMax(Integer.parseInt(pref.getMaxPoints()));
            sbWorstAt.setMax(Integer.parseInt(pref.getMaxPoints()));
            txtMark.setText(String.valueOf(pref.getUserMark()));
            txtPoints.setText(String.valueOf(pref.getUserPoint()));
            sbBestAt.setProgress((int)pref.getBestMarkAt());
            sbWorstAt.setProgress((int)pref.getWorstMarkAt());
            chkDictatMode.setChecked(pref.getDictMode());
            ((ArrayAdapter) spMarks.getAdapter()).notifyDataSetChanged();
            ((ArrayAdapter) spViews.getAdapter()).notifyDataSetChanged();
            changeSearchText();
            updateBackgrounds();
            createList();

            if(chkDictatMode.isChecked()) {
                optPoints.setText(getString(R.string.mistakes));
            } else {
                optPoints.setText(getString(R.string.points));
            }
        } catch (Exception ex) {
            clsHelper.createToast(getApplicationContext(), getString(R.string.errorProblemsWithSettings) + "\n" + ex.toString());
        }

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            tblHeight = tblSettings.getLayoutParams().height;
            tblSettings.getLayoutParams().height = cmdExp.getLayoutParams().height;


            cmdExp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tblSettings.getLayoutParams().height == cmdExp.getLayoutParams().height) {
                        tblSettings.getLayoutParams().height = tblHeight;
                    } else {
                        tblSettings.getLayoutParams().height = cmdExp.getLayoutParams().height;
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

        spMarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPoints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spViews.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sbBestAt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                createList();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbWorstAt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                createList();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        txtMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                createList();
            }
        });

        txtPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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
                //find();
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
                    //find();
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_main, menu);
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
            pref.setMarks(spMarks.getSelectedItemId());
            pref.setPoints(spPoints.getSelectedItemId());
            pref.setViews(spViews.getSelectedItemId());
            pref.setDictMode(chkDictatMode.isChecked());
            pref.setMode(spMode.getSelectedItemId());
            pref.setBestMarkAt(sbBestAt.getProgress());
            pref.setWorstMarkAt(sbWorstAt.getProgress());
            pref.setUserMark(Float.parseFloat(txtMark.getText().toString()));
            pref.setUserPoint(Float.parseFloat(txtPoints.getText().toString()));
            pref.save();
            clsHelper.createToast(getApplicationContext(), getString(R.string.infoSettingsSaved));
        }

        if(id == R.id.menExport) {
            try {
                Intent intent = new Intent(this.getApplicationContext(), actExport.class);
                intent.putExtra(getString(R.string.prefMaxPoints), txtMaxPoints.getText().toString());
                intent.putExtra(getString(R.string.mark), spMarks.getSelectedItemId());
                intent.putExtra(getString(R.string.points), spMarks.getSelectedItemId());
                intent.putExtra(getString(R.string.view), spViews.getSelectedItemId());
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
            }
            txtSearch.requestFocus();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0) {
            updateBackgrounds();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        surf.onResumeMySurfaceView();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        surf.onPauseMySurfaceView();
    }

    /*private void find() {
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
    }*/

    private void closeSoftKeyBoard(){
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch(NullPointerException ex) {
            clsHelper.createToast(this, getString(R.string.errorSomethingWentWrong) + "\n" + ex.getLocalizedMessage());
        }
    }

    private void createList() {
        if(!txtMaxPoints.getText().equals("")) {
            clsMarkList.Sorting srt;
            clsMarkList.Mode mode;
            MarkListViewHelper viewer = new MarkListViewHelper(Integer.parseInt(txtMaxPoints.getText().toString()), lvMarkList, lvHeader, getApplicationContext());
            viewer.setDictatMode(chkDictatMode.isChecked());
            switch(Integer.parseInt(String.valueOf(spMode.getSelectedItemId()))) {
                case 0:
                    mode = clsMarkList.Mode.linear;
                    break;
                case 1:
                    mode = clsMarkList.Mode.exponential;
                    break;
                case 2:
                    mode = clsMarkList.Mode.withCrease;
                    break;
                case 3:
                    mode = clsMarkList.Mode.ihk;
                    break;
                default:
                    mode = clsMarkList.Mode.abitur;
            }
            switch (Integer.parseInt(String.valueOf(spPoints.getSelectedItemId()))) {
                case 0:
                    viewer.setPointsMultiplier(clsMarkList.PointsMultiplier.quarterPoints);
                    break;
                case 1:
                    viewer.setPointsMultiplier(clsMarkList.PointsMultiplier.halfPoints);
                    break;
                case 2:
                    viewer.setPointsMultiplier(clsMarkList.PointsMultiplier.wholePoints);
                    break;
                default:
                    viewer.setPointsMultiplier(clsMarkList.PointsMultiplier.wholePoints);
            }
            switch (Integer.parseInt(String.valueOf(spMarks.getSelectedItemId()))) {
                case 0:
                    viewer.setMarkMultiplier(clsMarkList.MarkMultiplier.tenthMarks);
                    break;
                case 1:
                    viewer.setMarkMultiplier(clsMarkList.MarkMultiplier.quarterMarks);
                    break;
                case 2:
                    viewer.setMarkMultiplier(clsMarkList.MarkMultiplier.wholeMarks);
                    break;
                case 3:
                    viewer.setMarkMultiplier(clsMarkList.MarkMultiplier.namedMarks);
                    break;
                default:
                    viewer.setMarkMultiplier(clsMarkList.MarkMultiplier.abiturpoints);
            }
            switch (Integer.parseInt(String.valueOf(spViews.getSelectedItemId()))) {
                case 0:
                    srt = clsMarkList.Sorting.bestMarkFirst;
                    break;
                case 1:
                    srt = clsMarkList.Sorting.worstMarkFirst;
                    break;
                case 2:
                    srt = clsMarkList.Sorting.highestPointsFirst;
                    break;
                default:
                    srt = clsMarkList.Sorting.lowestPointsFirst;
            }
            viewer.update(srt, mode);
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
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.sysSharedPref), Context.MODE_PRIVATE);
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
