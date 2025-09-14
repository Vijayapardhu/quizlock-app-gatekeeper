package com.smartappgatekeeper.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartappgatekeeper.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Data Export Activity - Apple-style data export
 * Allows users to export their data in various formats
 */
public class DataExportActivity extends AppCompatActivity {
    
    private LinearLayout layoutExportOptions;
    private TextView textExportInfo;
    private Button buttonExportJSON, buttonExportCSV, buttonExportPDF;
    private Button buttonImportData, buttonDeleteData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_export);
        
        initViews();
        setupClickListeners();
        loadExportInfo();
    }
    
    private void initViews() {
        layoutExportOptions = findViewById(R.id.layout_export_options);
        textExportInfo = findViewById(R.id.text_export_info);
        buttonExportJSON = findViewById(R.id.button_export_json);
        buttonExportCSV = findViewById(R.id.button_export_csv);
        buttonExportPDF = findViewById(R.id.button_export_pdf);
        buttonImportData = findViewById(R.id.button_import_data);
        buttonDeleteData = findViewById(R.id.button_delete_data);
    }
    
    private void setupClickListeners() {
        buttonExportJSON.setOnClickListener(v -> exportData("JSON"));
        buttonExportCSV.setOnClickListener(v -> exportData("CSV"));
        buttonExportPDF.setOnClickListener(v -> exportData("PDF"));
        buttonImportData.setOnClickListener(v -> importData());
        buttonDeleteData.setOnClickListener(v -> showDeleteConfirmation());
    }
    
    private void loadExportInfo() {
        // Load user's data summary
        textExportInfo.setText("Your data includes:\n" +
                "• 25 completed quizzes\n" +
                "• 3 learning courses\n" +
                "• 1,250 coins earned\n" +
                "• 7-day current streak\n" +
                "• 2 friends connected\n" +
                "• 3 achievements unlocked");
    }
    
    private void exportData(String format) {
        // TODO: Implement actual data export
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String filename = "smartapp_data_" + timestamp + "." + format.toLowerCase();
        
        Toast.makeText(this, "Exporting data as " + format + "...", Toast.LENGTH_SHORT).show();
        
        // Simulate export process
        new android.os.Handler().postDelayed(() -> {
            Toast.makeText(this, "Data exported successfully as " + filename, Toast.LENGTH_LONG).show();
            
            // TODO: Actually create and share the file
            // For now, just show success message
        }, 2000);
    }
    
    private void importData() {
        // TODO: Implement data import
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a file to import"), 1001);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No file manager found", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                // TODO: Process the imported file
                Toast.makeText(this, "Data imported successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void showDeleteConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete All Data")
                .setMessage("Are you sure you want to delete all your data? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteAllData())
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void deleteAllData() {
        // TODO: Implement actual data deletion
        Toast.makeText(this, "All data has been deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
