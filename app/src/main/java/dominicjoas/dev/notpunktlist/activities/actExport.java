package dominicjoas.dev.notpunktlist.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dominicjoas.dev.notpunktlist.R;
import dominicjoas.dev.notpunktlist.classes.*;

public class actExport extends AppCompatActivity {

    RelativeLayout rlMain;
    EditText txtPath, txtContent;
    RadioButton optTXT, optXML, optCSV;
    Button cmdExport;
    clsSharedPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actexport);

        pref = new clsSharedPreference(getApplicationContext());
        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        txtPath = (EditText) findViewById(R.id.txtPath);
        txtContent = (EditText) findViewById(R.id.txtContent);

        optTXT = (RadioButton) findViewById(R.id.optExportToTXT);
        optXML = (RadioButton) findViewById(R.id.optExportToXML);
        optCSV = (RadioButton) findViewById(R.id.optExportToCSV);

        cmdExport = (Button) findViewById(R.id.cmdExport);

        txtPath.setText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
        txtPath.setEnabled(false);
        txtContent.setFocusable(false);

        updateBackgrounds();

        cmdExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clsMarkList list = new clsMarkList(Integer.parseInt(getIntent().getStringExtra(getString(R.string.prefMaxPoints))));
                Map<Double, Double> marklist = list.generateList(clsMarkList.Sorting.bestMarkFirst, clsMarkList.Mode.linear);
                List<String> lsCorrect = new ArrayList<>();
                if(getIntent().getBooleanExtra(getString(R.string.prefDictMode), true)) {
                    lsCorrect.add(getString(R.string.mistakes) + "             " + getString(R.string.mark));
                    for(Double key : marklist.keySet()) {
                        String cur = String.valueOf(Double.parseDouble(getIntent().getStringExtra(getString(R.string.prefMaxPoints))) - key);
                        for(int i = cur.length(); i<=20;i++) {
                            cur += " ";
                        }
                        lsCorrect.add(cur + getString(R.string.sysSplitChar) + marklist.get(key));
                    }
                } else {
                    lsCorrect.add(getString(R.string.points) + "             " + getString(R.string.mark));
                    for(Double key : marklist.keySet()) {
                        String cur = String.valueOf(key);
                        for(int i = cur.length(); i<=20;i++) {
                            cur += " ";
                        }
                        lsCorrect.add(cur + getString(R.string.sysSplitChar) + marklist.get(key));
                    }
                }
                String path = "";
                try {
                    if(!new File(txtPath.getText().toString()).exists()) {
                        if(!new File(txtPath.getText().toString()).mkdirs()) {
                            clsHelper.createToast(getApplicationContext(), getString(R.string.errorCantCreateFolders));
                        }
                    }
                } catch(Exception ex) {
                    clsHelper.createToast(getApplicationContext(), ex.getLocalizedMessage());
                }
                if(optTXT.isChecked()) {
                    try {
                        path = txtPath.getText().toString() + "/" + getString(R.string.expFilePart) + "_" + new Date().getTime() + ".txt";
                        PrintWriter writer = new PrintWriter(path, getString(R.string.sysCharset));
                        writer.println(getString(R.string.expTitle));
                        writer.println(getString(R.string.settings));
                        writer.println(getString(R.string.maxPoints) + ":\t\t" + getIntent().getStringExtra(getString(R.string.prefMaxPoints)));
                        writer.println(getString(R.string.quarterMarks) + ":\t\t\t" + getIntent().getIntExtra(pref.PREFCUSTOMMARK, 0));
                        writer.println(getString(R.string.halfPoints) + ":\t\t\t" + getIntent().getBooleanExtra(getString(R.string.halfPoints), true));
                        writer.println(getString(R.string.dictatMode) + ":\t\t\t" + getIntent().getBooleanExtra(getString(R.string.prefDictMode), true));
                        for(String item : lsCorrect) {
                            writer.println(item);
                        }
                        writer.close();
                        clsHelper.createToast(getApplicationContext(), getString(R.string.infoExportSuccessfully));
                    } catch(Exception ex) {
                        clsHelper.createToast(getApplicationContext(), getString(R.string.errorCantCreateFile));
                    }
                } else if(optCSV.isChecked()) {
                    try {
                        path = txtPath.getText().toString() + "/" + getString(R.string.expFilePart) + "_" + new Date().getTime() + ".csv";
                        PrintWriter writer = new PrintWriter(path, getString(R.string.sysCharset));
                        writer.println(getString(R.string.expTitle));
                        writer.println(getString(R.string.settings));
                        writer.println(getString(R.string.maxPoints) + getString(R.string.sysSplitChar) + getIntent().getStringExtra(getString(R.string.prefMaxPoints)));
                        writer.println(getString(R.string.quarterMarks) + getString(R.string.sysSplitChar) + getIntent().getIntExtra(pref.PREFMARKMODE , 0));
                        writer.println(getString(R.string.halfPoints) + getString(R.string.sysSplitChar) + getIntent().getBooleanExtra(getString(R.string.halfPoints), true));
                        writer.println(getString(R.string.dictatMode) + getString(R.string.sysSplitChar) + getIntent().getBooleanExtra(getString(R.string.prefDictMode), true));
                        for(String item : lsCorrect) {
                            writer.println(item.replace(" ", ""));
                        }
                        writer.close();
                        clsHelper.createToast(getApplicationContext(), getString(R.string.infoExportSuccessfully));
                    } catch(Exception ex) {
                        clsHelper.createToast(getApplicationContext(), getString(R.string.errorCantCreateFile));
                    }
                } else {
                    try {
                        clsXML xml = new clsXML(getString(R.string.expXMLRoot));
                        path = txtPath.getText().toString() + "/" +  getString(R.string.expFilePart) + "_" + new Date().getTime() + ".xml";
                        Map<String, String> dict = new HashMap<>();
                        dict.put(getString(R.string.prefMaxPoints), getIntent().getStringExtra(getString(R.string.prefMaxPoints)));
                        dict.put(pref.PREFMARKMODE , String.valueOf(getIntent().getBooleanExtra(pref.PREFMARKMODE, true)));
                        dict.put(pref.PREFPOINTMODE , String.valueOf(getIntent().getBooleanExtra(getString(R.string.halfPoints), true)));
                        dict.put(getString(R.string.prefDictMode), String.valueOf(getIntent().getBooleanExtra(getString(R.string.prefDictMode), true)));
                        xml.addElement(getString(R.string.expXMLList), dict);
                        String first = "", second=getString(R.string.expXMLMark);
                        for(String item : lsCorrect) {
                            if(item.contains(";")) {
                                dict = new HashMap<>();
                                dict.put(first, item.split(";")[0].trim());
                                dict.put(second, item.split(";")[1].trim());
                                xml.addSubElement(getString(R.string.expXMLElem), dict, getString(R.string.expXMLList));
                            } else {
                                if(item.contains(getString(R.string.mistakes))) {
                                    first = getString(R.string.expXMLMistakes);
                                } else {
                                    first = getString(R.string.expXMLPoints);
                                }
                            }
                        }
                        xml.save(path);
                        clsHelper.createToast(getApplicationContext(), getString(R.string.infoExportSuccessfully));
                    } catch(Exception ex) {
                        clsHelper.createToast(getApplicationContext(), getString(R.string.errorCantCreateFile));
                    }
                }

                try {
                    File file = new File(path);
                    StringBuilder txt = new StringBuilder();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        txt.append(line.replace(getString(R.string.sysSplitChar), ":"));
                        txt.append('\n');
                    }
                    br.close();
                    txtContent.setText(txt.toString());
                } catch (IOException ex) {
                    clsHelper.createToast(getApplicationContext(), getString(R.string.errorCantCreateFile));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menexport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menBack) {
            this.finish();
            return true;
        }
        if(id == R.id.menClose) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateBackgrounds() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rlMain.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, pref.getCTRLCenter()));
            txtContent.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, pref.getBackground()));
        } else {
            dangerousBG();
        }
        txtPath.setText(pref.getPath());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dangerousBG() {
        rlMain.setBackground(clsHelper.getBitmapDrawable(this, pref.getCTRLCenter()));
        txtContent.setBackground(clsHelper.getBitmapDrawable(this, pref.getBackground()));
    }
}
