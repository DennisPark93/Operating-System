package Lab4;

import java.util.*;

public class LIFO extends Paging{

	public static Stack<Frame> frames = new Stack<Frame>();
	public static Stack<Frame> loadedFrames = new Stack<Frame>();

 
	public LIFO(ArrayList<Frame> FT) {
		frames.addAll(FT);
	}

   public static Frame replaceLIFO() {
		Frame next = loadedFrames.pop();
		return next;
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
							if (debuggingLevel != 0) System.out.printf("Fault, using free frame %d.\n", currentFrame.ID);
						}
						else{
							currentFrame = replaceLIFO();
							if (debuggingLevel != 0) {
								System.out.printf("Fault, evicting page %d of %d from frame %d.\n", currentFrame.currentPage, currentFrame.currentProcess, currentFrame.ID);
							}
							processes.get(currentFrame.currentProcess-1).evict(runtime, currentFrame.ID);
						}
						loadedFrames.add(currentFrame);
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
