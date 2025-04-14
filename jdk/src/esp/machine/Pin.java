package esp.machine;

import flint.machine.PinController;

public class Pin extends PinController {
    final int pin;

    private static native void setMode(int pin, int mode);
    private static native boolean readPin(int pin);
    private static native void writePin(int pin, boolean level);

    public Pin(int pin) {
        this.pin = pin;
    }

    @SuppressWarnings("this-escape")
    public Pin(int pin, PinMode mode) {
        this.pin = pin;
        setMode(mode);
    }

    public Pin setMode(PinMode mode) {
        int m = switch(mode) {
            case INPUT -> 0;
            case OUTPUT -> 1;
            case INPUT_PULL_UP -> 2;
            case INPUT_PULL_DOWN -> 3;
            default -> 4;
        };
        Pin.setMode(pin, m);
        return this;
    }

    @Override
    public boolean read() {
        return Pin.readPin(pin);
    }

    @Override
    public void write(boolean level) {
        Pin.writePin(pin, level);
    }

    @Override
    public void set() {
        Pin.writePin(pin, true);
    }

    @Override
    public void reset() {
        Pin.writePin(pin, false);
    }
}
