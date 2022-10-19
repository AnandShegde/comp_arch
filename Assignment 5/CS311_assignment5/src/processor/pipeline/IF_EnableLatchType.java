package processor.pipeline;

public class IF_EnableLatchType {
	
	// Dynamic number of instructions
	int NumberOfInstructions;
	boolean IF_enable;
	boolean IF_busy;
	
	public IF_EnableLatchType()
	{
		NumberOfInstructions = 0;	
		IF_enable = true;
		IF_busy = false;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	//Incementing number of instructions
	public void incrementIns(){
		NumberOfInstructions++;
	}

	//get number of instructions
	public int getNumberOfInstructions(){
		return NumberOfInstructions;
	}

	public boolean isIFBusy(){
		return IF_busy;
	}

	public void setIFBusy(boolean value){
		IF_busy = value;
	}
}
