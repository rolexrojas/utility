package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryTwoBitsRequest {

    @JsonProperty("device_token")
    private String deviceToken;
    @JsonProperty("transaction_id")
    private String transactionId;
    private Long timestamp;

    public QueryTwoBitsRequest(){}

    public QueryTwoBitsRequest(String device_token, String transaction_id, Long timestamp) {
        this.deviceToken = device_token;
        this.transactionId = transaction_id;
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
}
