package Lab4;

import java.util.ArrayList;
import java.util.Scanner;

public class LRU extends Paging{

	public static ArrayList<Frame> frames = new ArrayList<Frame>();

   public LRU(ArrayList<Frame> FT) {
		frames.addAll(FT);
	}

   public static Frame replaceLRU() {
		Frame curr = frames.get(0);
		for (Frame f : frames) {
			if (curr.lastUsedTime > f.lastUsedTime){
				curr = f;
			}
		}
		return curr;
	}

   public static void run(Scanner random){
		do {
			for (Process process : processes){

				for (int counter = 0; counter < QUANTUM; counter++){
					if (process.isDone) continue;
					int pageNumber = process.word/psize;
					if (debuggingLevel != 0) {
						System.out.printf("%d references word %d (page %d) at time %d: ", process.ID, process.word, pageNumber, runtime);
					}

					if (hasPageFault(process.ID, pageNumber, frames)){
						if (freeFrameLeft(frames)){
							currentFrame = nextHighestFrame();
							if (debuggingLevel != 0) {
								System.out.printf("Fault, using free frame %d.\n", currentFrame.ID);
							}
						}
						else{
							currentFrame = replaceLRU();
							if (debuggingLevel != 0) {
								System.out.printf("Fault, evicting page %d of %d from frame %d.\n", currentFrame.currentPage, currentFrame.currentProcess, currentFrame.ID);
							}
							processes.get(currentFrame.currentProcess-1).evict(runtime, currentFrame.ID);
						}
						process.numFaults++;
						currentFrame.setCurrent(pageNumber, process.ID);
						process.load(runtime, currentFrame.ID);
					}
					else{
						if (debuggingLevel != 0) {
							System.out.printf("Hit in frame %d.\n", currentFrame.ID);
						}
					}
                    isDone(process);
					process.getNextWord(random);
					currentFrame.lastUsedTime = runtime;
					runtime++;
				}
			}
		} while (done.size() < numProcesses);
	}
}
