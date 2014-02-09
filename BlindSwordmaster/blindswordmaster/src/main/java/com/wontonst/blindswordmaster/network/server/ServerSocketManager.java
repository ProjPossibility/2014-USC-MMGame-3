package com.wontonst.blindswordmaster.network.server;

import com.wontonst.blindswordmaster.network.SocketManagerBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by roycr_000 on 2/8/14.
 */
public class ServerSocketManager extends SocketManagerBase {

    public ServerSocketManager() {
    }

    public void connect(ServerSocket serverSocket) {
        try {
            this.socket = serverSocket.accept();
            this.connectRoutine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
