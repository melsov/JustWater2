package water;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

//import Person.MathyStuff;

public class Water2 extends JPanel implements ActionListener,KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Drop> drops = new ArrayList<Drop>();
	List<Line2D.Double> obstacles = new ArrayList<Line2D.Double>();
	
	public static Timer animate;
	Line2D line = new Line2D.Double();
	public double MX;
	public double MY;
	Point2D p1 = new Point2D.Double();
	Point2D p2 = new Point2D.Double();
	int frames = 0;
	double superAngle;
	int added=0;
	int deleted=0;
	double waterHeight = 675;
	boolean space;
	
	public static void main(String[] as) {
		JFrame fr = new JFrame();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		fr.setSize(1280, 675);
//		fr.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		fr.setUndecorated(true);
//		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
//		device.setFullScreenWindow(fr);
		Water2 db = new Water2();
		
		db.setSize((int)dim.getWidth(),(int) dim.getHeight());
		db.setFocusable(true);
		fr.add(db);
		
		fr.setVisible(true);
		fr.setResizable(true);
	}
	
	public void paint(Graphics g2)
	{
		Graphics2D g = (Graphics2D) g2;
		g.clearRect(0, 0, 1280, 675);
		g.setPaint(Color.GREEN);
		
		superAngle=getAngle(640,674,MX,MY);
		newDrop(640,674);
		drawObstacles(g);
		fillWater(g);
		drawWater(g);
	}

	public void addNotify(){
		super.addNotify();
//		setupBoxes();
		setupObstacles();
		animate = new Timer(25,this);
		setSize(1280,675);
		animate.start();
		addKeyListener(this);
	}
	
	private void setupObstacles() {
		
		int nudgeX = 0;
		double cols = 10.0;
		double rows = 10.0;
		for (int i = 0; i < (int) cols; ++i) 
		{
			for(int j = 0; j < (int) rows; ++j) 
			{
				//Make an interesting group of diagonals
				Line2D.Double l = new Line2D.Double(new Point2D.Double(i * 100 +nudgeX, j * 50), 
						new Point2D.Double(i * 100 + nudgeX + 55 * Math.cos((-i*1.5/cols) * Math.PI), j * 50 + 35 ));
				obstacles.add(l);
				nudgeX += 25;
			}
			nudgeX = 0;
		}
	}
	
	private void drawObstacles(Graphics2D g) {
		for(Line2D.Double l : obstacles) {
			g.setPaint(Color.DARK_GRAY);
			g.draw(l);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		Point point = MouseInfo.getPointerInfo().getLocation();
		MX = point.getX();
		MY = point.getY();
		repaint();	
	}
	
	public void newDrop(double Sx,double Sy)
	{
		Drop the_drop = new Drop();
		the_drop.Xloc = Sx;
		the_drop.Yloc = Sy;
		the_drop.Xvel = 50*Math.sin(superAngle);
		the_drop.Yvel = 50*Math.cos(superAngle);
		drops.add(the_drop);
		added++;
	}
	
	public static double getAngle(double CurrentX, double CurrentY, double FindX, double FindY)
	{
		double oneEightyInRadians = Math.PI;
		double ninetyInRadians = Math.PI * .5;
		double direction;
		if(CurrentY-FindY==0)
		{
			if(CurrentX-FindX > 0){
				direction = -ninetyInRadians;
			}else{
				direction = ninetyInRadians;
			}
		}else{
			direction = Math.atan((CurrentX-FindX)/(CurrentY-FindY));
		}
			
		if(CurrentY-FindY < 0){
			direction+=oneEightyInRadians;
		}
		direction+=oneEightyInRadians;
//		return modular(direction,360);
		return modular(direction,oneEightyInRadians * 2);
	}
	
	public static double modular(double number, double mod){
		while(number>mod){
			number-=mod;	
		}
		while(number<0){
			number = mod - number;	
		}
		return number;
	}

	public void drawWater(Graphics2D g){
		g.setPaint(Color.blue);
		g.setStroke(new BasicStroke(4));
		int l = drops.size();
		for(int i=0;i<l;i++)
		{
			Drop d = drops.get(i);

			d.collideWithLine(collidedWithAnObstacle(d));

			p1.setLocation(d.Xloc, d.Yloc);
			p2.setLocation(d.Xloc + d.Xvel,  d.Yloc + d.Yvel);
			line.setLine(p1,p2);
			g.draw(line);

			d.Xloc+=d.Xvel;
			d.Yloc+=d.Yvel;
			d.Yvel+=2;
			if(d.Yloc>waterHeight){
				if(0<d.Yvel){
					drops.remove(i);
					i--;
					deleted++;
					waterHeight-=0.25;
				}
			}
			l=drops.size();
		}
	}

	private Line2D.Double collidedWithAnObstacle(Drop d) 
	{
		Line2D.Double dropLine = d.getLine2D();
		for (Line2D.Double l : obstacles) {
			if (dropLine.intersectsLine(l)) {
				return l;
			}
		}
		return null;
	}


	public void debug(boolean debug){
		frames++;
		System.out.print(frames + " = frame number | ");
		System.out.print("list size: "+ drops.size()+ " | ");
		System.out.print("new drops = " + added + " | deleted drops = " + deleted + " | " );
		System.out.print("water level: " + (675-waterHeight) + "(" + waterHeight + ") | " );
		System.out.println("end of frame.");
		added=0;
		deleted=0;
		
	}

	public void fillWater(Graphics2D g){
		g.setPaint(Color.cyan);
		g.setStroke(new BasicStroke(1));
		for(double i = 675; i>waterHeight-1;i-=0.25){
			line.setLine(0,i,1280,i);
			g.draw(line);	
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int pressed = e.getKeyCode();
		if(pressed == KeyEvent.VK_SPACE){
			waterHeight+=10;
			if(waterHeight>675){
			waterHeight=675;	
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {	}

	@Override
	public void keyTyped(KeyEvent arg0) {	}

}
