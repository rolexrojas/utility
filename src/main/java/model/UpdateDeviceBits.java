package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateDeviceBits {

    @JsonProperty("device-token")
    private String deviceToken;

    @JsonProperty("bits-device")
    private BitsDevice bitsDevice;


    public UpdateDeviceBits() { }

    public UpdateDeviceBits(String deviceToken,BitsDevice bitsDevice ) {
        this.deviceToken = deviceToken;
        this.bitsDevice = bitsDevice;
    }

}