package com.example.ehcf_doctor.AyuSynk.bluetooth.connect;

public class DeviceListObject {
    private String name;
    private String bleAddress;

    public DeviceListObject(String name, String bleAddress) {
        this.name = name;
        this.bleAddress = bleAddress;
    }

    public String getName() {
        return name;
    }

    public String getBleAddress() {
        return bleAddress;
    }

}

