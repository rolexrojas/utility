package model;


import com.fasterxml.jackson.annotation.JsonProperty;


public class BitsDevice {

    /**
     * The value of the first bit
     */
    @JsonProperty("bit-one")
    private Boolean bitOne;

    /**
     * The value of the second bit
     */
    @JsonProperty("bit-two")
    private Boolean bitTwo;

    /**
     * The date of the last modification, in YYYY-MM format
     */
    @JsonProperty("last_update_time")
    private String lastUpdateTime;

    public BitsDevice() {
    }

    public BitsDevice(Boolean bitOne, Boolean bitTwo) {
        this.bitOne = bitOne;
        this.bitTwo = bitTwo;
    }

    public BitsDevice(Boolean bitOne, Boolean bitTwo, String lastUpdateTime) {
        this.bitOne = bitOne;
        this.bitTwo = bitTwo;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getBitOne() {
        return bitOne;
    }

    public void setBitOne(Boolean bitOne) {
        this.bitOne = bitOne;
    }

    public Boolean getBitTwo() {
        return bitTwo;
    }

    public void setBitTwo(Boolean bitTwo) {
        this.bitTwo = bitTwo;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
