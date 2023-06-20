package org.astronomydatacompression;

import org.astronomydatacompression.session.Flow;
import org.astronomydatacompression.session.UserPathFlow;

public class Main {
    public static void main(String[] args) {
        Thread flowThread = new Thread(new Flow());
        flowThread.start();
    }

}