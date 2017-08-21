package com.eaglebk.sepro.testtcpmoxy;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eaglebk.sepro.testtcpmoxy.ui.activity.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import static android.R.id.message;
import static android.content.ContentValues.TAG;

public class TCPService extends Service {

    final int NEW_MESSAGE = 1; // загрузка началась

    Handler uiHandler;
    Handler sendMsgHandler;
    Thread Thread1 = null;
    Handler h;
    public static final int SERVERPORT = 7000;
    public static final String SERVERIP = "192.168.1.16";


    final String LOG_TAG = "myLogs";

    private final IBinder iBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        TCPService getService() {
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
        uiHandler = new Handler();
        sendMsgHandler = new Handler();

        this.Thread1 = new Thread(new Thread1());
        this.Thread1.start();
    }

    class Thread1 implements Runnable {

        @Override
        public void run() {
            Socket socket = null;
            try {
                InetAddress serInetAddress = InetAddress.getByName(SERVERIP);
                socket = new Socket(serInetAddress, SERVERPORT);
                socket.setSoTimeout(2000);

                Thread2 commThread = new Thread2(socket);
                new Thread(commThread).start();

                Thread3 sendThread = new Thread3(socket);
                new Thread(sendThread).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class Thread3 implements Runnable {

        private Socket clientSocket;


        PrintWriter output;

        public Thread3(Socket clientSocket) {
            this.clientSocket = clientSocket;

            try {
                this.output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream())), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            Looper.prepare();
            h = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == NEW_MESSAGE) {
                        if (output != null && !output.checkError()) {
                            if(clientSocket.isBound() && clientSocket.isConnected()) {
                                output.println((String) msg.obj);
                                output.flush();
                                Log.d(TAG, "Sent Message: " + message);
                            } else {
                                Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                                intent.putExtra(MainActivity.PARAM_STATUS, "Error connection");
                                sendBroadcast(intent);
                            }
                        }
                    }
                }
            };
            Looper.loop();


        }
    }

    class Thread2 implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public Thread2(Socket clientSocket) {
            this.clientSocket = clientSocket;

            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    String read = input.readLine();
                    if (read != null) {
                        uiHandler.post(new UpdateThread(read));
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        class UpdateThread implements Runnable {

            private String msg;

            public UpdateThread(String str) {
                this.msg = str;
            }

            @Override
            public void run() {
                System.out.println("Client says: " + msg);
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

    public void sendCommand(String message) {


        System.out.println("New message: " + message);

        Thread t = new Thread(new Runnable() {
            Message msg;
            @Override
            public void run() {
                msg = h.obtainMessage(NEW_MESSAGE, message);
                h.sendMessage(msg);
            }
        }
        );
        t.start();

        Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
        intent.putExtra(MainActivity.PARAM_STATUS, message);
        sendBroadcast(intent);
    }
}

