package Lab4;

public class Frame {
	public int ID = -1;
	public int currentProcess = -1;
	public int currentPage = -1;
	public boolean isFree = true;
	public int lastUsedTime = -1;
	public int loadTime = -1;

    //Default constructor
	public Frame(){
		
	}

    public Frame(int frameID) {
		this.ID = frameID;
	}

    public void setCurrent(int pageID, int processID) {
		this.currentPage = pageID;
		this.currentProcess = processID;
		this.isFree = false;
	}

}


