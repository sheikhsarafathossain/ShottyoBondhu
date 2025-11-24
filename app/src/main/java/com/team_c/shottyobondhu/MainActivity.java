package com.team_c.shottyobondhu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView tvSystemStatus;
    private TextView tvOverlayStatus;
    private TextView tvAccessibilityStatus;
    private Button btnOverlay;
    private Button btnAccessibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        tvSystemStatus = findViewById(R.id.tvSystemStatus);
        tvOverlayStatus = findViewById(R.id.tvOverlayStatus);
        tvAccessibilityStatus = findViewById(R.id.tvAccessibilityStatus);
        btnOverlay = findViewById(R.id.btnOverlay);
        btnAccessibility = findViewById(R.id.btnAccessibility);

        // Set Click Listeners
        btnOverlay.setOnClickListener(v -> requestOverlayPermission());
        btnAccessibility.setOnClickListener(v -> requestAccessibilityPermission());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // We check permissions every time the app comes into view
        // to update the dashboard immediately after the user changes settings.
        updateDashboard();
    }

    private void updateDashboard() {
        boolean isOverlayGranted = hasOverlayPermission();
        boolean isServiceOn = isAccessibilityServiceEnabled(this, ShottyoBondhuService.class);

        // Update Overlay Card
        if (isOverlayGranted) {
            tvOverlayStatus.setText("[ GRANTED ]");
            tvOverlayStatus.setTextColor(Color.parseColor("#00FF00")); // Cyber Green
            btnOverlay.setEnabled(false);
            btnOverlay.setAlpha(0.5f);
            btnOverlay.setText("OK");
        } else {
            tvOverlayStatus.setText("[ MISSING ]");
            tvOverlayStatus.setTextColor(Color.parseColor("#FF4444"));
            btnOverlay.setEnabled(true);
            btnOverlay.setAlpha(1.0f);
            btnOverlay.setText("GRANT");
        }

        // Update Accessibility Card
        if (isServiceOn) {
            tvAccessibilityStatus.setText("[ ACTIVE ]");
            tvAccessibilityStatus.setTextColor(Color.parseColor("#00FF00"));
            // The button now acts as a "Disable" shortcut
            btnAccessibility.setText("DISABLE");
        } else {
            tvAccessibilityStatus.setText("[ INACTIVE ]");
            tvAccessibilityStatus.setTextColor(Color.parseColor("#FF4444"));
            btnAccessibility.setText("ENABLE");
        }

        // Update Main System Status
        if (isOverlayGranted && isServiceOn) {
            tvSystemStatus.setText("ONLINE");
            tvSystemStatus.setTextColor(Color.parseColor("#00FF00")); // Green
            tvSystemStatus.setShadowLayer(20, 0, 0, Color.parseColor("#00FF00"));
        } else {
            tvSystemStatus.setText("OFFLINE");
            tvSystemStatus.setTextColor(Color.parseColor("#FF4444")); // Red
            tvSystemStatus.setShadowLayer(20, 0, 0, Color.parseColor("#FF0000"));
        }
    }

    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true; // Pre-M devices usually grant this by default
    }

// Helper to check if the Accessibility Service is actually running
    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> serviceClass) {
        String accessibilityServices = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        if (accessibilityServices == null) return false;

        String serviceName = context.getPackageName() + "/" + serviceClass.getName();

        // Simple standard Java split instead of the complex TextUtils one
        String[] splitServices = accessibilityServices.split(":");

        for (String service : splitServices) {
            if (service.equalsIgnoreCase(serviceName)) {
                return true;
            }
        }
        return false;
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void requestAccessibilityPermission() {
        Toast.makeText(this, "Find 'ShottyoBondhu' and toggle it.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }
}