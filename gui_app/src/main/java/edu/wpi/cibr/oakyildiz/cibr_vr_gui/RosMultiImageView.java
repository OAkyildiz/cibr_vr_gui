package edu.wpi.cibr.oakyildiz.cibr_vr_gui;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.ros.android.MessageCallable;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

/**
 * Displays incoming sensor_msgs/CompressedImage messages.
 * 
 * @author ethan.rublee@gmail.com (Ethan Rublee)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class RosMultiImageView<T> extends ImageView{

  private ConnectedNode node;
  private String topicName;
  private String messageType;
  private MessageCallable<Bitmap, T> callable;
  private Subscriber<T> subscriber;
  
  public RosMultiImageView(Context context) {
    super(context);
  }

  public RosMultiImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RosMultiImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public void setMessageToBitmapCallable(MessageCallable<Bitmap, T> callable) {
    this.callable = callable;
  }
  //TODO:  pull out NodeMain implementation and place it in the container class, keep OnStart and call it from there

  //TODO: these go to the OverlayView
//  @Override
//  public GraphName getDefaultNodeName() {
//    return GraphName.of("ros_image_view");
//  }
//
//  @Override
//  public void onStart(ConnectedNode connectedNode) {
//    node = connectedNode;
//    subscriber = connectedNode.newSubscriber(topicName, messageType);
//    subscriber.addMessageListener(new MessageListener<T>() {
//      @Override
//      public void onNewMessage(final T message) {
//        post(new Runnable() {
//          @Override
//          public void run() {
//            setImageBitmap(callable.call(message));
//          }
//        });
//        postInvalidate();
//      }
//    });
//  }
//
//  @Override
//  public void onShutdown(Node node) {
//  }
//
//  @Override
//  public void onShutdownComplete(Node node) {
//  }
//
//  @Override
//  public void onError(Node node, Throwable throwable) {
//  }

}
