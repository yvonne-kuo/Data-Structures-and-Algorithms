	

/**
 * Yvonne Kuo
 * Simulation program for the NBody assignment
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBody {
	
	/**
	 * Read the specified file and return the radius
	 * @param fname is name of file that can be open
	 * @return the radius stored in the file
	 * @throws FileNotFoundException if fname cannot be open
	 */
	public static double readRadius(String fname) throws FileNotFoundException  {
		Scanner s = new Scanner(new File(fname));
		double radius = 0;
		for(int i = 0; i < 2; i++)
		{
			radius = s.nextDouble();	// find the radius
	}

		s.close();
		
		return radius;					//return the radius
	}
	
	/**
	 * Read all data in file, return array of Celestial Bodies
	 * read by creating an array of Body objects from data read.
	 * @param fname is name of file that can be open
	 * @return array of Body objects read
	 * @throws FileNotFoundException if fname cannot be open
	 */
	public static CelestialBody[] readBodies(String fname) throws FileNotFoundException {
		
			Scanner s = new Scanner(new File(fname));
			int nb = s.nextInt(); // # bodies to be read
			CelestialBody[] ans = new CelestialBody[nb];	//creates array based on body #
			s.nextDouble();	//skips over radius

		for(int k=0; k < nb; k++) {
				ans[k] = new CelestialBody(s.nextDouble(),s.nextDouble(),			//read data for each body
						s.nextDouble(),s.nextDouble(),s.nextDouble(),s.next());		//construct new body object
             }

			s.close();

			return ans;	//return array of body objects read
	}

	public static void main(String[] args) throws FileNotFoundException{
        double totalTime = 39447000.0;
        double dt = 25000.0;
		
		String fname= "./data/kaleidoscope.txt";
		if (args.length > 2) {
			totalTime = Double.parseDouble(args[0]);
			dt = Double.parseDouble(args[1]);
			fname = args[2];
		}	
		
		CelestialBody[] bodies = readBodies(fname);
		double radius = readRadius(fname);

		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0,0,"images/starfield.jpg");
		//StdAudio.play("images/2001.wav");
	
		// run simulation until time up

		for(double t = 0.0; t < totalTime; t += dt) {

			double[] xforces = new double[bodies.length];	//create double array to store forces in x direction
			double[] yforces = new double[bodies.length];	//create double array to store forces in y direction

			for(int i = 0; i < bodies.length; i++)	//loops over all bodies
			{
				xforces[i] = bodies[i].calcNetForceExertedByX(bodies);	//calculate netForcesX & stores in xforces
				yforces[i] = bodies[i].calcNetForceExertedByY(bodies);	//calculate netForcesX & stores in yforces
			}

			for(int j = 0; j < bodies.length; j++)	//loops over all bodies
			{
				bodies[j].update(dt,xforces[j],yforces[j]);	//update each body with t & corresponding x and y forces
			}
			
			StdDraw.picture(0,0,"images/starfield.jpg");
			
			for(CelestialBody b : bodies)	//loops over each body
			{
				b.draw();	//draws each body
			}

			StdDraw.show();
			StdDraw.pause(10);
		}
		
		// prints final values after simulation

        System.out.printf("%d\n", bodies.length);
        System.out.printf("%.2e\n", radius);
        for (int i = 0; i < bodies.length; i++) {
            System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    bodies[i].getX(), bodies[i].getY(),
                    bodies[i].getXVel(), bodies[i].getYVel(),
                    bodies[i].getMass(), bodies[i].getName());
        }
	}
}
