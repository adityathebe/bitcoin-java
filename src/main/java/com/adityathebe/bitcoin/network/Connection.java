package com.adityathebe.bitcoin.network;

import java.io.*;
import java.net.Socket;

import static com.adityathebe.bitcoin.crypto.Crypto.hash256;
import static com.adityathebe.bitcoin.utils.Utils.*;

public class Connection {
    private static String magic = "f9beb4d9";

    private static String getMsg(String cmd, String payload) {
        while (payload.length() < 8) {
            payload += "0";
        }
        String payloadSize = Integer.toHexString(payload.length() / 2);

        String checkSum = bytesToHex(hash256(payload)).substring(0, 8);

        // Fix Command length to 12 bytes
        while (cmd.length() < 24) {
            cmd += "0";
        }
        return magic + cmd + payloadSize + checkSum + payload;
    }

    private static String getPingMsg() {
        return getMsg("70696e67", "abcdef");
    }

    private static String getVersionMsg() {
        String versionNum = "62EA0000"; // little-endian
        String services = "0100000000000000"; // little-endian
        String timestamp = bytesToHex(changeEndian(longToBytes(System.currentTimeMillis() / 1000L)));
        String addr_recv = "";
        String addr_from = "";
        String nonce = "DD9D202C3AB45713";
        String sub_version_num = "00";
        String start_height = "00000000";
        String payload = versionNum + services + timestamp + addr_recv + addr_from + nonce + sub_version_num + start_height;
        return getMsg("76657273696F6E0000000000", payload);
    }

    public static void main(String[] args) throws IOException {
        String hostname = "47.254.169.60";
        int port = 8333;
        Socket socket = new Socket(hostname, port);
        System.out.println(getVersionMsg());

        OutputStream out = socket.getOutputStream();
        byte[] data = hexToBytes(getPingMsg());
        out.write(data);

        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
