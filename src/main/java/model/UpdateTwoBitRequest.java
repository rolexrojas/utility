package model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UpdateTwoBitRequest {

    @JsonProperty("device_token")
    private String deviceToken;
    @JsonProperty("transaction_id")
    private String transactionId;
    private Long timestamp;
    private Boolean bit0;
    private Boolean bit1;

    public UpdateTwoBitRequest() {
    }

    public UpdateTwoBitRequest(String deviceToken, String transactionId, Long timestamp, Boolean bit0, Boolean bit1) {
        this.deviceToken = deviceToken;
        this.bit0 = bit0;
        this.bit1 = bit1;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

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
}
