
import java.awt.geom.Point2D;
public abstract class Movement2D extends Point2D.Double{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4793805983343302668L;

	public final static double PI2 = 2 * Math.PI;

	protected Point2D.Double target;
	protected double accelation;
	protected double accelation2;
	protected double velocity;
	protected double maxSpeed;
	protected double angle;
	protected double angularSpeed;

	// 생성자
	public Movement2D(){ }
	public Movement2D(double x, double y){
		velocity = 0;
		setLocation(x, y);
		target = this;
	}
	public Movement2D(double x, double y, double accelation, double maxSpeed){
		velocity = 0;
		setLocation(x, y);
		setAccelation(accelation);
		setMaxSpeed(maxSpeed);
		target = this;
	}
	public Movement2D(double x, double y, double accelation, double maxSpeed,
			double angle, double angularSpeed){
		velocity = 0;
		setLocation(x, y);
		setAccelation(accelation);
		setMaxSpeed(maxSpeed);
		setAngle(angle);
		setAngularSpeed(angularSpeed);
	}

	// 멤버변수 관리
	public void setTarget(Point2D.Double p2d){
		target = p2d;
	}
	public Point2D.Double getTarget(){
		return target;
	}
	public void setAccelation(double val){
		accelation = val;
		accelation2 = accelation * 2;
	}
	public double getAccelation(){
		return accelation;
	}
	public double getVelocity(){
		return velocity;
	}
	public void setMaxSpeed(double val){
		maxSpeed = val;
	}
	public double getMaxSpeed(){
		return maxSpeed;
	}
	public void setAngle(double val){
		angle = val;
	}
	public double getAngle(){
		return angle;
	}
	public void setAngularSpeed(double val){
		angularSpeed = val * Math.PI;
	}
	public double getAngularSpeed(){
		return angularSpeed;
	}
	//  속도제어
	public void accelerate(){
		if(velocity < maxSpeed)
			velocity += accelation;
	}
	public void brake(){
		if(velocity > 0)
			velocity -= accelation;
	}
	public void move(){
		x += velocity * Math.cos(angle);
		y += velocity * Math.sin(angle);
	}
	// 제동거리를 리턴.
	public double getBrakeDist(){
		return ((velocity * velocity) / accelation2);
	}

	// 무브먼트의 좌표와 타겟좌표를 이용해 타겟의 각거리를 리턴.
	public double getTargetAngle(Point2D.Double target){
		double dx = x - target.x;
		double dy = y - target.y;
		return (Math.atan2(dy, dx)) + Math.PI;
	}

	// 방향제어
	public void turnCCW(){
		angle -= angularSpeed;
		while(angle <= 0)
			angle += PI2;
	}
	public void turnCW(){
		angle += angularSpeed;
		while(angle > PI2)
			angle -= PI2;
	}

	// 타겟과의 각거리를 이용해 빠른 회전방향을 결정. '1' = 시계방향  '-1' = 반시계방향
	public int decAngle(Point2D.Double target){
		return decAngle(getTargetAngle(target));
	}
	public int decAngle(double targetAngle){
		double angleDelta = targetAngle - angle;
		int dec = 0;
		if((targetAngle > 0.5 * Math.PI || angle < 1.5 * Math.PI)
				&& (angle > 0.5 * Math.PI || targetAngle < 1.5 * Math.PI)
				&& Math.abs(angleDelta) < Math.PI)
			dec = (0 < angleDelta) ? 1 : -1;
		else
			dec = (0 < angleDelta) ? -1 : 1;
		return dec;
	}

	public abstract void act();
}