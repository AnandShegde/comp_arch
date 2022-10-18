package processor.pipeline;

public class IF_EnableLatchType {
	
	// Dynamic number of instructions
	int NumberOfInstructions;
	boolean IF_enable;
	
	public IF_EnableLatchType()
	{
		NumberOfInstructions = 0;	
		IF_enable = true;
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
}
