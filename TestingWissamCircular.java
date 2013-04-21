
import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;




public class TestingWissamCircular extends GraphicsProgram {
	
	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 12;

	public void run () {
		GImage image = new GImage ("CircularWissam.gif");
		GOval circle = new GOval (400,400,BALL_RADIUS*2,BALL_RADIUS*2);
		double r = circle.getWidth();
		double w = image.getWidth();
		GLabel label = new GLabel ("image width = "+w +"  while ball width= "+r);
		add(label,100,100);
		add(image,0,0);
		
		double scale_factor;
		scale_factor= (BALL_RADIUS*2)/w;
		
		image.scale(scale_factor,scale_factor);
		w = image.getWidth();
		label = new GLabel ("image width = "+w +"  while ball width= "+r);
		add(label,200,200);
		add(image,600,600);
		add(circle,600,600);
	}

}
