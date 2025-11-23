package com.team_c.shottyobondhu;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;

public class ShottyoBondhuService extends AccessibilityService {

    private WindowManager windowManager;
    private View floatingAlertView;       // The "Fake News Detected" popup (Fixed)
    private View floatingHighlighterView; // The Red Box (Moves to text)
    private boolean isAlertShowing = false;

    // YOUR KEYWORDS HERE - Add more words as needed
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
                    // 1. We found the keyword!

                    // 2. Get the exact location of this text on the screen
                    Rect location = new Rect();
                    node.getBoundsInScreen(location);

                    // 3. Show the Red Box specifically around that text
                    showHighlighter(location);

                    // 4. Show the Fixed "Fake News" Popup at the top
                    showFixedAlert();

                    return; // Stop checking so we don't spam the screen
                }
            }
        }

        // Check children (recursive scan to find text inside layouts)
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            checkForKeywords(node.getChild(i));
        }
    }

    // --- LOGIC FOR THE RED BOX (Highlighter) ---
    private void showHighlighter(Rect location) {
        try {
            // Remove old box if it exists so we can move it to the new location
            if (floatingHighlighterView != null) {
                windowManager.removeView(floatingHighlighterView);
            }

            floatingHighlighterView = LayoutInflater.from(this).inflate(R.layout.layout_highlighter, null);

            // Create params based on the EXACT size of the text found
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    location.width(),   // Width of the text
                    location.height(),  // Height of the text
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    // FLAG_NOT_TOUCHABLE is crucial so you can still click the text underneath!
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = location.left; // Exact X position of text
            params.y = location.top;  // Exact Y position of text

            windowManager.addView(floatingHighlighterView, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- LOGIC FOR THE MESSAGE POPUP (Fixed Position) ---
    private void showFixedAlert() {
        // If the alert is already showing, don't create another one
        if (isAlertShowing) return;

        try {
            floatingAlertView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
            Button btnClose = floatingAlertView.findViewById(R.id.btnClose);

            // Close button clears BOTH the message and the red box
            btnClose.setOnClickListener(v -> removeAllAlerts());

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            // Fix position to Top Center
            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            params.y = 100; // 100 pixels down from the top

            windowManager.addView(floatingAlertView, params);
            isAlertShowing = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeAllAlerts() {
        try {
            // Remove the Message
            if (isAlertShowing && floatingAlertView != null) {
                windowManager.removeView(floatingAlertView);
                isAlertShowing = false;
            }
            // Remove the Red Box
            if (floatingHighlighterView != null) {
                windowManager.removeView(floatingHighlighterView);
                floatingHighlighterView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {
        // Required method for Accessibility Service
    }
}