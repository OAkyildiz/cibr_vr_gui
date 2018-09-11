/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.wpi.cibr.oakyildiz.cibr_vr_gui;


import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import org.ros.address.InetAddressFactory;
import org.ros.namespace.GraphName;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * A Cardboard sample application.
 */
public class VRViewerActivity extends RosVRActivity implements GvrView.StereoRenderer {
    static final int ROS_REFRESH_PER_SEC = 1;

    private static final String TAG = "MainActivity";
    private GVROverlayView mOverlayView;
    private GvrView gvrView;

    private RosMultiImageView rightRosImageView, leftRosImageView;
    //private View.OnTouchListenter touchListener;
    
    
    public VRViewerActivity() {
        super("CIBR VR GUI is running.", "CarboardROSView", URI.create("http://130.215.206.128:11311"));
//        super("Cardboard", "Cardboard");

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //mOverlayView.setMessageType();
        //rightRosImageView = mOverlayView.getRosImageView(GVROverlayView.Side.RIGHT);
        //leftRosImageView = mOverlayView.getRosImageView(GVROverlayView.Side.LEFT);
        init(GVRNodeMainExecutorService);
    }

    
    /**
     * Sets the view to our CardboardView and initializes the transformation matrices we will use
     * to render our scene.
     * //@param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardboard_viewer);
        gvrView = (GvrView) findViewById(R.id.cardboard_view);
        gvrView.setRenderer(this);
        setGvrView(gvrView);

        mOverlayView = (GVROverlayView) findViewById(R.id.overlay);
        //mOverlayView.setImgViewParams();
        mOverlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // get pointer index from the event object
                int pointerIndex = event.getActionIndex();

                // get pointer ID
                int pointerId = event.getPointerId(pointerIndex);

                // get masked (not specific to a pointer) action
                int maskedAction = event.getActionMasked();

                switch (maskedAction) {
                    case MotionEvent.ACTION_DOWN: return true;
                    case MotionEvent.ACTION_UP: {
                        mOverlayView.setIndex();
                        mOverlayView.switch_camera();

                        return true;
                    }
                }
                return false;
            }
        });

    }
    


    
    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    /**
     * Creates the buffers we use to store information about the 3D world. OpenGL doesn't use Java
     * arrays, but rather needs data in a format it can understand. Hence we use ByteBuffers.
     *
     * @param config The EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
    }

    /**
     * Prepares OpenGL ES before we draw a frame.
     *
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) { }

    @Override
    public void onDrawEye(Eye eye) { }


    @Override
    public void onFinishFrame(Viewport viewport) { }

 
    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        if (mOverlayView != null && nodeMainExecutor != null) {


            NodeConfiguration cibrVRNodeConfig = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostName())
                    .setMasterUri(getMasterUri());
            cibrVRNodeConfig.setNodeName(GraphName.of("cibr_vr_gui"));

            nodeMainExecutor.execute(mOverlayView, cibrVRNodeConfig);

        }
    }



}
