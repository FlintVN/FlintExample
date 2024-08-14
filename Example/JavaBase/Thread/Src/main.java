
public class main {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    System.out.println("Hello from thread1");
                    sleep(100);
                }
            }
        });

        while(true) {
            System.out.println("Hello from main");
            sleep(100);
        }
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        }
        catch(InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
