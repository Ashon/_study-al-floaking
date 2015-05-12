import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class Automaton extends Movement2D{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6517954900688909931L;
	private double sight;
	private double sight2;

	// 보이드의 시야
	private double range;
	private double range2;
	// 부채꼴의 영역을 선언
	private Arc2D.Double sightArea;

	// 보이드의 크기
	private double size;
	private double size2;
	// 보이드의 형태는 원모양으로 설정한다.
	private Ellipse2D.Double sizeArea;

	private double minRange;
	private double minRange2;
	// 분리행동을 구현하기위한 최소 범위
	private Ellipse2D.Double minRangeArea;

	// 보이드가 행동을 하는지에 대한 boolean 연산자.
	private boolean activate;

	private Color color;

	// 보이드의 상태
	private int state;

	public void setState(int s){
		state = s;
	}
	public int getState(){
		return state;
	}

	public Automaton(){
		super();
		activate = false;
	}
	public Automaton(double x, double y){
		super(x, y);
		activate = false;
	}
	public Automaton(double x, double y, double accelation, double maxSpeed){
		super(x, y, accelation, maxSpeed);
		activate = false;
	}
	public Automaton(double x, double y, double accelation, double maxSpeed, double angle, double angularSpeed, Color c){
		super(x, y, accelation, maxSpeed, angle, angularSpeed);
		activate = false;
		initAutomaton();
		color = c;
	}
	public void initAutomaton(){
		activate = false;
		setSightRange(0.2, 60);
		setSize(2);
		setMinRange(20);
	}
	public void setColor(Color c){
		color = c;
	}
	public Color getColor(){
		return color;
	}
	public void setSightRange(double sight, double range){
		this.sight = sight * Math.PI;
		sight2 = this.sight * 2;
		this.range = range;
		range2 = this.range * 2;
		sightArea = new Arc2D.Double(x - range, y - range, range2, range2,
				Math.toDegrees(-(sight + angle)), Math.toDegrees(sight2), Arc2D.PIE);
	}
	public double getSight(){
		return sight;
	}
	public double getRange(){
		return range;
	}
	public void setSize(double val){
		size = val;
		size2 = size * 2;
		sizeArea = new Ellipse2D.Double(x - size, y - size, size2, size2);
	}
	public double getSize(){
		return size2;
	}
	public void setMinRange(double val){
		minRange = val;
		minRange2 = minRange * 2;
		minRangeArea = new Ellipse2D.Double(x - minRange, y - minRange, minRange2, minRange2);
	}
	public double getMinRange(){
		return minRange2;
	}
	public Ellipse2D.Double getMinRangeArea(){
		return minRangeArea;
	}
	public void setActivate(boolean b){
		activate = b;
	}
	public boolean getActivate(){
		return activate;
	}
	public Ellipse2D.Double setSizeArea(){
		return sizeArea;
	}
	public Arc2D.Double getSightArea(){
		return sightArea;
	}

	// getBrakeDist()를 오버라이드. 제동거리와 보이드의 크기만큼의 제동거리를 설정.
	public double getBrakeDist(){
		return super.getBrakeDist() + size;
	}
	// move()를 오버라이드. 이동후 각 범위에 대해서도 좌표에 맞게 갱신을 해줌.
	public void move(){
		super.move();
		refreshSight();
		refreshSize();
		refreshMinRange();
	}
	// act()를 위임. 보이드의 틱당 움직임을 제어하는 알고리즘
	public void act(){
		if(activate){
			// 방향제어
			double targetAngle = getTargetAngle(target);
			if(Math.abs(targetAngle - angle) > angularSpeed){
				int decAngle =  decAngle(targetAngle);
				if(decAngle == 1)
					turnCW();
				if(decAngle == -1)
					turnCCW();
			}

			// 속도제어
			if(distance(target) > getBrakeDist()){
				accelerate();
			}else{
				brake();
			}
			if(sizeArea.contains(target))
				activate = false;
		}else{
			brake();
		}
		move();
	}

	// 이동후 각 범위에 대한 갱신 알고리즘
	public void refreshSight(){
		sightArea.setAngleStart(Math.toDegrees(-(sight + angle)));
		sightArea.setAngleExtent(Math.toDegrees(sight2));
		sightArea.setFrame(x - range, y - range, range2, range2);
	}
	public void refreshSize(){
		sizeArea.setFrame(x - size, y - size, size2, size2);
	}
	public void refreshMinRange(){
		minRangeArea.setFrame(x - minRange, y - minRange, minRange2, minRange2);
	}

	// 페인팅 메소드
	public void drawSight(Graphics2D g2d){
		g2d.draw(sightArea);
	}
	public void drawMinRange(Graphics2D g2d){
		g2d.draw(minRangeArea);
	}
	public void drawTarget(Graphics2D g2d){
		g2d.draw(new Line2D.Double(x, y, target.getX(), target.getY()));
	}
	public void drawAngle(Graphics2D g2d){
		g2d.draw(new Line2D.Double(x, y, x + (size2 * Math.cos(angle)), y + (size2 * Math.sin(angle))));
	}
	public void drawAutomaton(Graphics2D g2d){
		g2d.draw(sizeArea);
	}
}