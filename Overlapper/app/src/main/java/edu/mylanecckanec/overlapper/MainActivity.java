package edu.mylanecckanec.overlapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private EditText firstUrlEditText;
    private EditText secondUrlEditText;
    private Spinner firstUrlSpinner;
    private Spinner secondUrlSpinner;
    private Button firstUrlSaveButton;
    private Button secondUrlSaveButton;
    private Button combineButton;
    private SharedPreferences savedValues;
    private Map<String, ?> savedUrlStorageMap;
    private Map<String, String> savedUrlMap;
    public static final String FIRST_URL = "firstUrl";
    public static final String SECOND_URL = "secondUrl";
    public static final int REQ_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstUrlEditText = (EditText) findViewById(R.id.firstUrlEditText);
        secondUrlEditText = (EditText) findViewById(R.id.secondUrlEditText);
        firstUrlSpinner = (Spinner) findViewById(R.id.firstUrlSpinner);
        secondUrlSpinner = (Spinner) findViewById(R.id.secondUrlSpinner);
        firstUrlSaveButton = (Button) findViewById(R.id.firstUrlSaveButton);
        secondUrlSaveButton = (Button) findViewById(R.id.secondUrlSaveButton);
        combineButton = (Button) findViewById(R.id.combineButton);

        savedUrlMap = new HashMap<String, String>();
        savedValues = getSharedPreferences("SavedValues",MODE_PRIVATE);

        updateSavedUrls();
        renderSavedUrls();
    }

    @Override
    public void onPause() {
        Editor editor = savedValues.edit();

        String firstUrl = firstUrlEditText.getText().toString();
        String secondUrl = secondUrlEditText.getText().toString();

        if (!firstUrl.isEmpty())
            editor.putString("firstUrl",firstUrl);
        if (!secondUrl.isEmpty())
            editor.putString("secondUrl",secondUrl);

        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        firstUrlEditText.setText(savedValues.getString("firstUrl",""));
        firstUrlEditText.setText(savedValues.getString("secondUrl",""));
    }

    public void saveUrlClick(final View button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a URL name");

        final EditText nameEditText = new EditText(this);
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(nameEditText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String urlName = nameEditText.getText().toString();
                String url;
                if (button.getId() == R.id.firstUrlSaveButton) {
                    url = firstUrlEditText.getText().toString();
                }
                else {
                    url = secondUrlEditText.getText().toString();
                }
                saveUrl(urlName,url);
                updateSavedUrls();
                renderSavedUrls();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void saveUrl(String name, String url){
        Editor editor = savedValues.edit();
        editor.putString(name,url);
        editor.commit();
    }

    public void renderSavedUrls() {
        List<String> urlNames = new ArrayList<>(savedUrlMap.keySet());
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, urlNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        firstUrlSpinner.setAdapter(adapter);
        firstUrlSpinner.setOnItemSelectedListener(this);
        secondUrlSpinner.setAdapter(adapter);
        secondUrlSpinner.setOnItemSelectedListener(this);
    }

    public void updateSavedUrls(){
        //savedUrlStorageMap.clear();
        savedUrlStorageMap = savedValues.getAll();
        for (Map.Entry<String,?> item : savedUrlStorageMap.entrySet()) {
            savedUrlMap.put(item.getKey(),item.getValue().toString());
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String urlName = parent.getItemAtPosition(pos).toString();
        String url = savedValues.getString(urlName, "no URL found");//savedUrlMap.get(urlName);

        if (parent.getId() == R.id.firstUrlSpinner) {
            firstUrlEditText.setText(url);
        } else {
            secondUrlEditText.setText(url);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    public void combineClick(View view) {
        String firstUrl = firstUrlEditText.getText().toString();
        String secondUrl = secondUrlEditText.getText().toString();
        if (!firstUrl.isEmpty() && !secondUrl.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(FIRST_URL, firstUrl);
            intent.putExtra(SECOND_URL, secondUrl);
            startActivityForResult(intent,REQ_ID);
        }
    }
}
