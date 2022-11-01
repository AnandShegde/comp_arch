package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	boolean OF_busy;
	boolean isBranchTaken;
	boolean isResponse;

	int instruction;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
		OF_busy = false;
		isBranchTaken = false;
		isResponse = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean isOFBusy(){
		return OF_busy;
	}

	public void setOFBusy(boolean value){
		OF_busy = value;
	}

	public boolean getIsBranchTaken(){
		return isBranchTaken;
	}

	public void setBranchTarget(boolean value){
		isBranchTaken = value;
	}

	public boolean getIsResponse(){
		return isBranchTaken;
	}

	public void setResponse(boolean value){
		isBranchTaken = value;
	}

}
