

/**
 * Celestial Body class for NBody
 * @author ola
 *
 */
public class CelestialBody {

	private double myXPos;
	private double myYPos;
	private double myXVel;
	private double myYVel;
	private double myMass;
	private String myFileName;

	/**
	 * Create a Body from parameters	
	 * @param xp initial x position
	 * @param yp initial y position
	 * @param xv initial x velocity
	 * @param yv initial y velocity
	 * @param mass of object
	 * @param filename of image for object animation
	 */

	/**
	 *Constructor to create CelestialBody object with instances variables above
	 */
	public CelestialBody(double xp, double yp, double xv,
			             double yv, double mass, String filename){
		myXPos = xp;
		myYPos = yp;
		myXVel = xv;
		myYVel = yv;
		myMass = mass;
		myFileName = filename;
	}

	/**
	 * Constructor that creates duplicates of CelestialBody objects
	 * Copies info of instance variables from parameter CelestialBody object
	 * to this CelestialBody object
	 */
	public CelestialBody(CelestialBody b){
		myXPos=b.getX();
		myYPos=b.getY();
	myXVel=b.getXVel();
		myYVel= b.getYVel();
		myMass= b.getMass();
	myFileName=b.getName();
	}

	/**
	 * @return initial X position of celestial body
	 */
	public double getX() {
		return myXPos;
	}

	/**
	 * @return initial Y position of celestial body
	 */
	public double getY() {
		return myYPos;
	}

	/**
	 * @return initial X velocity of celestial body
	 */
	public double getXVel() {
		return myXVel;
	}

	/**
	 * @return initial Y velocity of celestial body
	 */
	public double getYVel() {
		return myYVel;
	}

	/**
	 * @return mass of celestial body
	 */
	public double getMass() {
		return myMass;
	}

	/**
	 * @return filename of image for object animation
	 */
	public String getName() {
		return myFileName;
	}

	/**
	 * @param b CelestialBody object
	 * @return the distance between this body and body b
	 * using the distance formula
	 */

	public double calcDistance(CelestialBody b) {
		return Math.sqrt((Math.pow((b.getX()-myXPos),2) + Math.pow((b.getY()-myYPos),2)));
	}

	/**
	 * @param b CelestialBody object
	 * @return force exerted on this body by body b
	 */
	public double calcForceExertedBy(CelestialBody b) {
		return (6.67e-11*myMass*b.getMass())/Math.pow(calcDistance(b),2);
	}

	/**
	 * @param b CelestialBody object
	 * @return force exerted in the X direction by body b on this object
	 */
	public double calcForceExertedByX(CelestialBody b) {

		return (calcForceExertedBy(b)*(b.getX()-myXPos))/calcDistance(b);
	}

	/**
	 * @param b CelestialBody object
	 * @return force exerted in the Y direction by body b on this object
	 */
	public double calcForceExertedByY(CelestialBody b) {
		return (calcForceExertedBy(b)*(b.getY()-myYPos))/calcDistance(b);
	}

	/**
	 * @param bodies CelestialBody object
	 * @return total/net force exerted on this body in the X direction
	 * by all the bodies in the array parameter except this body itself
	 */

	public double calcNetForceExertedByX(CelestialBody[] bodies) {
		double sum = 0;
		for(CelestialBody b : bodies)
		{
			if(!b.equals(this))
			{
				sum += calcForceExertedByX(b);
			}
		}
		return sum;
	}

	/**
	 * @param bodies CelestialBody object
	 * @return total/net force exerted on this body in the Y direction
	 * by all the bodies in the array parameter except this body itself
	 */

	public double calcNetForceExertedByY(CelestialBody[] bodies) {
		double sum = 0;
		for(CelestialBody b : bodies)
		{
			if(!b.equals(this))
			{
				sum += calcForceExertedByY(b);
			}
		}
		return sum;
	}

	/**
	 * @param deltaT change in time
	 * @param xforce force in the x direction
	 * @param yforce force in the y direction
	 * updates body's position and velocity
	 */

	public void update(double deltaT, 
			           double xforce, double yforce) {
		double ax = xforce/myMass;
		double ay = yforce/myMass;
		double nvx = myXVel + deltaT*ax;
		double nvy = myYVel + deltaT*ay;
		double nx = myXPos + deltaT*nvx;
		double ny = myYPos + deltaT*nvy;
		myXVel = nvx;
		myYVel = nvy;
		myXPos = nx;
		myYPos = ny;

	}

	/**
	 * draws the CelestialBody object
	 */

	public void draw() {
		StdDraw.picture(myXPos,myYPos, "images/"+myFileName);
	}
}
