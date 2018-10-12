package org.sdoaj.minecraft.rio;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTablesClient {
    public void run() {
        NetworkTableInstance instance = NetworkTableInstance.getDefault();
        instance.startClientTeam(254);
        instance.startDSClient(); // gets robot IP from driver station

        NetworkTable table = instance.getTable("minecraft");
        NetworkTableEntry testEntry = table.getEntry("test");

        while (true) {
            try {
                Thread.sleep(Constants.kLoopDt);
            } catch (InterruptedException ex) {
                System.out.println("interrupted");
                return;
            }

            System.out.println(testEntry.getValue().getValue());
        }
    }
}
