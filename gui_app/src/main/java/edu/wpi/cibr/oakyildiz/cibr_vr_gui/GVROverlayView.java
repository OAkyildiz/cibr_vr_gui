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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.MessageCallable;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import java.util.HashMap;

import sensor_msgs.CompressedImage;


/**
 * Contains two sub-views to provide a simple stereo HUD.
 */

//public class CardboardOverlayView<T> extends LinearLayout implements NodeMain{
public class GVROverlayView<T> extends LinearLayout implements NodeMain{

    public enum Side {
        LEFT(0), RIGHT(1);

        int side;

        Side(int side) {
            this.side = side;
        }
    }

    private enum TopicLabel{HEAD_CAM(0), LEFT_CAM(1), RIGHT_CAM(2), WORKSPACE_CAM(3), BODY_CAM(4);

        private int num;

        TopicLabel(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public TopicLabel next(){
            return values()[(ordinal() + 1)%values().length];
        }
    }

    private String stringCmdTopic = "transcript";
    private String messageType = std_msgs.String._TYPE;
    //private StringCommandParser cmdPrser;


    private ConnectedNode node;
    private String imgTopic;
    private String imgMsgType;
    private MessageCallable<Bitmap, CompressedImage> imgCallable;
    private Subscriber<CompressedImage> imgSubscriber;
    private Subscriber<std_msgs.String> cmdSubscriber;


    public void setImgTopic(String topicName) { this.imgTopic = topicName; }

    public void setImgMsgType(String messageType) {
        this.imgMsgType = messageType;
    }

    public void setMsgToBitmapCallable(MessageCallable<Bitmap, CompressedImage> callable) { this.imgCallable = callable; }


    private static final String TAG = "CardboardOverlayView";
    private GVROverlayEyeView mLeftView;
    private GVROverlayEyeView mRightView;
    AttributeSet attrs;



   // private String[] topics = {LEFT_CAM, RIGHT_CAM, HEAD_CAM, WORKSPACE_CAM};
    private TopicLabel sel;
    private static HashMap<TopicLabel,String> topics;

    public static String get_namespace(String topic){

        return topic.split("/")[1];
    }
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("cibr_vr_gui");
    }


    @Override
    public void onStart(ConnectedNode connectedNode) {
        node = connectedNode;


        setImgTopic(topics.get(sel));
        setImgMsgType(CompressedImage._TYPE);
        setMsgToBitmapCallable(new BitmapFromCompressedImage());


        cmdSubscriber = connectedNode.newSubscriber(stringCmdTopic, messageType);
        cmdSubscriber.addMessageListener(new MessageListener<std_msgs.String>() {
            @Override
            public void onNewMessage(final std_msgs.String message) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        parse_command(message.getData());

                    }
                });
                postInvalidate();
            }
        });

    //get params here?
       // while(mRightView.getImageView() == null || mLeftView.getImageView() == null);;
        mLeftView = new GVROverlayEyeView(getContext(), attrs);
        mRightView = new GVROverlayEyeView(getContext(), attrs);

       LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        mLeftView.setLayoutParams(params);
        //addView(mLeftView);

        //mRightView = new GVROverlayEyeView(getContext(), attrs);
        mRightView.setLayoutParams(params);
        //addView(mRightView);

        // Set some reasonable defaults.
        setDepthOffset(0.016f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);
        addView(mRightView);
        addView(mLeftView);
        subscribe();
    }


    public void subscribe() {
        if(imgSubscriber!=null)
            imgSubscriber.shutdown();
        imgSubscriber = node.newSubscriber(imgTopic, imgMsgType);
        imgSubscriber.addMessageListener(new MessageListener<CompressedImage>() {
            @Override
            public void onNewMessage(final CompressedImage message) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mRightView.setImageViewBitmap(imgCallable.call(message));
                        mLeftView.setImageViewBitmap(imgCallable.call(message));                    }
                });
                postInvalidate();
            }
        });
    }


    @Override
    public void onShutdown(Node node) {
        cmdSubscriber.shutdown();
        imgSubscriber.shutdown();

    }

    @Override
    public void onShutdownComplete(Node node) { }

    @Override
    public void onError(Node node, Throwable throwable) {

    }


    public RosMultiImageView getRosImageView(Side side) {
        if (mRightView == null || mLeftView == null) {
            throw new IllegalStateException("Remember to call CardboardOverlayView.setTopicInformation(String topicName, String messageType)");
        }
        return side == Side.RIGHT ? mRightView.getImageView() : mLeftView.getImageView();
    }

    public void setImgViewAndParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        mLeftView.setLayoutParams(params);
        //addView(mLeftView);

        //mRightView = new GVROverlayEyeView(getContext(), attrs);
        mRightView.setLayoutParams(params);
        //addView(mRightView);

        // Set some reasonable defaults.
        setDepthOffset(0.016f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);
    }

    public GVROverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.sel = TopicLabel.HEAD_CAM;


        topics= new HashMap<TopicLabel, String>();
//        topics.put(TopicLabel.LEFT_CAM, "/left_wrist_camera/color/image_raw/compressed");
//        topics.put(TopicLabel.RIGHT_CAM, "/right_wrist_camera/color/image_raw/compressed");
//        topics.put(TopicLabel.HEAD_CAM, "/cibr_webcam/image_raw/compressed");
//        topics.put(TopicLabel.WORKSPACE_CAM, "/workspace_cam/image_raw/compressed");

        topics.put(TopicLabel.LEFT_CAM, "/h_left_wrist_cam/image_raw/compressed");
        topics.put(TopicLabel.RIGHT_CAM, "/h_right_wrist_cam/image_raw/compressed");
        topics.put(TopicLabel.HEAD_CAM, "/h_head_cam/image_raw/compressed");
        topics.put(TopicLabel.WORKSPACE_CAM, "/workspace_cam/image_raw/compressed");
        topics.put(TopicLabel.BODY_CAM, "/workspace_cam/image_raw/compressed");

        this.attrs = attrs;
        setOrientation(HORIZONTAL);


    }

    private void setDepthOffset(float offset) {
        mLeftView.setOffset(offset);
        mRightView.setOffset(-offset);
    }

    private void setColor(int color) {
        mLeftView.setColor(color);
        mRightView.setColor(color);
    }

    public void parse_command(String cmd) {

        if (cmd.contains("camera") || cmd.contains("switch")) {
            if (cmd.contains("head"))
                setIndex(TopicLabel.HEAD_CAM);
            else if (cmd.contains("workspace") || cmd.contains("third"))
                setIndex(TopicLabel.WORKSPACE_CAM);
            else if (cmd.contains("torso") || cmd.contains("chest") || cmd.contains("clavicle") || cmd.contains("body"))
                setIndex(TopicLabel.BODY_CAM);
            else if (cmd.contains("left"))
                setIndex(TopicLabel.LEFT_CAM);
            else if (cmd.contains("right"))
                setIndex(TopicLabel.RIGHT_CAM);
            else
                setIndex();
            switch_camera();
        }
        else if(cmd.contains("update")){
            //here update topic list
        }

        else
            makeToast(String.format("Didn't understand \'%s\' :(",cmd));
    }
    //TODO: jsut combine this, make method private and only set Sel (name in honorably)
    public void setIndex() {
        sel=sel.next();
    }

    public void setIndex(TopicLabel s) {
        sel = s;
    }

    public void switch_camera(){
        setImgTopic(topics.get(sel));
        subscribe();
        //rightEyeImageView.setMessageToBitmapCallable(new BitmapFromCompressedImage());
        //leftEyeImageView.setMessageToBitmapCallable(new BitmapFromCompressedImage());
        makeToast(String.format("%d: %s",sel.getNum(),get_namespace(topics.get(sel))));
    }

    private void makeToast(String string){
        Context context = getContext();
        CharSequence text = string;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
