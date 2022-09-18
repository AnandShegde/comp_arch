package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int aluResult;
	int destination;
	boolean isWriteback;
	String optCode;
	int rs1;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	//aluResults
	public void setAluResult(int aluResult){
		this.aluResult = aluResult;
	}
	public int getAluresult(){
		return aluResult;
	}

	//rs1
	public void setrs1(int rs1){
		this.rs1 = rs1;
	}
	public int getrs1(){
		return rs1;
	}

	//Destination
	public void setDestination(int destination){
		this.destination = destination;
	}
	public int getDestination(){
		return destination;
	}

	//isWriteBack
	public void setWriteBack(boolean isWriteBack){
		this.isWriteback = isWriteBack;
	}
	public boolean getWriteBack(){
		return isWriteback;
	}

	//optCode
	public void setOptCode(String optCode){
		this.optCode = optCode;
	}
	public String getOptCode(){
		return optCode;
	}

}
