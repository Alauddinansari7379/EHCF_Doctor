package com.example.ehcf_doctor.AyuSynk.bluetooth.ota;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ayudevice.ayusynksdk.AyuSynk;
import com.ayudevice.ayusynksdk.ble.listener.AyuDeviceUpdateListener;
import com.example.ehcf_doctor.R;
import com.example.ehcf_doctor.databinding.FragmentOtaBinding;

public class OtaUpdateFragment extends Fragment implements View.OnClickListener, AyuDeviceUpdateListener {

    FragmentOtaBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOtaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnUpdate.setText(R.string.check_update);
        binding.btnUpdate.setOnClickListener(this);
        binding.btnUpdate.setTag(1);
        if (AyuSynk.getBleInstance().isTestOtaEnabled())
            binding.cbTestOta.setChecked(true);
        binding.cbTestOta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AyuSynk.getBleInstance().enableOTAForTesting(isChecked);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AyuSynk.getBleInstance().setAyuDeviceUpdateListener(this);
        updateInProgress(AyuSynk.getBleInstance().isDeviceUpdateInProgress());
    }

    @Override
    public void onStop() {
        super.onStop();
        AyuSynk.getBleInstance().setAyuDeviceUpdateListener(null);
    }

    @Override
    public void onClick(View v) {
        if ((int) binding.btnUpdate.getTag() == 3) {
            AyuSynk.getBleInstance().cancelDeviceUpdate();
            updateInProgress(false);
        } else if ((int) binding.btnUpdate.getTag() == 2) {
            AyuSynk.getBleInstance().updateDevice();

            binding.pbApi.setVisibility(View.VISIBLE);
        } else {
            AyuSynk.getBleInstance().checkForDeviceUpdate();
            binding.pbApi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isNewUpdateAvailable(boolean hasUpdate, String message) {
        binding.pbApi.setVisibility(View.GONE);
        if (hasUpdate) {
            binding.btnUpdate.setText(R.string.device_update);
            binding.btnUpdate.setTag(2);
            binding.message.setText(getString(R.string.ota_message, message));
        } else {
            binding.btnUpdate.setText(R.string.check_update);
            binding.btnUpdate.setTag(1);
            binding.message.setText(R.string.no_updates);
        }
    }

    @Override
    public void onUpdateStarted() {
        updateInProgress(true);
        binding.tvUploadProgress.setText(R.string.upload_started);
        binding.pbApi.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateProgressChange(int progress) {
        binding.tvUploadProgress.setText(getString(R.string.upload_progress, progress));
        binding.progressBarUpdate.setProgress(progress);
    }

    @Override
    public void onUpdateComplete() {
        updateInProgress(false);
        binding.tvUploadProgress.setText(R.string.upload_complete);
        Toast.makeText(getContext(), "Ota completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateError(String error) {
        updateInProgress(false);
        binding.tvUploadProgress.setText(getString(R.string.upload_error, error));
        binding.pbApi.setVisibility(View.GONE);
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void updateInProgress(boolean inProgress) {
        if (inProgress) {
            binding.btnUpdate.setText(R.string.cancel_update);
            binding.btnUpdate.setTag(3);
            binding.grpProgress.setVisibility(View.VISIBLE);
        } else {
            binding.btnUpdate.setText(R.string.check_update);
            binding.btnUpdate.setTag(1);
            binding.grpProgress.setVisibility(View.GONE);
        }
    }
}
