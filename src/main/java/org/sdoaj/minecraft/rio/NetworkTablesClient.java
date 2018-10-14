package org.sdoaj.minecraft.rio;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;

public class NetworkTablesClient {
    private NetworkTable table;

    private static NetworkTablesClient instance = new NetworkTablesClient();
    private NetworkTablesClient() {};

    public static NetworkTablesClient getInstance() {
        return instance;
    }

    public void run() {
        NetworkTableInstance instance = NetworkTableInstance.getDefault();
        instance.startClientTeam(254);
        instance.startDSClient(); // gets robot IP from driver station

        table = instance.getTable("minecraft");

        while (true) {
            try {
                Thread.sleep(Constants.kLoopDt);
            } catch (InterruptedException ex) {
                System.out.println("interrupted");
                return;
            }
        }
    }

    public NetworkTableValue getValue(String key) {
        return table.getEntry(key).getValue();
    }
}
