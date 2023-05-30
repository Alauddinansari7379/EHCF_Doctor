package com.example.ehcf_doctor.AyuSynk.utils;

import android.content.Context;

import java.io.File;

public class GenUtil {

    public static File getSaveDir(String id, Context context) {
        File base = new File(context.getExternalFilesDir(null).getAbsolutePath(), String.format("%s%s%s",
                "AyuData",
                File.separator,
                id));

        if(!base.getParentFile().exists()){
            base.getParentFile().mkdir();
        }
        if (!base.exists()) {
            base.mkdir();
        }

        File audioDir = new File(base, "audio");

        if (!audioDir.exists()) {
            audioDir.mkdir();
        }

        return audioDir;
    }

}
