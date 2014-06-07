package water;

import java.awt.geom.Point2D;

public class Pointt 
{
	public float x, y;
	
	/*
	 * CONSTRUCTORS
	 */
	public Pointt() {
	}
	public Pointt(float xx, float yy) {
		x=xx; y=yy;
	}
	public Pointt(double xx, double yy) {
		this((float) xx, (float) yy);
	}
	public Pointt(Point2D.Float p) {
		this(p.x, p.y);
	}
	public Pointt(Point2D p) {
		this( p.getX(), p.getY());
	}
	
	/*
	 * MATH
	 */
	public boolean greaterThan(Pointt other) {
		return this.x > other.x && this.y > other.y;
	}
	public Pointt plus(Pointt other) {
		return new Pointt(other.x + x, other.y + y);
	}
	public Pointt minus(Pointt other) {
		return new Pointt(x - other.x, y - other.y);
	}
	public Pointt multiply(Pointt other) {
		return new Pointt(other.x * x, other.y * y);
	}
	public Pointt multiply(float num) {
		return new Pointt(num * x, num * y);
	}
	public Pointt dividedBy(Pointt other) {
		return new Pointt(x/other.x, y/other.y);
	}
	public Pointt dividedBy(float num) {
		if (num == 0) {
			return new Pointt(Float.MAX_VALUE, Float.MAX_VALUE);
		}
		return new Pointt(x/num, y/num);
	}
	public Pointt dividedBy(double num) {
		if (num == 0) {
			return new Pointt(Float.MAX_VALUE, Float.MAX_VALUE);
		}
		return new Pointt(x/num, y/num);
	}
	public Pointt multiply(double num) {
		return new Pointt(num * x, num * y);
	}
	public double distance() {
		return Math.sqrt(x*x + y*y);
	}
	public double dot(Pointt other) {
		return this.x* other.x + this.y*other.y;
	}
	
}
