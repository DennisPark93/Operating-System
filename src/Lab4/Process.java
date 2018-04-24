package Lab4;

import java.util.*;

public class Process extends Paging{

	public int ID;
	public int processSize;
	public int word;
	public int nextWord;
	public int referenced = 0;

	public int[] loadTime;
	public boolean isDone = false;

	public int numFaults = 0;
	public int residency = 0;
	public int eviction = 0;

	public double A;
	public double B;
	public double C;

    //Default 
	Process(){
		
	}

  
	public Process(int ID, int processSize, double A, double B, double C, int numFrames){
		this.ID = ID;
		this.processSize = processSize;
		this.A = A;
		this.B = B;
		this.C = C;
		this.word = (111*this.ID)%this.processSize;
		this.loadTime = new int[numFrames];             
	}


	public void load(int runtime, int frameID) {
		this.loadTime[frameID] = runtime;
	}

	public void addfault() {
		this.numFaults++;
	}

	public void evict(int runtime, int frameID) {
		this.eviction++;
		this.residency += runtime - this.loadTime[frameID];
	}

	public int referenced() {
		this.referenced++;
		return this.referenced;
	}

	public void getNextWord(Scanner random) {

		int randomInt = random.nextInt();
		if (debuggingLevel == 11)  {
			System.out.printf("%d uses random number: %d\n", this.ID, randomInt);
		}

		double y = randomInt / (Integer.MAX_VALUE + 1d);

		if (y < this.A) {
			this.nextWord = (this.word + 1)%this.processSize;
		}

		else if (y < this.A + this.B) {
			this.nextWord = (this.word - 5 + this.processSize)%this.processSize;
		}

		else if (y < this.A + this.B + this.C) {
			this.nextWord = (this.word + 4)%this.processSize;
		}

		else{
			randomInt = random.nextInt();
			if (debuggingLevel == 11)  {
				System.out.printf("%d uses random number: %d\n", this.ID, randomInt);
			}
			this.nextWord = randomInt%this.processSize;
		}
		this.word = this.nextWord;
	}
}
