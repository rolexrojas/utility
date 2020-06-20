package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryTwoBitsResponse {

    private Boolean bit0;
    private Boolean bit1;

    /**
     * The date of the last modification, in YYYY-MM format
     */
    @JsonProperty("last_update_time")
    private String lastUpdateTime;

    public Boolean getBit0() {
        return bit0;
    }

    public void setBit0(Boolean bit0) {
        this.bit0 = bit0;
    }

    public Boolean getBit1() {
        return bit1;
    }

    public void setBit1(Boolean bit1) {
        this.bit1 = bit1;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
