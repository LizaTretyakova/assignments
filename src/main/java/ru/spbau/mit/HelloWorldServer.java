package ru.spbau.mit;


public class HelloWorldServer implements Server {

    @Override
    public void accept(final Connection connection) {
    //    throw new UnsupportedOperationException("TODO: implement");
        Thread processConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                connection.send("Hello world");
                connection.close();
            }
        });
        processConnection.start();
    }
}
