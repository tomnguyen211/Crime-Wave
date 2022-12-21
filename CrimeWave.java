package exercise_crimewave;

import java.lang.Thread.State;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class CrimeWave {

	Random r = new Random();
	//Stack <Crime> crimes = new Stack<Crime>();
	Queue<Crime> crimes = new LinkedList<Crime>();
	int commited = 0;
	int solved = 0;

	Detective d1 = new Detective("Batman");
	Detective d2 = new Detective("Sherlock");
	Criminal c1 = new Criminal("Joker");
	Criminal c2 = new Criminal("Riddles");
	Thread f1 = new Thread(d1);
	Thread f2 = new Thread(d2);
	Thread f3 = new Thread(c1);
	Thread f4 = new Thread(c2);
	
	public void addCrime(Crime c, String criminalName)
	{
		System.out.println("Criminal " + criminalName + " commits a crime of severity " + c.getSeverity());
		crimes.add(c);
		commited++;
	}
	
	public void solveCrime(String detectiveName)
	{
		try
		{
			while(crimes.isEmpty())
			{					
				Thread.sleep(1);
			}
			int severity = crimes.poll().getSeverity();
			solved++;
			System.out.println("Detetive " + detectiveName + " solves a crime of severity " + severity);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public static void main(String[] args) {
		CrimeWave c = new CrimeWave();
		c.feed();
		System.exit(0);

	}
	public void feed()
	{
		f3.start();
		f4.start();
		f1.start();
		f2.start();
		
		while (f1.getState() != State.TERMINATED || f2.getState() != State.TERMINATED ||
				  crimes.isEmpty() == false)
		{
			try
			{
				//System.out.println(crimes.isEmpty());
				//System.out.println(f3.getState());
				Thread.sleep(1);				
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
				
			}
		}
		System.out.println("Commited: " + commited);
		System.out.println("Solved: " + solved);
	}
	
	private class Crime
	{
		int severity;
		public Crime(int s)
		{
			this.severity = s;
		}
		
		public int getSeverity() {
			return this.severity;
		}
	}
	
	private class Detective implements Runnable
	{
		private String name;
		
		public Detective(String n)
		{
			this.name = n;
		}

		@Override
		public void run() {
			synchronized(this)
			{
				while(crimes.isEmpty() == false || f3.getState() != State.TERMINATED || f4.getState() != State.TERMINATED)
				{
					solveCrime(name);
				}
			}			
		}
		
	}
	
	private class Criminal implements Runnable
	{
		private String name;
		
		public Criminal(String n)
		{
			this.name = n;
		}
		@Override
		public void run() {
			for(int n = 0; n < 100;n++)
			{
				Crime c = new Crime(r.nextInt(11));
				addCrime(c,name);
			}
		}
		
	}
	
	
	
}

