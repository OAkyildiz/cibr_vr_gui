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

  
  public RosMultiImageView(Context context) {
    super(context);
  }

  public RosMultiImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RosMultiImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }


}
