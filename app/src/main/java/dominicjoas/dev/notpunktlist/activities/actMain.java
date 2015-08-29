package dominicjoas.dev.notpunktlist.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.clsHelper;
import dominicjoas.dev.notpunktlist.classes.clsMarkList;

public class actMain extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmain);
        try {

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
            lvHeader.setEnabled(true);
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("NOTPUNKTLIST",Context.MODE_PRIVATE);
            txtMaxPoints.setText(sharedPref.getString(String.valueOf(R.string.maximumPoints), String.valueOf(txtMaxPoints.getText())));
            optQuarterMarks.setChecked(sharedPref.getBoolean(String.valueOf(R.string.quarterMarks), optQuarterMarks.isChecked()));
            optMarks.setChecked(sharedPref.getBoolean("Note", optMarks.isChecked()));
            chkHalfPoints.setChecked(sharedPref.getBoolean(String.valueOf(R.string.halfPoints), false));
            chkDictatMode.setChecked(sharedPref.getBoolean(String.valueOf(R.string.dictatMode), false));
            changeSearchText();
            updateBackgrounds();

            if(chkDictatMode.isChecked()) {
                optPoints.setText("Fehler");
            } else {
                optPoints.setText("Punkte");
            }
        } catch (Exception ex) {
            CharSequence text = String.valueOf("Es gab Probleme beim Laden der Einstellungen!");
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
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
                    lblMaxPoints.setText("Maximale Fehleranzahl");
                    optPoints.setText("Fehler");
                } else {
                    lblMaxPoints.setText("Maximale Punktzahl");
                    optPoints.setText("Punkte");
                }
                createList();
                changeSearchText();
            }
        });

        cmdSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtSearch.getText().toString().equals("")) {
                    try {
                        txtSearch.setText(txtSearch.getText().toString().replace(",", "."));
                        if (optPoints.isChecked()) {
                            CharSequence text = String.valueOf(marks.findPoints(createList(), Double.parseDouble(txtSearch.getText().toString())));
                            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            CharSequence text = String.valueOf(marks.findMark(createList(), Double.parseDouble(txtSearch.getText().toString())));
                            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } catch (Exception ex) {
                        CharSequence text = String.valueOf("Die Suchangabe ist fehlerhaft!");
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menCloseApp) {
            System.exit(0);
            return true;
        }

        if(id == R.id.menSaveSettings) {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("NOTPUNKTLIST", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(String.valueOf(R.string.maximumPoints), txtMaxPoints.getText().toString());
            editor.putBoolean(String.valueOf(R.string.quarterMarks), optQuarterMarks.isChecked());
            editor.putBoolean("Note", optMarks.isChecked());
            editor.putBoolean(String.valueOf(R.string.halfPoints), chkHalfPoints.isChecked());
            editor.putBoolean(String.valueOf(R.string.dictatMode), chkDictatMode.isChecked());
            editor.commit();
            CharSequence text = String.valueOf("Einstellungen wurden gespeichert!");
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
        }

        if(id == R.id.menExport) {
            try {
                Intent intent = new Intent(this.getApplicationContext(), actExport.class);
                intent.putExtra("maxPoints", txtMaxPoints.getText().toString());
                intent.putExtra("quarterMarks", optQuarterMarks.isChecked());
                intent.putExtra("halfPoints", chkHalfPoints.isChecked());
                intent.putExtra("dictMode", chkDictatMode.isChecked());
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0) {
            updateBackgrounds();
        }
    }

    private List<String> createList() {
        if(!txtMaxPoints.getText().toString().equals("")) {
            try {
                if(Double.parseDouble(txtMaxPoints.getText().toString())>9999) {
                    CharSequence text = String.valueOf(String.valueOf("Die Zahl ist zu groß!"));
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                    adapter.clear();
                    adapterHeader.clear();
                    return null;
                }
                boolean dictat = chkDictatMode.isChecked();
                if(!dictat) {
                    adapter.clear();
                    adapterHeader.clear();
                    marks = new clsMarkList(Double.parseDouble(txtMaxPoints.getText().toString()), !chkHalfPoints.isChecked(), optTenthMarks.isChecked());
                    adapterHeader.add("Punkte");
                    adapterHeader.add("Note");
                    List<String> lsMarks = marks.generateList();
                    Collections.reverse(lsMarks);
                    for (String item : lsMarks) {
                        adapter.add(item.split(";")[0]);
                        adapter.add(item.split(";")[1]);
                    }
                    lvMarkList.setAdapter(adapter);
                    lvHeader.setAdapter(adapterHeader);
                    return lsMarks;
                } else {
                    adapterHeader.clear();
                    adapter.clear();
                    marks = new clsMarkList(Double.parseDouble(txtMaxPoints.getText().toString()), !chkHalfPoints.isChecked(), optTenthMarks.isChecked());
                    adapterHeader.add("Fehler");
                    adapterHeader.add("Note");
                    List<String> lsMarks = marks.generateList();
                    List<String> lsCorrect = new ArrayList<>();
                    for (String item : lsMarks) {
                        String cur = String.valueOf(Double.parseDouble(txtMaxPoints.getText().toString()) - Double.parseDouble(item.split(";")[0]));
                        adapter.add(cur);
                        adapter.add(item.split(";")[1]);
                        lsCorrect.add(cur + ";" + item.split(";")[1]);
                    }
                    lvMarkList.setAdapter(adapter);
                    lvHeader.setAdapter(adapterHeader);
                    return lsCorrect;
                }
            } catch(Exception ex) {
                System.out.println(ex.toString());
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
            txtSearch.setHint("Note");
        } else {
            if (chkDictatMode.isChecked()) {
                txtSearch.setHint("Anzahl der Fehler");
            } else {
                txtSearch.setHint("Anzahl der Punkte");
            }
        }
    }

    private void updateBackgrounds() {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("NOTPUNKTLIST",Context.MODE_PRIVATE);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rlMain.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, sharedPref.getInt("Hintergrund", R.drawable.light_bg_texture_01)));
            lvHeader.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, sharedPref.getInt("Kopfbereich", R.drawable.medium_bg_texture_04)));
            tblSettings.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, sharedPref.getInt("Kontrollzentrum", R.drawable.dark_bg_texture_04)));
        } else {
            rlMain.setBackground(clsHelper.getBitmapDrawable(this, sharedPref.getInt("Hintergrund", R.drawable.light_bg_texture_01)));
            lvHeader.setBackground(clsHelper.getBitmapDrawable(this, sharedPref.getInt("Kopfbereich", R.drawable.medium_bg_texture_04)));
            tblSettings.setBackground(clsHelper.getBitmapDrawable(this, sharedPref.getInt("Kontrollzentrum", R.drawable.dark_bg_texture_04)));
        }
    }
}
