package org.sdoaj.minecraft.rio;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import org.sdoaj.minecraft.rio.constants.Constants;

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
                Thread.sleep(Constants.loopDt);
            } catch (InterruptedException ex) {
                System.out.println("interrupted");
                return;
            }
        }
    }

    public NetworkTableValue get(String key) {
        return table.getEntry(key).getValue();
    }

    public void set(String key, Object value) {
        table.getEntry(key).setValue(value);
    }
}
