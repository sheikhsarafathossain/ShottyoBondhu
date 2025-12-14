package com.team_c.shottyobondhu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private TextView tvOverlayStatus, tvAccessibilityStatus, tvThemeStatus;
    private Button btnOverlay, btnAccessibility;
    private View cvThemeCard;
    private boolean isDarkMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvOverlayStatus = view.findViewById(R.id.tvOverlayStatus);
        tvAccessibilityStatus = view.findViewById(R.id.tvAccessibilityStatus);
        tvThemeStatus = view.findViewById(R.id.tv_theme_status);

        btnOverlay = view.findViewById(R.id.btnOverlay);
        btnAccessibility = view.findViewById(R.id.btnAccessibility);
        cvThemeCard = view.findViewById(R.id.cv_theme_card);

        // Load saved theme preference
        SharedPreferences prefs = getContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("isDarkMode", false);
        updateThemeText();

        btnOverlay.setOnClickListener(v -> requestOverlayPermission());
        btnAccessibility.setOnClickListener(v -> requestAccessibilityPermission());

        // Theme Toggle Logic
        cvThemeCard.setOnClickListener(v -> {
            isDarkMode = !isDarkMode;
            prefs.edit().putBoolean("isDarkMode", isDarkMode).apply();

            // Apply Theme
            if (isDarkMode) {
                androidx.appcompat.app.AppCompatDelegate
                        .setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                androidx.appcompat.app.AppCompatDelegate
                        .setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
            }
            updateThemeText();
        });

        return view;
    }

    private void updateThemeText() {
        if (isDarkMode) {
            tvThemeStatus.setText("Theme: Dark");
        } else {
            tvThemeStatus.setText("Theme: Light");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDashboard();
    }

    private void updateDashboard() {
        Context context = getContext();
        if (context == null)
            return;

        boolean isOverlayGranted = hasOverlayPermission();
        boolean isServiceOn = isAccessibilityServiceEnabled(context, ShottyoBondhuService.class);

        if (isOverlayGranted) {
            tvOverlayStatus.setText("Active");
            tvOverlayStatus.setTextColor(Color.GREEN);
            btnOverlay.setVisibility(View.GONE); // Hide button if granted
        } else {
            tvOverlayStatus.setText("Missing");
            tvOverlayStatus.setTextColor(Color.RED);
            btnOverlay.setVisibility(View.VISIBLE);
        }

        if (isServiceOn) {
            tvAccessibilityStatus.setText("Active");
            tvAccessibilityStatus.setTextColor(Color.GREEN);
            btnAccessibility.setText("SETTINGS");
        } else {
            tvAccessibilityStatus.setText("Inactive");
            tvAccessibilityStatus.setTextColor(Color.RED);
            btnAccessibility.setText("GRANT");
        }
    }

    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(getContext());
        }
        return true;
    }

    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> serviceClass) {
        String accessibilityServices = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (accessibilityServices == null)
            return false;
        String serviceName = context.getPackageName() + "/" + serviceClass.getName();
        String[] splitServices = accessibilityServices.split(":");
        for (String service : splitServices) {
            if (service.equalsIgnoreCase(serviceName))
                return true;
        }
        return false;
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getContext().getPackageName()));
            startActivity(intent);
        }
    }

    private void requestAccessibilityPermission() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }
}
