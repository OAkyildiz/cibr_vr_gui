//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.wpi.cibr.oakyildiz.cibr_vr_gui;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.ros.android.NodeMainExecutorService;
import org.ros.node.NodeMainExecutor;

public class GVRNodeMainExecutorService extends NodeMainExecutorService implements NodeMainExecutor {
    private final IBinder gvrBinder = new GVRNodeMainExecutorService.CardboardLocalBinder();

    public class CardboardLocalBinder extends Binder {
        CardboardLocalBinder() {
        }

        GVRNodeMainExecutorService getService() {
            return GVRNodeMainExecutorService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.gvrBinder;
    }


}
