package com.multistreamcammc.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class ObsDetector {
    private ObsDetector() {}

    public static boolean isObsWebsocketReachable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 4455), 200);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
