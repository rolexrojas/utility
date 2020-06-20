package model;

public class QueryTwoBitsDeviceCheckResponseType {

    private BitsDevice bitsDevice;

    public QueryTwoBitsDeviceCheckResponseType(BitsDevice bitsDevice) {
        this.bitsDevice = bitsDevice;
    }

    public QueryTwoBitsDeviceCheckResponseType( ) { }

    public BitsDevice getBitsDevice() {
        return bitsDevice;
    }

    public void setBitsDevice(BitsDevice bitsDevice) {
        this.bitsDevice = bitsDevice;
    }


}