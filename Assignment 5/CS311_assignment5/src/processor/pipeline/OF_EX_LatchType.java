package processor.pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int branchTarget;
	String optCode;
	int immediate;
	int op1, op2;
	int rd;
	
	int destination;

	boolean isBranchTaken;

	// HashMap<Integer, Integer> registersUsed = new HashMap<>();
	List<Integer> registersUsed = new ArrayList<Integer>();   

	public OF_EX_LatchType()
	{
		EX_enable = false;
		isBranchTaken = false;

		//initial state
		destination = 32;
		optCode = "00000";

		//33rd is for indicating latch data is null
		for(int i = 0 ; i < 33; i++){
			registersUsed.add(0);
		}
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
		this.op1 = op1;
	}

	public int getOp2(){
		return op2;
	}
	public void setOp2(int op2){
		this.op2 = op2;
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


	//get and set fro register Used
	public int getRegisterUsed(int index){
		return registersUsed.get(index);
	}

	public void setRegisterUsed(int index,int val){
		registersUsed.set(index, val);
	}

	//set and get for branch taken
	public boolean getIsBranchTaken(){
		return isBranchTaken;
	}

	public void setIsBranchTaken(boolean val){
		isBranchTaken = val;
	}

}
