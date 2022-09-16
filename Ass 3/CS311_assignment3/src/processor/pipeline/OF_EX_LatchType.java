package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int branchTarget;
	String optCode;
	int immediate;
	int op1, op2;
	int rd;
	
	int destination;



	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}


	//get and set optcode
	public void setOptCode(String optCode){
		this.optCode = optCode;
	}
	public String getOptCode(){
		return optCode;
	}

	//get and set immediate
	public void setImmediate(int immediate){
		this.immediate = immediate;
	}
	public int getImmediate(){
		return immediate;
	}

	// get and set branch target
	public int getBranchTarget(){
		return branchTarget;
	}
	public void setBranchTarget(int branchTarget){
		this.branchTarget = branchTarget;
	}

	public int getOp1(){
		return op1;
	}
	public void setOp1(int op1){
		this.branchTarget = op1;
	}

	public int getOp2(){
		return op2;
	}
	public void setOp2(int op2){
		this.branchTarget = op2;
	}


	public int getRd(){
		return rd;
	}
	public void setRd(int rd){
		this.rd = rd;
	}

	//destination register address

	public void setDestination(int destination){
		this.destination = destination;
	}

	public int getDestination(){
		return destination;
	}





}
