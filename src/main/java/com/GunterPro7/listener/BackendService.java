package com.GunterPro7.listener;

import com.GunterPro7.event.BackendRecievedEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BackendService implements Listener {
    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    private static BackendService instance;

    public static BackendService getInstance() {
        return instance;
    }

    public BackendService(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
        instance = this;
    }

    public void close() throws IOException {
        socket.close();
        instance = null;
    }

    public void run() {
        try {
            String packet;
            if (in.available() > 0) {
                packet = readPacket(in);
                if (!packet.isEmpty()) {
                    MinecraftForge.EVENT_BUS.post(new BackendRecievedEvent(packet));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readPacket(DataInputStream in) throws IOException {
        int available;
        try {
            available = in.available();
        } catch (IOException e) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < available; i++) {
            s.append((char) in.read());
        }
        return s.toString();
    }

    public void send(String message) throws IOException {
        out.writeUTF(message);
    }
}