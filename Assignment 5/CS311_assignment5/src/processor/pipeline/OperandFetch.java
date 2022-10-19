package processor.pipeline;

import processor.Processor;
import java.util.HashMap;
import java.util.Map;


// import java.util.Arrays;
import java.lang.Integer;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	IF_EnableLatchType IF_EnableLatch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public static Map<String,Integer> insType = new HashMap<String, Integer>(){{
		put("00000",3);
		put("00001",2);
		put("00010",3);
		put("00011",2);
		put("00100",3);
		put("00101",2);
		put("00110",3);
		put("00111",2);
		put("01000",3);
		put("01001",2);
		put("01010",3);
		put("01011",2);
		put("01100",3);
		put("01101",2);
		put("01110",3);
		put("01111",2);
		put("10000",3);
		put("10001",2);
		put("10010",3);
		put("10011",2);
		put("10100",3);
		put("10101",2);
		put("10110",2);
		put("10111",2);
		put("11000",1);
		put("11001",21);
		put("11010",21);
		put("11011",21);
		put("11100",21);
		put("11101",0);
	}};

	public static int binaryToInt(String bin){
		if(bin.substring(0, 1).equals("0")) return (int)Long.parseLong(bin,2);
		int n = bin.length();
		int val = (int)Long.parseLong(bin,2);
		val = val - (int)Math.pow((double)2,(double) n);
		return val;
	}

	public static String binaryofint(int val){
		String binaryVal = Long.toBinaryString( Integer.toUnsignedLong(val) | 0x100000000L ).substring(1);
		return binaryVal;
	}
	

	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, IF_EnableLatchType iF_EnableLatch, EX_MA_LatchType eX_MA_Latch , MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;

	}

	void checkDataHazards(String optcode, int dest){
		System.out.println("check data hazards");
		System.out.println("optcode dest "+optcode+" "+dest);
		if(insType.get(optcode) == 3 || (insType.get(optcode) == 2 && !optcode.equals("10111"))){
			OF_EX_Latch.setRegisterUsed(dest, 1);
			
		}
	}

	void eraseDataHazards(){
		for(int i = 0; i < 33; i++){
			OF_EX_Latch.setRegisterUsed(i, 0);
		}
	}
	
	public void performOF()
	{
		System.out.println("operand fetch");

		if(IF_OF_Latch.isOFBusy()){
			return;
		}

		if(OF_EX_Latch.getIsBranchTaken() && IF_OF_Latch.isOF_enable()){
			// System.out.println(IF_EnableLatch.getNumberOfInstructions());
			
			System.out.println("branck taken");
			String ins = binaryofint(IF_OF_Latch.getInstruction());
			
			// String ins = binaryofint((int)Long.parseLong("10110000000001100000000000000011",2), 32);
			System.out.println(" Ins = "+ ins);
			OF_EX_Latch.setIsBranchTaken(false);
			IF_OF_Latch.setOF_enable(false);
			IF_EnableLatch.setIF_enable(true);

		}
		else if(IF_OF_Latch.isOF_enable())
		{

			

			System.out.println("branck not taken");


			int pc = containingProcessor.getRegisterFile().getProgramCounter();
			// System.out.println(IF_OF_Latch.getInstruction());
			String ins = binaryofint(IF_OF_Latch.getInstruction());
			
			// String ins = binaryofint((int)Long.parseLong("10110000000001100000000000000011",2), 32);

			
			System.out.println("pc= "+pc+" Ins = "+ ins);
			//Control unit
			String optcode = ins.substring(0, 5);
			OF_EX_Latch.setOptCode(optcode);
			
			//Immediate 
			int immediate17 = binaryToInt(ins.substring(15));
			// int immediate17 = (int)Long.parseLong(ins.substring(15),2);
			OF_EX_Latch.setImmediate(immediate17);

			// Writing branchTarget
			int offset;
			// for jmp -- rd + imm(17 bits) bits
			if(ins.substring(0,5).equals("11000")){

				int rd = containingProcessor.getRegisterFile().getValue(
					(int)Long.parseLong(ins.substring(5,10),2)
					);

				// offset = (int)Long.parseLong(ins.substring(10),2) + rd;
				offset = rd + binaryToInt(ins.substring(10));

				System.out.println("rd "+rd+" offset"+offset);
			}
			else{
				offset = immediate17;
			}
			
			int branchTarget = offset + pc - 1;
			OF_EX_Latch.setBranchTarget(branchTarget);
			
			
			// set op1
			int rs1 = (int)Long.parseLong(ins.substring(5,10),2);
			int op1 = containingProcessor.getRegisterFile().getValue(rs1);

			System.out.println("op1 rs1" + op1 + " "+rs1);
			
			

			int dest = 0;
			//set op2
			int rs2 = 0;
			int op2;
			int rd;

			// R3 type
			if(insType.get(optcode) == 3){
				rs2 = (int)Long.parseLong(ins.substring(10,15),2);
				op2 = containingProcessor.getRegisterFile().getValue(
					rs2
				);
				dest = (int)Long.parseLong(ins.substring(15,20),2);
				rd = containingProcessor.getRegisterFile().getValue(dest);
				System.out.println("r3 tye op2 rs2 "+op2+" dest "+dest+" rd"+rd+" rs2 "+rs2);
			}	
			else if(optcode.equals("10111")){
				// Store
				// it is reverse. ie [rd+imm] = rs1
				dest = (int)Long.parseLong(ins.substring(10,15),2)  ;
				rd = containingProcessor.getRegisterFile().getValue(
					dest
				);
				rs1 = (int)Long.parseLong(ins.substring(5,10),2);
				op2 = containingProcessor.getRegisterFile().getValue(
					rs1
				);
			}		
			else{
				rs2 = 0;
				op2 = 0;
				dest = (int)Long.parseLong(ins.substring(10,15),2);
				rd = containingProcessor.getRegisterFile().getValue(dest);
			}

			checkDataHazards(OF_EX_Latch.getOptCode(), OF_EX_Latch.getDestination());
			checkDataHazards(EX_MA_Latch.getOptCode(), EX_MA_Latch.getDestination());
			checkDataHazards(MA_RW_Latch.getOptCode(), MA_RW_Latch.getDestination());		
			
			
			//Assignment 4
			boolean isDependancy = false;
			if(insType.get(optcode) == 3){
				if(OF_EX_Latch.getRegisterUsed(rs1) > 0 || OF_EX_Latch.getRegisterUsed(rs2) > 0){
					isDependancy = true;
					OF_EX_Latch.setEX_enable(false);
					IF_EnableLatch.setIF_enable(false);
				}
			}
			else if(insType.get(optcode) == 2){
				if(optcode.equals("10111")){
					if(OF_EX_Latch.getRegisterUsed(rs1) > 0 || OF_EX_Latch.getRegisterUsed(dest) > 0){
						isDependancy = true;
						OF_EX_Latch.setEX_enable(false);
						IF_EnableLatch.setIF_enable(false);
					}
				}
				else{
					if(OF_EX_Latch.getRegisterUsed(rs1) > 0){
						isDependancy = true;
						OF_EX_Latch.setEX_enable(false);
						IF_EnableLatch.setIF_enable(false);
					}
				}
			}
			else if(insType.get(optcode) == 21){
				if(OF_EX_Latch.getRegisterUsed(rs1) > 0 || OF_EX_Latch.getRegisterUsed(dest) > 0){
					isDependancy = true;
					OF_EX_Latch.setEX_enable(false);
					IF_EnableLatch.setIF_enable(false);
				}
			}
			if(!isDependancy){
				if(insType.get(optcode) == 21){
					System.out.println("BGT BGT BGT BGT BGT Op1 = "+ op1);
					System.out.println("BGT BGT BGT BGT BGT dest = "+ rd);
				}
				IF_EnableLatch.setIF_enable(true);
				// if(insType.get(optcode) == 3 || (insType.get(optcode) == 2 && !optcode.equals("10111"))){
				// 	// System.out.println("rd "+rd);
				// 	// OF_EX_Latch.setRegisterUsed(dest, 4);
				// 	int cur_no_reg = OF_EX_Latch.getRegisterUsed(dest);
				// 	OF_EX_Latch.setRegisterUsed(dest, cur_no_reg+1);
					
				// }
				//Erase vector if there are no deps
				eraseDataHazards();
				OF_EX_Latch.setOp1(op1);
				OF_EX_Latch.setOp2(op2);
				OF_EX_Latch.setRd(rd);
				OF_EX_Latch.setDestination(dest);
				

				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(true);


				//end statement
				if(insType.get(optcode) == 0){
					System.out.println("Set if busy : end");
					// containingProcessor.getRegisterFile().setProgramCounter(pc-1);
					IF_EnableLatch.setIFBusy(true);
				}
			}
	  	}
	} 
}
