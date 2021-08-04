package ex.components;

import java.util.ArrayList;

public class ConclusionStats {
	public int score;
	public int goods;
	public int bads;
	
	public int time;
	public ArrayList<Integer> qtimes;
	
	public double avgtime;
	public int longest;
	public int shortest;
	
	public ConclusionStats(int sco, int go, int ba, int ti, ArrayList<Integer> tis) {
		score = sco;
		goods = go;
		bads = ba;
		
		time = ti;
		qtimes = tis;
	}
	
	public void calculateDetails() {
		avgtime = (double)time / qtimes.size();
		int max = qtimes.size()>0?qtimes.get(0):0;
		int min = qtimes.size()>0?qtimes.get(0):0;
		int sum = 0;
		for(int i = 0; i < qtimes.size(); i++) {
			if(max < qtimes.get(i))
				max = qtimes.get(i);
			if(min > qtimes.get(i))
				min = qtimes.get(i);
			sum += qtimes.get(i);
		}
		longest = max;
		shortest = min;
		System.out.println("Sum of times: " + sum);
	}
	
}
