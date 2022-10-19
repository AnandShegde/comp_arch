package generic;

public class ExecutionCompleteEvent extends Event {

	int aluResult, dest, rs1, branchTarget;
	String optcode;
	boolean isWriteBack, isBranchTaken;
   
	public ExecutionCompleteEvent(long eventTime, Element requestingElement, Element processingElement, int result, int dest, int rs1, int branchTarget,String optcode, boolean isWriteBack, boolean isBranchTaken)
	{
		super(eventTime, EventType.ExecutionComplete, requestingElement, processingElement);
		this.aluResult = result;
		this.dest = dest;
		this.rs1 = rs1;
		this.branchTarget = branchTarget;
		this.optcode = optcode;
		this.isBranchTaken = isBranchTaken;
		this.isWriteBack = isWriteBack;
	}

	public int getAluresult(){
		return aluResult;
	}

	public int getDestination(){
		return dest;
	}
	public int getBranchTarget(){
		return branchTarget;
	}
	public int getrs1(){
		return rs1;
	}
	
	public String getOptCode(){
		return optcode;
	}
	public boolean getIsWriteBack(){
		return isWriteBack;
	}
	public boolean getIsBranchTaken(){
		return isBranchTaken;
	}

}
