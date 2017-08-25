package com.example.myapplication;

/**
 * Created by Administrator on 2017/8/25.
 */

public class BluetoothData {

    private int start_Byte;

    private int high;

    private int low;

    private byte[] data;

    private byte cal;

    private byte[] end;

    private int index;

    private boolean isReadFinish;


    public int getStart_Byte() {
        return start_Byte;
    }

    public void setStart_Byte(int start_Byte) {
        this.start_Byte = start_Byte;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getCal() {
        return cal;
    }

    public void setCal(byte cal) {
        this.cal = cal;
    }

    public byte[] getEnd() {
        return end;
    }

    public void setEnd(byte[] end) {
        this.end = end;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isReadFinish() {
        return isReadFinish;
    }

    public void setReadFinish(boolean readFinish) {
        isReadFinish = readFinish;
    }
}
