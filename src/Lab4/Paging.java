package Lab4;

import java.io.*;
import java.util.*;

public class Paging {

	public static int msize;
	public static int psize;
	public static int prsize;
	public static int mix;
	public static int numRefs;
	public static int numProcesses;
	public static int numFrames;
	public static String algorithm;
	public static int debuggingLevel = 0;
	public static int runtime = 1;
	public static final int QUANTUM = 3;
	public static Frame currentFrame = new Frame();

	public static ArrayList<Process> processes = new ArrayList<Process>();
	public static ArrayList<Frame> FT = new ArrayList<Frame>();
	public static ArrayList<Process> done = new ArrayList<Process>();




	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner random = new Scanner(new FileReader("src/Lab4/random-numbers.txt"));

        //Check
		if (args.length < 6) {
			System.err.println("Error: invalid input format");
			System.exit(1);
		}

        //Parse input
		msize = Integer.parseInt(args[0]);
		psize = Integer.parseInt(args[1]);
		prsize = Integer.parseInt(args[2]);
		mix = Integer.parseInt(args[3]);
		numRefs = Integer.parseInt(args[4]);
		algorithm = args[5];
		
		
		numFrames = msize/psize;

	
		for (int i = 0; i < numFrames; i++) {
			Frame f = new Frame(i);
			FT.add(f);
		}
        
    	loadProcesses(mix);

		
		switch(algorithm.toLowerCase()){
			case("lru"):
				LRU lru = new LRU(FT);
				lru.run(random);
				printResult();
				break;
			case("lifo"):
				LIFO lifo = new LIFO(FT);
				lifo.run(random);
				printResult();
				break;
			case("random"):
				Random randomp = new Random(FT);
				randomp.run(random);
				printResult();
				break;
			default:
				System.err.println("Error: invalid algorithm type");
				System.exit(1);
		}


	}


    
	public static boolean isDone(Process p) {
		if (p.referenced() == numRefs){
			p.isDone = true;
			done.add(p);
			return true;
		}
		return false;
	}


    //Method to create and load 
	public static void loadProcesses(int mix) {
		Process p = new Process();
		switch(mix){
			case(1):
				numProcesses = 1;
				p = new Process(1, prsize, 1, 0, 0, numFrames);
				processes.add(p);
				break;
			case(2):
				numProcesses = 4;
				for (int i = 0; i < numProcesses; i++) {
					p = new Process(i+1, prsize, 1, 0, 0, numFrames);
					processes.add(p);
				}
				break;
			case(3):
				numProcesses = 4;
				for (int i = 0; i < numProcesses; i++) {
					p = new Process(i+1, prsize, 0, 0, 0, numFrames);
					processes.add(p);
				}
				break;

			case(4):
				numProcesses = 4;
				double[][] prob = {
						{0.75, 0.25, 0},
						{0.75, 0, 0.25},
						{0.75, 0.125, 0.125},
						{0.5, 0.125, 0.125}
						};
				for (int i = 0; i < numProcesses; i++) {
					p = new Process(i+1, prsize, prob[i][0], prob[i][1], prob[i][2], numFrames);
					processes.add(p);
				}
				break;
		}
	}


    public static boolean hasPageFault(int processID, int pageNumber, Collection<Frame> frames){
		for (Frame f : frames){
			if (f.currentProcess == processID && f.currentPage == pageNumber){
				currentFrame = f;
				return false;
			}
		}
		return true;
	}
    
    
    public static boolean freeFrameLeft(Collection<Frame> frames) {
		for (Frame f : frames){
			if (f.isFree) return true;
		}
		return false;
	}

    
    public static Frame nextHighestFrame() {
		Frame curr = new Frame();
		for (int i = FT.size()-1; i >= 0; i--) {
			curr = FT.get(i);
			if (curr.isFree) return curr;
		}
		return curr;
	}

    
 
	public static void printResult() {
		int totalFaults = 0;
		int totalResidency = 0;
		int totalEviction = 0;
		
		
		System.out.println("The machie size is " + msize + ".");
		System.out.println("The page size is " + psize + ".");
		System.out.println("The process size is " + prsize + ".");
		System.out.println("The job mix number is " + mix + ".");
		System.out.println("The number of references per process is " + numRefs + ".");
		System.out.println("The replacement algorithm is " + algorithm + ".");
		System.out.println("The level of debugging output is " + debuggingLevel + ".");
		System.out.println();
	
		
		for (Process p : processes) {
			totalFaults += p.numFaults;
			totalResidency += p.residency;
			totalEviction += p.eviction;
			if (p.eviction == 0){
				System.out.printf("Process %d had %d faults.\n", p.ID, p.numFaults);
				System.out.println("\tWith no evictions, the average residence is undefined.\n");
			}
			else{
			System.out.printf("Process %d had %d faults and %.15f average residency.\n", p.ID, p.numFaults, ((double)p.residency/p.eviction));
			}
		}
		System.out.println();
		if (totalEviction == 0){
			System.out.printf("The total number of faults is %d.\n", totalFaults);
			System.out.println("\tWith no evictions, the average residence is undefined.\n");
		}
		else{
			System.out.printf("The total number of faluts is %d and the overall average residency is %.15f.\n\n", totalFaults, ((double)totalResidency/totalEviction));
		}
	}
}