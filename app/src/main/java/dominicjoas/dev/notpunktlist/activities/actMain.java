package dominicjoas.dev.notpunktlist.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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

import java.text.DecimalFormat;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.viewhelper.clsDrawCurrent;
import dominicjoas.dev.notpunktlist.viewhelper.clsDrawDiagram;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsMarkList;
import dominicjoas.dev.notpunktlist.classes.clsSharedPreference;
import dominicjoas.dev.notpunktlist.viewhelper.MarkListViewHelper;

public class actMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout rlMain, rlDiagram, rlLineDiagram;
    TableLayout tblSettings;
    GridView lvMarkList, lvHeader;
    TextView lblMaxPoints, lblPoints, lblBestMark, lblWorstMark;
    EditText txtMaxPoints, txtSearch, txtMark, txtPoints;
    ImageButton cmdExp, cmdSearch;
    RadioButton optMarks, optPoints;
    CheckBox chkDictatMode;
    Spinner spMarks, spPoints, spViews, spMode;
    SeekBar sbBestAt, sbWorstAt;
    clsSharedPreference pref;
    clsDrawDiagram diagram;
    clsDrawCurrent current;

    int tblHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmain);

        // Instantiate basic controls of the Main-Activity
        // do not change that (it's for the drawer of the activity)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Instantiate controls of the activity
        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        rlDiagram = (RelativeLayout) findViewById(R.id.rlDiagram);
        rlLineDiagram = (RelativeLayout) findViewById(R.id.rlCurrentLine);
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
        lblBestMark = (TextView) findViewById(R.id.lblCurrentBestMark);
        lblWorstMark = (TextView) findViewById(R.id.lblCurrentWorstMark);
        spMode = (Spinner) findViewById(R.id.spMode);
        sbBestAt = (SeekBar) findViewById(R.id.sbBestMark);
        sbWorstAt = (SeekBar) findViewById(R.id.sbWorst);
        txtMark = (EditText) findViewById(R.id.txtCurMark);
        txtPoints = (EditText) findViewById(R.id.txtCurPoints);

        try {
            // create object of classes 'clsSharedPreferences', 'clsDrawDiagram', 'clsDrawCurrent'
            pref = new clsSharedPreference(getApplicationContext());
            current = new clsDrawCurrent(getApplicationContext());
            diagram = new clsDrawDiagram(getApplicationContext());

            // add version to title
            this.setTitle(this.getTitle() + " v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            lvHeader.setEnabled(false);

            // add values of the sharedPreferences to controls
            txtMaxPoints.setText(String.valueOf(pref.getMaxPoints()));
            spPoints.setSelection(Integer.parseInt(String.valueOf(pref.getPointMode())));
            spMarks.setSelection(Integer.parseInt(String.valueOf(pref.getMarkMode())));
            spViews.setSelection(Integer.parseInt(String.valueOf(pref.getViewMode())));
            chkDictatMode.setChecked(pref.getDictMode());

            spMode.setSelection(Integer.parseInt(String.valueOf(pref.getMarkListMode())));
            sbBestAt.setMax(pref.getMaxPoints());
            sbWorstAt.setMax(pref.getMaxPoints());
            if(chkDictatMode.isChecked()) {
                sbBestAt.setProgress(pref.getWorstMarkAt());
                sbWorstAt.setProgress(pref.getBestMarkAt());
                lblBestMark.setText(String.valueOf(pref.getWorstMarkAt()));
                lblWorstMark.setText(String.valueOf(pref.getBestMarkAt()));
            } else {
                sbBestAt.setProgress(pref.getBestMarkAt());
                sbWorstAt.setProgress(pref.getWorstMarkAt());
                lblBestMark.setText(String.valueOf(pref.getBestMarkAt()));
                lblWorstMark.setText(String.valueOf(pref.getWorstMarkAt()));
            }
            txtMark.setText(String.valueOf(pref.getCustomMark()));
            txtPoints.setText(String.valueOf(pref.getCustomPoint()));

            diagram.setMaxPoints(pref.getMaxPoints());
            diagram.setDictMode(pref.getDictMode());
            rlDiagram.addView(diagram);

            current.setMaxPoints(pref.getMaxPoints());
            current.setDictMode(pref.getDictMode());
            current.setBestMarkAt(pref.getBestMarkAt());
            current.setWorstMarkAt(pref.getWorstMarkAt());
            current.setTextViewMark(txtMark);
            current.setTextViewPoints(txtPoints);
            rlLineDiagram.addView(current);

            ((ArrayAdapter) spMarks.getAdapter()).notifyDataSetChanged();
            ((ArrayAdapter) spViews.getAdapter()).notifyDataSetChanged();
            if(chkDictatMode.isChecked()) {
                optPoints.setText(getString(R.string.mistakes));
            } else {
                optPoints.setText(getString(R.string.points));
            }

        } catch (Exception ex) {
            clsHelper.createToast(getApplicationContext(), getString(R.string.errorProblemsWithSettings));
        }

        changeSearchText();
        updateBackgrounds();
        createList();

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
                try {
                    if (!txtMaxPoints.getText().toString().equals("")) {
                        int maxPoints = Integer.parseInt(txtMaxPoints.getText().toString());
                        createList();
                        diagram.setMaxPoints(maxPoints);
                        diagram.calcDiagram();
                        current.setMaxPoints(pref.getMaxPoints());
                        current.calcDiagram(false);
                    }
                } catch (Exception ex) {
                    clsHelper.createToast(getApplicationContext(), getApplicationContext().getString(R.string.errorSomethingWentWrong));
                }
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
                controllDiagram();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sbBestAt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (chkDictatMode.isChecked()) {
                    current.setBestMarkAt(Integer.parseInt(txtMaxPoints.getText().toString()) - sbBestAt.getProgress());
                    lblBestMark.setText(String.valueOf(sbBestAt.getProgress()));
                } else {
                    current.setBestMarkAt(Integer.parseInt(txtMaxPoints.getText().toString()) - sbBestAt.getProgress());
                    lblBestMark.setText(String.valueOf(Integer.parseInt(txtMaxPoints.getText().toString()) - sbBestAt.getProgress()));
                }
                current.calcDiagram(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                try {
                    sbBestAt.setMax(Integer.parseInt(txtPoints.getText().toString().replace(".", "-").split("-")[0]) - 1);
                } catch (Exception ex) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorSomethingWentWrong));
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbWorstAt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(chkDictatMode.isChecked()) {
                    current.setWorstMarkAt(sbWorstAt.getProgress());
                    lblWorstMark.setText(String.valueOf(Integer.parseInt(txtMaxPoints.getText().toString()) - sbWorstAt.getProgress()));
                } else {
                    current.setWorstMarkAt(sbWorstAt.getProgress());
                    lblWorstMark.setText(String.valueOf(sbWorstAt.getProgress()));
                }
                current.calcDiagram(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                try {
                    sbWorstAt.setMax(Integer.parseInt(new DecimalFormat("0").format(Double.parseDouble(txtPoints.getText().toString()))));
                }catch(Exception ex) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorSomethingWentWrong));
                }
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
                try {
                    createList();
                } catch (Exception ex) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorSomethingWentWrong));
                }
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
                try {
                    createList();
                }catch (Exception ex) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorSomethingWentWrong));
                }
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
                diagram.setDictMode(chkDictatMode.isChecked());
                diagram.calcDiagram();
                current.setDictMode(chkDictatMode.isChecked());
                current.calcDiagram(false);
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

        txtPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                current.setTextViewPoints(txtPoints);
                current.calcDiagram(false);
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
                current.setTextViewMark(txtMark);
                current.calcDiagram(false);
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
            pref.setMaxPoints(Integer.parseInt(txtMaxPoints.getText().toString()));
            pref.setMarkMode(spMarks.getSelectedItemId());
            pref.setPointMode(spPoints.getSelectedItemId());
            pref.setViewMode(spViews.getSelectedItemId());
            pref.setDictMode(chkDictatMode.isChecked());
            pref.setMarkListMode(spMode.getSelectedItemId());
            pref.setBestMarkAt(Integer.parseInt(lblBestMark.getText().toString()));
            pref.setWorstMarkAt(Integer.parseInt(lblWorstMark.getText().toString()));
            float customMark, customPoint;
            try {
                customMark = Float.parseFloat(String.valueOf(txtMark.getText()));
            } catch(Exception ex) {
                customMark = 3.5F;
            }
            try {
                customPoint = Float.parseFloat(String.valueOf(txtPoints.getText()));
            } catch(Exception ex) {
                customPoint = Integer.parseInt(txtMaxPoints.getText().toString())/2;
            }
            pref.setCustomMark(customMark);
            pref.setCustomPoint(customPoint);
            pref.save();
            clsHelper.createToast(getApplicationContext(), getString(R.string.infoSettingsSaved));
        }

        if(id == R.id.menExport) {
            try {
                Intent intent = new Intent(this.getApplicationContext(), actExport.class);
                intent.putExtra(pref.PREFMAXPOINTS, txtMaxPoints.getText().toString());
                intent.putExtra(pref.PREFMARKLISTMODE, spMode.getSelectedItemId());
                intent.putExtra(pref.PREFMARKMODE, spMarks.getSelectedItemId());
                intent.putExtra(pref.PREFPOINTMODE, spPoints.getSelectedItemId());
                intent.putExtra(pref.PREFVIEWMODE, spViews.getSelectedItemId());
                intent.putExtra(pref.PREFDICTMODE, chkDictatMode.isChecked());
                intent.putExtra(pref.PREFBESTAT, sbBestAt.getProgress());
                intent.putExtra(pref.PREFWORSTAT, sbWorstAt.getProgress());
                intent.putExtra(pref.PREFCUSTOMPOINT, txtPoints.getText().toString());
                intent.putExtra(pref.PREFCUSTOMMARK, txtMark.getText().toString());
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
        super.onResume();
        this.diagram.onResumeMySurfaceView();
        this.current.onResumeMySurfaceView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.diagram.onPauseMySurfaceView();
        this.current.onPauseMySurfaceView();
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
        if(!txtMaxPoints.getText().toString().equals("")) {
            clsMarkList.Sorting srt;
            clsMarkList.Mode mode;
            MarkListViewHelper viewer = new MarkListViewHelper(this, Integer.parseInt(txtMaxPoints.getText().toString()), lvMarkList, lvHeader, getApplicationContext());
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
            Thread th = new Thread(viewer);
            th.start();
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
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rlMain.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, pref.getBackground()));
            lvHeader.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, pref.getHeader()));
            tblSettings.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, pref.getCTRLCenter()));
        } else {
            dangerousBG();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dangerousBG() {
        rlMain.setBackground(clsHelper.getBitmapDrawable(this, pref.getBackground()));
        lvHeader.setBackground(clsHelper.getBitmapDrawable(this, pref.getHeader()));
        tblSettings.setBackground(clsHelper.getBitmapDrawable(this, pref.getCTRLCenter()));
    }

    private void controllDiagram() {
        switch(Integer.parseInt(String.valueOf(spMode.getSelectedItemId()))) {
            case 0:
            case 3:
            case 5:
                rlDiagram.setEnabled(false);
                sbBestAt.setEnabled(false);
                sbWorstAt.setEnabled(false);
                txtMark.setEnabled(false);
                txtPoints.setEnabled(false);
                txtPoints.setText(String.valueOf(current.getMaxPoints() / 2));
                txtMark.setText(String.valueOf(3.5));
                current.setWorstMarkAt(0);
                current.setBestMarkAt(current.getMaxPoints());
                current.setTextViewPoints(txtPoints);
                current.setTextViewMark(txtMark);
                break;
            case 1:
            case 2:
                rlDiagram.setEnabled(true);
                sbBestAt.setEnabled(true);
                sbWorstAt.setEnabled(true);
                txtMark.setEnabled(true);
                txtPoints.setEnabled(true);
                break;
        }
        current.calcDiagram(false);
    }
}
