package com.eaglebk.sepro.testtcpmoxy;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class TCPService extends Service {

    Handler handler;
    Thread thread = null;

    public static final int SERVERPORT = 7000;
    public static final String SERVERIP = "192.1.168.1.16";

    final String LOG_TAG = "myLogs";

    private final IBinder iBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        TCPService getService(){
            return TCPService.this;
        }
    }

    public TCPService() {
//        super("tpcservice");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Start service");
        Log.d(LOG_TAG, "onCreate");
        handler = new Handler();

        thread = new Thread(new thread());
        thread.start();
    }

    class Thread1 implements Runnable{

        @Override
        public void run() {
            Socket socket = null;

            try {
                InetAddress serverAddress = InetAddress.getByName(SERVERIP);
                socket = new Socket(serverAddress, SERVERPORT);

                Thread2 commThread = new Thread(socket);
                new Thread1(commThread.start());
                return;
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    class Thread2 implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public Thread2(Socket clientSocket){
            this.clientSocket = clientSocket;

            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()){

                try {
                    String read = input.readLine();
                    if(read != null) {
                        handler.post(new updateThread(read));
                    }
                    else {
                        thread = new Thread(new Thread1());
                        thread.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        int tm = intent.getIntExtra("time", 0);
//        String label = intent.getStringExtra("label");
//        Log.d(LOG_TAG, "onHandleIntent start " + label);
//        try {
//            TimeUnit.SECONDS.sleep(tm);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.d(LOG_TAG, "onHandleIntent end " + label);
//    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(LOG_TAG, "onStartCommand");
//        return Service.START_NOT_STICKY;
//    }


//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.d(LOG_TAG, "onBind");
//        return null;
//    }

    public void sendCommand(String message){
//        TODO: написать функцию отправки сообщения по TCP
//        TCP.getTCP.send(message)
        System.out.println("New message: " + message);
    }

    class UpdateThread implements Runnable{
        private String msg;


        @Override
        public void run() {

        }
    }
}
