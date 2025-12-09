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
    private View floatingAlertView;
    private View floatingHighlighterView;
    private boolean isAlertShowing = false;


    private String[] KEYWORDS = {"fake", "fakenews", "rumor", "viral", "viralnews", "Yasin Sazid"};

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Toast.makeText(this, "ShottyoBondhu is watching!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (isAlertShowing) return;

        if (event == null) return;

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        checkForKeywords(rootNode);
    }

    private void checkForKeywords(AccessibilityNodeInfo node) {
        if (node == null) return;

        if (isAlertShowing) return;

        if (node.getText() != null) {
            String screenText = node.getText().toString().toLowerCase();

            for (String keyword : KEYWORDS) {
                if (screenText.contains(keyword)) {

                    Rect location = new Rect();
                    node.getBoundsInScreen(location);

                    showHighlighter(location);

                    showFixedAlert();

                    return;
                }
            }
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            checkForKeywords(node.getChild(i));
        }
    }

    private void showHighlighter(Rect location) {
        try {
            if (floatingHighlighterView != null) {
                windowManager.removeView(floatingHighlighterView);
            }

            floatingHighlighterView = LayoutInflater.from(this).inflate(R.layout.layout_highlighter, null);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    location.width(),
                    location.height(),
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = location.left;
            params.y = location.top;

            windowManager.addView(floatingHighlighterView, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showFixedAlert() {
        if (isAlertShowing) return;

        try {
            floatingAlertView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
            Button btnClose = floatingAlertView.findViewById(R.id.btnClose);

            btnClose.setOnClickListener(v -> removeAllAlerts());

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            params.y = 100;

            windowManager.addView(floatingAlertView, params);



            isAlertShowing = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeAllAlerts() {
        try {
            if (isAlertShowing && floatingAlertView != null) {
                windowManager.removeView(floatingAlertView);
            }
            if (floatingHighlighterView != null) {
                windowManager.removeView(floatingHighlighterView);
                floatingHighlighterView = null;
            }


            isAlertShowing = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {}
}