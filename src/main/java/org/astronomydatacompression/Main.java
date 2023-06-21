package org.astronomydatacompression;

import org.astronomydatacompression.session.Flow;

public class Main {
    public static void main(String[] args) {
        Thread flowThread = new Thread(new Flow());
        flowThread.start();
    }

}