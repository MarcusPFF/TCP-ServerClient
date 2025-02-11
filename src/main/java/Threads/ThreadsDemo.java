package Threads;

public class ThreadsDemo {
    public static void main(String[] args){
        new Thread(){
            @Override
            public void run() {
                System.out.println("Hello from thread");
            }
        }.start();
        new ThreadExtensions().start();
        try {
            ThreadExtensions TE1 = new ThreadExtensions();
            ThreadExtensions TE2 = new ThreadExtensions();
            ThreadExtensions TE3 = new ThreadExtensions();
            ThreadExtensions TE4 = new ThreadExtensions();
            TE1.start();
            TE2.start();
            TE3.start();
            TE4.start();
            TE1.join();
            TE2.join();
            TE3.join();
            TE4.join();
            new Thread(new ThreadRunnable()).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End of program");
    }

    private static class ThreadExtensions extends Thread{
        @Override
        public void run() {
            System.out.println("Message from thread: " + Thread.currentThread().getName());
        }
    }

    public static class ThreadRunnable implements Runnable{
        @Override
        public void run() {
            System.out.println("From Runnable Thread");
        }
    }
}
