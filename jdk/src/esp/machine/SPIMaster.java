package esp.machine;

import java.io.IOException;
import flint.machine.Communication;

public class SPIMaster extends Communication {
    private int handle;
    private int spi;
    private int speed;
    private int maxTransferSize;
    private boolean cpol;
    private boolean cpha;
    private boolean isLsb;
    private boolean csLevel;
    private byte[] spiPins;
    private int rxIndex;
    private int rxCount;
    private byte[] rxBuff;

    private static final int DEFAULT_MAX_TRANSFER_SIZE = 4096;
    private static final int DEFAULT_RX_BUFFER_SIZE = 1024;

    private static native int open(int spiId, int speed, int maxTranferSize, byte[] pins, int mode, boolean isLsb, boolean csLevel);
    private static native int getActualSpeed(int handle);
    private static native int getDefaultMosiPin(int spiId);
    private static native int getDefaultMisoPin(int spiId);
    private static native int getDefaultClkPin(int spiId);
    private static native boolean isOpen(int spiId);
    private static native boolean write(int handle, byte[] txBuff, int txOffset, byte[] rxBuff, int rxOffset, int length);
    private static native void close(int handle);

    private static int getSpiId(SPIDevice spi) {
        return switch(spi) {
            case SPI1 -> 0;
            case SPI2 -> 1;
            default -> 2;
        };
    }

    public SPIMaster(SPIDevice spi) {
        this.spi = getSpiId(spi);
        this.speed = 5000000;
        this.maxTransferSize = DEFAULT_MAX_TRANSFER_SIZE;
        this.rxBuff = new byte[DEFAULT_RX_BUFFER_SIZE];
        this.spiPins = new byte[4];
        this.spiPins[0] = (byte)getDefaultMosiPin(this.spi);
        this.spiPins[1] = (byte)getDefaultMisoPin(this.spi);
        this.spiPins[2] = (byte)getDefaultClkPin(this.spi);
        this.spiPins[3] = -1;
    }

    public boolean open() {
        int mode = (cpol ? 2 : 0) | (cpha ? 1 : 0);
        handle = open(spi, speed, maxTransferSize, spiPins, mode, isLsb, csLevel);
        return handle != 0;
    }

    public void close() {
        if(handle != 0)
            close(handle);
    }

    private void checkStateBeforeChange() {
        if(isOpen())
            throw new IllegalStateException();
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getActualSpeed() {
        if(!isOpen())
            throw new IllegalStateException();
        return getActualSpeed(handle);
    }

    public int getMaxTransferSize() {
        return this.maxTransferSize;
    }

    public void setMaxTransferSize(int value) {
        checkStateBeforeChange();
        this.maxTransferSize = value;
    }

    public void setSpeed(int speed) {
        checkStateBeforeChange();
        this.speed = speed;
    }

    public boolean getCpol() {
        return this.cpol;
    }

    public void setCpol(boolean value) {
        checkStateBeforeChange();
        this.cpol = value;
    }

    public boolean getCpha() {
        return this.cpha;
    }

    public boolean isMsb() {
        return !this.isLsb;
    }

    public boolean isMsb(boolean isMsb) {
        this.isLsb = !isMsb;
        return isMsb;
    }

    public boolean getCsPinActiveLevel() {
        return this.csLevel;
    }

    public void setCsPinActiveLevel(boolean level) {
        checkStateBeforeChange();
        this.csLevel = level;
    }

    public void setCpha(boolean value) {
        checkStateBeforeChange();
        this.cpha = value;
    }

    public int getMosiPin() {
        return this.spiPins[0];
    }

    public void setMosiPin(int pin) {
        checkStateBeforeChange();
        this.spiPins[0] = (byte)pin;
    }

    public int getMisoPin() {
        return this.spiPins[1];
    }

    public void setMisoPin(int pin) {
        checkStateBeforeChange();
        this.spiPins[1] = (byte)pin;
    }

    public int getClkPin() {
        return this.spiPins[2];
    }

    public void setClkPin(int pin) {
        checkStateBeforeChange();
        this.spiPins[2] = (byte)pin;
    }

    public int getCsPin() {
        return this.spiPins[3];
    }

    public void setCsPin(int pin) {
        checkStateBeforeChange();
        this.spiPins[3] = (byte)pin;
    }

    public int getRxBufferSize() {
        return this.rxBuff.length;
    }

    public void setRxBufferSize(int size) {
        checkStateBeforeChange();
        this.rxBuff = (size != 0) ? new byte[size] : null;
    }

    public boolean isOpen() {
        if(handle != 0) {
            boolean ret = isOpen(spi);
            if(!ret)
                handle = 0;
            return ret;
        }
        return false;
    }

    @Override
    public void write(byte[] buffer, int offset, int count) {
        byte[] rxBuff = this.rxBuff;
        if(rxBuff != null && count > rxBuff.length && getMisoPin() > 0)
            throw new IllegalArgumentException("Receive buffer size is insufficient");
        this.rxIndex = 0;
        this.rxCount = 0;
        if(!write(handle, buffer, offset, rxBuff, 0, count))
            throw new IllegalStateException("Error while transmitting data");
        this.rxCount = (rxBuff != null) ? count : 0;
    }

    @Override
    public int bytesToRead() {
        return rxCount - rxIndex;
    }

    @Override
    public int read(byte[] buffer, int offset, int count) {
        if((offset | count) < 0 || (count > (buffer.length - offset)))
            throw new IndexOutOfBoundsException();
        int remaining = bytesToRead();
        count = (remaining < count) ? remaining : count;
        if(count == 0)
            return 0;
        System.arraycopy(rxBuff, rxIndex, buffer, offset, count);
        rxIndex += count;
        return count;
    }
}
