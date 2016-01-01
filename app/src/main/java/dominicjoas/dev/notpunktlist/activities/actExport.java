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
    clsSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actexport);

        settings = new clsSettings(getApplicationContext());
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
                clsMarkPointList markPointList = new clsMarkPointList();
                markPointList.setMaxPoints(Integer.parseInt(getIntent().getStringExtra(getString(R.string.prefMaxPoints))));
                markPointList.setMarkMultiplier(getIntent().getFloatExtra(getString(R.string.prefMarkMultiplier), 0.1f));
                markPointList.setPointsMultiplier(getIntent().getFloatExtra(getString(R.string.prefPointsMultiplier), 0.5f));
                markPointList.setMarkPoints(getIntent().getBooleanExtra(getString(R.string.prefMarkPoints), false));
                markPointList.setCustomPoints(getIntent().getFloatExtra(getString(R.string.prefCustomPoints), 10.0f));
                markPointList.setCustomMark(getIntent().getFloatExtra(getString(R.string.prefCustomMark), 3.5f));
                markPointList.setBestMarkAt(getIntent().getFloatExtra(getString(R.string.prefBestMarkAt), 20.0f));
                markPointList.setWorstMarkTo(getIntent().getFloatExtra(getString(R.string.prefWorstMarkTo), 0.0f));
                markPointList.setMarkSigns(getIntent().getBooleanExtra(getString(R.string.prefMarkSigns), false));
                switch (getIntent().getIntExtra(getString(R.string.prefMode), 0)) {
                    case 0:
                        markPointList.setMode(clsMarkPointList.Mode.linear);
                        break;
                    case 1:
                        markPointList.setMode(clsMarkPointList.Mode.exponential);
                        break;
                    case 2:
                        markPointList.setMode(clsMarkPointList.Mode.withCrease);
                        break;
                    case 3:
                        markPointList.setMode(clsMarkPointList.Mode.ihk);
                        break;
                }
                switch (getIntent().getIntExtra(getString(R.string.prefView), 0)) {
                    case 0:
                        markPointList.setView(clsMarkPointList.View.bestMarkFirst);
                        break;
                    case 1:
                        markPointList.setView(clsMarkPointList.View.worstMarkFirst);
                        break;
                    case 2:
                        markPointList.setView(clsMarkPointList.View.highestPointsFirst);
                        break;
                    case 3:
                        markPointList.setView(clsMarkPointList.View.lowestPointsFirst);
                        break;
                }
                List<String> lsCorrect = new ArrayList<>();
                if(getIntent().getBooleanExtra(getString(R.string.prefDictMode), true)) {
                    lsCorrect.add(getString(R.string.mistakes) + "             " + getString(R.string.mark));
                    for(Map.Entry<Float, Float> entry : markPointList.generateMarkPointList().entrySet()) {
                        String cur = String.valueOf(Double.parseDouble(getIntent().getStringExtra(getString(R.string.prefMaxPoints))) - Double.parseDouble(entry.getKey().toString()));
                        for(int i = cur.length(); i<=20;i++) {
                            cur += " ";
                        }
                        lsCorrect.add(cur + getString(R.string.sysSplitChar) + entry.getValue());
                    }
                } else {
                    lsCorrect.add(getString(R.string.points) + "             " + getString(R.string.mark));
                    for(Map.Entry<Float, Float> entry : markPointList.generateMarkPointList().entrySet()) {
                        String cur = entry.getKey().toString();
                        for(int i = cur.length(); i<=20;i++) {
                            cur += " ";
                        }
                        lsCorrect.add(cur + getString(R.string.sysSplitChar) + entry.getValue());
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
                        writer.println(getString(R.string.prefMaxPoints) + ":\t\t" + getIntent().getStringExtra(getString(R.string.prefMaxPoints)));
                        writer.println(getString(R.string.prefMarkMultiplier) + ":\t\t\t" + getIntent().getFloatExtra(getString(R.string.prefMarkMultiplier), 0.1f));
                        writer.println(getString(R.string.prefPointsMultiplier) + ":\t\t\t" + getIntent().getFloatExtra(getString(R.string.prefPointsMultiplier), 0.5f));
                        writer.println(getString(R.string.prefDictMode) + ":\t\t\t" + getIntent().getBooleanExtra(getString(R.string.prefDictMode), true));
                        writer.println(getString(R.string.prefCustomMark) + ":\t\t\t" + getIntent().getFloatExtra(getString(R.string.prefCustomMark), 3.5f));
                        writer.println(getString(R.string.prefCustomPoints) + ":\t\t\t" + getIntent().getFloatExtra(getString(R.string.prefCustomPoints), 10.0f));
                        writer.println(getString(R.string.prefBestMarkAt) + ":\t\t\t" + getIntent().getFloatExtra(getString(R.string.prefBestMarkAt), 20.0f));
                        writer.println(getString(R.string.prefWorstMarkTo) + ":\t\t\t" + getIntent().getFloatExtra(getString(R.string.prefWorstMarkTo), 0.0f));
                        writer.println(getString(R.string.prefMarkPoints) + ":\t\t\t" + getIntent().getBooleanExtra(getString(R.string.prefMarkPoints), false));
                        writer.println(getString(R.string.prefMarkSigns) + ":\t\t\t" + getIntent().getBooleanExtra(getString(R.string.prefMarkSigns), false));
                        writer.println(getString(R.string.prefMode) + ":\t\t\t" + getIntent().getIntExtra(getString(R.string.prefMode), 0));
                        writer.println(getString(R.string.prefView) + ":\t\t\t" + getIntent().getIntExtra(getString(R.string.prefView), 0));
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
                        writer.println(getString(R.string.prefMaxPoints) + getString(R.string.sysSplitChar) + getIntent().getStringExtra(getString(R.string.prefMaxPoints)));
                        writer.println(getString(R.string.prefMarkMultiplier) + getString(R.string.sysSplitChar) + getIntent().getFloatExtra(getString(R.string.prefMarkMultiplier), 0.1f));
                        writer.println(getString(R.string.prefPointsMultiplier) + getString(R.string.sysSplitChar) + getIntent().getFloatExtra(getString(R.string.prefPointsMultiplier), 0.5f));
                        writer.println(getString(R.string.prefDictMode) + getString(R.string.sysSplitChar) + getIntent().getBooleanExtra(getString(R.string.prefDictMode), true));
                        writer.println(getString(R.string.prefCustomMark) + getString(R.string.sysSplitChar) + getIntent().getFloatExtra(getString(R.string.prefCustomMark), 3.5f));
                        writer.println(getString(R.string.prefCustomPoints) + getString(R.string.sysSplitChar) + getIntent().getFloatExtra(getString(R.string.prefCustomPoints), 10.0f));
                        writer.println(getString(R.string.prefBestMarkAt) + getString(R.string.sysSplitChar) + getIntent().getFloatExtra(getString(R.string.prefBestMarkAt), 20.0f));
                        writer.println(getString(R.string.prefWorstMarkTo) + getString(R.string.sysSplitChar) + getIntent().getFloatExtra(getString(R.string.prefWorstMarkTo), 0.0f));
                        writer.println(getString(R.string.prefMarkPoints) + getString(R.string.sysSplitChar) + getIntent().getBooleanExtra(getString(R.string.prefMarkPoints), false));
                        writer.println(getString(R.string.prefMarkSigns) + getString(R.string.sysSplitChar) + getIntent().getBooleanExtra(getString(R.string.prefMarkSigns), false));
                        writer.println(getString(R.string.prefMode) + getString(R.string.sysSplitChar) + getIntent().getIntExtra(getString(R.string.prefMode), 0));
                        writer.println(getString(R.string.prefView) + getString(R.string.sysSplitChar) + getIntent().getIntExtra(getString(R.string.prefView), 0));
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
                        dict.put(getString(R.string.prefMarkMultiplier), String.valueOf(getIntent().getFloatExtra(getString(R.string.prefMarkMultiplier), 0.1f)));
                        dict.put(getString(R.string.prefPointsMultiplier), String.valueOf(getIntent().getFloatExtra(getString(R.string.prefPointsMultiplier), 0.1f)));
                        dict.put(getString(R.string.prefDictMode), String.valueOf(getIntent().getBooleanExtra(getString(R.string.prefDictMode), true)));
                        dict.put(getString(R.string.prefCustomMark), String.valueOf(getIntent().getFloatExtra(getString(R.string.prefCustomMark), 3.5f)));
                        dict.put(getString(R.string.prefCustomPoints), String.valueOf(getIntent().getFloatExtra(getString(R.string.prefCustomPoints), 10.0f)));
                        dict.put(getString(R.string.prefBestMarkAt),  String.valueOf(getIntent().getFloatExtra(getString(R.string.prefBestMarkAt), 20.0f)));
                        dict.put(getString(R.string.prefWorstMarkTo), String.valueOf(getIntent().getFloatExtra(getString(R.string.prefWorstMarkTo), 0.0f)));
                        dict.put(getString(R.string.prefMarkPoints), String.valueOf(getIntent().getBooleanExtra(getString(R.string.prefMarkPoints), false)));
                        dict.put(getString(R.string.prefMarkSigns),  String.valueOf(getIntent().getBooleanExtra(getString(R.string.prefMarkSigns), false)));
                        dict.put(getString(R.string.prefMode), String.valueOf(getIntent().getIntExtra(getString(R.string.prefMode), 0)));
                        dict.put(getString(R.string.prefView),String.valueOf(getIntent().getIntExtra(getString(R.string.prefView), 0)));
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
            rlMain.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, settings.getCTRLCenter()));
            txtContent.setBackgroundDrawable(clsHelper.getBitmapDrawable(this, settings.getBackground()));
        } else {
            dangerousBG();
        }
        txtPath.setText(settings.getPath());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dangerousBG() {
        rlMain.setBackground(clsHelper.getBitmapDrawable(this, settings.getCTRLCenter()));
        txtContent.setBackground(clsHelper.getBitmapDrawable(this, settings.getBackground()));
    }
}
