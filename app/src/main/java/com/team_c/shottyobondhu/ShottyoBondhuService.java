package com.team_c.shottyobondhu;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShottyoBondhuService extends AccessibilityService {

    private WindowManager windowManager;
    private View floatingView;
    private boolean isPopupShowing = false;

    // YOUR KEYWORDS HERE
    private String[] KEYWORDS = {"fake", "fakenews", "rumor", "lie", "hoax"};

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Toast.makeText(this, "ShottyoBondhu is watching!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) return;

        // Get the root node of the window content
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        // Scan the screen
        checkForKeywords(rootNode);
    }

    private void checkForKeywords(AccessibilityNodeInfo node) {
        if (node == null) return;

        // If this node has text, check it
        if (node.getText() != null) {
            String screenText = node.getText().toString().toLowerCase();

            for (String keyword : KEYWORDS) {
                if (screenText.contains(keyword)) {
                    showFloatingAlert();
                    return; // Stop checking once found
                }
            }
        }

        // Check children (recursive scan)
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            checkForKeywords(node.getChild(i));
        }
    }

    private void showFloatingAlert() {
        // Prevent showing multiple popups on top of each other
        if (isPopupShowing) return;

        // Run on UI thread just in case
        try {
            if (floatingView == null) {
                floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

                Button btnClose = floatingView.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(v -> removeFloatingAlert());
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            windowManager.addView(floatingView, params);
            isPopupShowing = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFloatingAlert() {
        if (isPopupShowing && floatingView != null) {
            windowManager.removeView(floatingView);
            isPopupShowing = false;
        }
    }

    @Override
    public void onInterrupt() {
        // Required method
    }
}