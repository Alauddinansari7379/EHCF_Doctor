package com.example.ehcf_doctor.AyuSynk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ehcf_doctor.R;
import com.example.ehcf_doctor.databinding.FragmentSelectModuleBinding;

public class SelectModuleFragment extends Fragment {

    FragmentSelectModuleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectModuleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBle.setOnClickListener(v -> NavHostFragment.findNavController(SelectModuleFragment.this)
                .navigate(R.id.action_SelectModuleFragment_to_BLEFragment));

        binding.btnUsb.setOnClickListener(v -> NavHostFragment.findNavController(SelectModuleFragment.this)

                .navigate(R.id.action_SelectModuleFragment_to_USBFragment));
    }
}
