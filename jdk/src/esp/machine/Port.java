package esp.machine;

import flint.machine.PortController;

public class Port extends PortController {
    private final byte[] pins;

    private static native void setMode(byte[] pins, int mode);
    private static native int readPort(byte[] pins);
    private static native void writePort(byte[] pins, int value);

    public Port(byte... pins) {
        if(pins == null || (pins.length < 1) || (pins.length > 32)) {
            if(pins == null)
                throw new NullPointerException("pins array cannot be null object");
            else
                throw new NullPointerException("The pin number must be from 1 to 32");
        }
        byte[] tmp = new byte[pins.length];
        System.arraycopy(pins, 0, tmp, 0, pins.length);
        this.pins = tmp;
    }

    public Port(int... pins) {
        if(pins == null || (pins.length < 1) || (pins.length > 32)) {
            if(pins == null)
                throw new NullPointerException("pins array cannot be null object");
            else
                throw new NullPointerException("The pin number must be from 1 to 32");
        }
        byte[] tmp = new byte[pins.length];
        for(int i = 0; i < pins.length; i++)
            tmp[i] = (byte)pins[i];
        this.pins = tmp;
    }

    public Port(Pin... pins) {
        if(pins == null || (pins.length < 1) || (pins.length > 32)) {
            if(pins == null)
                throw new NullPointerException("pins array cannot be null object");
            else
                throw new NullPointerException("The pin number must be from 1 to 32");
        }
        byte[] pinArray = new byte[pins.length];
        for(int i = 0; i < pins.length; i++)
            pinArray[i] = (byte)pins[i].pin;
        this.pins = pinArray;
    }

    public Port setMode(PinMode mode) {
        int m = switch(mode) {
            case INPUT -> 0;
            case OUTPUT -> 1;
            case INPUT_PULL_UP -> 2;
            case INPUT_PULL_DOWN -> 3;
            default -> 4;
        };
        Port.setMode(pins, m);
        return this;
    }

    @Override
    public int read() {
        return Port.readPort(pins);
    }

    @Override
    public void write(int value) {
        Port.writePort(pins, value);
    }

    @Override
    public void reset() {
        Port.writePort(pins, 0);
    }
}
