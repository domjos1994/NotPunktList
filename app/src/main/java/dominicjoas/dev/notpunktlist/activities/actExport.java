package dominicjoas.dev.notpunktlist.activities;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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
import dominicjoas.dev.notpunktlist.classes.clsMarkList;
import dominicjoas.dev.notpunktlist.classes.clsXML;

public class actExport extends Activity {

    EditText txtPath, txtContent;
    RadioButton optTXT, optXML, optCSV;
    Button cmdExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actexport);

        txtPath = (EditText) findViewById(R.id.txtPath);
        txtContent = (EditText) findViewById(R.id.txtContent);

        optTXT = (RadioButton) findViewById(R.id.optExportToTXT);
        optXML = (RadioButton) findViewById(R.id.optExportToXML);
        optCSV = (RadioButton) findViewById(R.id.optExportToCSV);

        cmdExport = (Button) findViewById(R.id.cmdExport);

        txtPath.setText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());

        cmdExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clsMarkList list = new clsMarkList(Double.parseDouble(getIntent().getStringExtra("maxPoints")), !getIntent().getBooleanExtra("halfPoints", true), !getIntent().getBooleanExtra("quarterMarks", true));
                List<String> marklist = list.generateList();
                List<String> lsCorrect = new ArrayList<>();
                if(getIntent().getBooleanExtra("dictMode", true)) {
                    lsCorrect.add("Fehler             Note");
                    for(String current : marklist) {
                        String cur = String.valueOf(Double.parseDouble(getIntent().getStringExtra("maxPoints")) - Double.parseDouble(current.split(";")[0]));
                        for(int i = cur.length(); i<=20;i++) {
                            cur += " ";
                        }
                        lsCorrect.add(cur + ";" + current.split(";")[1]);
                    }
                } else {
                    lsCorrect.add("Punkte             Note");
                    for(String current : marklist) {
                        String cur = current.split(";")[0];
                        for(int i = cur.length(); i<=20;i++) {
                            cur += " ";
                        }
                        lsCorrect.add(cur + ";" + current.split(";")[1]);
                    }
                }
                String path = "";
                if(optTXT.isChecked()) {
                    try {
                        if(!new File(txtPath.getText().toString()).exists()) {
                            new File(txtPath.getText().toString()).mkdirs();
                        }
                        path = txtPath.getText().toString() + "/" + "export_" + new Date().getTime() + ".txt";
                        PrintWriter writer = new PrintWriter(path, "UTF-8");
                        writer.println("NotPunktList - Export");
                        writer.println("Settings");
                        writer.println("Maximale Punktzahl:\t\t" + getIntent().getStringExtra("maxPoints"));
                        writer.println("Viertel Noten:\t\t\t" + getIntent().getBooleanExtra("quarterMarks", true));
                        writer.println("Halbe Punkte:\t\t\t" + getIntent().getBooleanExtra("halfPoints", true));
                        writer.println("Diktat-Modus:\t\t\t" + getIntent().getBooleanExtra("dictMode", true));
                        for(String item : lsCorrect) {
                            writer.println(item);
                        }
                        writer.close();
                        CharSequence text = String.valueOf("Export in Datei '"+ path +"' erfolgreich!");
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    } catch(Exception ex) {
                        CharSequence text = String.valueOf("Fehler beim exportieren der Liste!");
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else if(optCSV.isChecked()) {
                    try {
                        if(!new File(txtPath.getText().toString()).exists()) {
                            new File(txtPath.getText().toString()).mkdirs();
                        }
                        path = txtPath.getText().toString() + "/" + "export_" + new Date().getTime() + ".csv";
                        PrintWriter writer = new PrintWriter(path, "UTF-8");
                        writer.println("NotPunktList - Export");
                        writer.println("Settings");
                        writer.println("Maximale Punktzahl;" + getIntent().getStringExtra("maxPoints"));
                        writer.println("Viertel Noten;" + getIntent().getBooleanExtra("quarterMarks", true));
                        writer.println("Halbe Punkte;" + getIntent().getBooleanExtra("halfPoints", true));
                        writer.println("Diktat-Modus;" + getIntent().getBooleanExtra("dictMode", true));
                        for(String item : lsCorrect) {
                            writer.println(item.replace(" ", ""));
                        }
                        writer.close();
                        CharSequence text = String.valueOf("Export in Datei '"+ path +"' erfolgreich!");
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    } catch(Exception ex) {
                        CharSequence text = String.valueOf("Fehler beim exportieren der Liste!");
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    try {
                        if(!new File(txtPath.getText().toString()).exists()) {
                            new File(txtPath.getText().toString()).mkdirs();
                        }
                        clsXML xml = new clsXML("notpunktlistexport");
                        path = txtPath.getText().toString() + "/" + "export_" + new Date().getTime() + ".xml";
                        Map<String, String> dict = new HashMap<>();
                        dict.put("maxpkt", getIntent().getStringExtra("maxPoints"));
                        dict.put("viertel_noten", String.valueOf(getIntent().getBooleanExtra("quarterMarks", true)));
                        dict.put("halbe_punkte", String.valueOf(getIntent().getBooleanExtra("halfPoints", true)));
                        dict.put("diktat_modus", String.valueOf(getIntent().getBooleanExtra("dictMode", true)));
                        xml.addElement("liste", dict);
                        String first = "", second="note";
                        for(String item : lsCorrect) {
                            if(item.contains(";")) {
                                dict = new HashMap<>();
                                dict.put(first, item.split(";")[0].trim());
                                dict.put(second, item.split(";")[1].trim());
                                xml.addSubElement("element", dict, "liste");
                            } else {
                                if(item.contains("Fehler")) {
                                    first = "fehler";
                                } else {
                                    first = "punkte";
                                }
                            }
                        }
                        xml.save(path);
                        CharSequence text = String.valueOf("Export in Datei '"+ path +"' erfolgreich!");
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    } catch(Exception ex) {
                        CharSequence text = String.valueOf("Fehler beim exportieren der Liste!");
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                try {
                    File file = new File(path);
                    StringBuilder txt = new StringBuilder();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        txt.append(line.replace(";", ":"));
                        txt.append('\n');
                    }
                    br.close();
                    txtContent.setText(txt.toString());
                } catch (IOException ex) {
                    CharSequence error = ex.toString();//String.valueOf("Fehler beim exportieren der Liste!");
                    Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menexport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menBack) {
            this.finish();
            return true;
        }
        if(id == R.id.menClose) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
}
