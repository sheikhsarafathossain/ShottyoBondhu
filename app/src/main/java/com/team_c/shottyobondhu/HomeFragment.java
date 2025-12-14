package com.team_c.shottyobondhu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private androidx.activity.result.ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest> pickMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        // TODO: Handle the selected image URI (e.g., display it or process it)
                        android.util.Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        android.util.Log.d("PhotoPicker", "No media selected");
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the "Scan News Image" card and set a click listener
        View scanActionCard = view.findViewById(R.id.cv_scan_action);
        if (scanActionCard != null) {
            scanActionCard.setOnClickListener(v -> {
                // Launch the photo picker to let the user choose only images.
                pickMedia.launch(new androidx.activity.result.PickVisualMediaRequest.Builder()
                        .setMediaType(
                                androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            });
        }
    }
}