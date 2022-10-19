package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	String optCode;
	int aluResult;
	int loadResult;
	boolean isWriteback;
	int destination;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		//initial state
		destination = 32;
		optCode = "00000";
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	//optCode
	public void setOptCode(String optCode){
		this.optCode = optCode;
	}
	public String getOptCode(){
		return optCode;
	}

	//aluResult
	public void setAluResult(int aluResult){
		this.aluResult = aluResult;
	}
	public int getAluresult(){
		return aluResult;
	}

	//Destination
	public void setDestination(int destination){
		this.destination = destination;
	}
	public int getDestination(){
		return destination;
	}

	//loadResult
	public void setLoadResult(int loadResult){
		this.loadResult = loadResult;
	}
	public int getLoadResult(){
		return loadResult;
	}

	//isWriteBack
	public void setWriteBack(boolean isWriteBack){
		this.isWriteback = isWriteBack;
	}
	public boolean getWriteBack(){
		return isWriteback;
	}

}
