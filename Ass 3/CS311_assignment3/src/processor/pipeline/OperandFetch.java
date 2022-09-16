package processor.pipeline;

import processor.Processor;
import java.util.HashMap;
import java.util.Map;

import generic.Simulator;

// import java.util.Arrays;
import java.lang.Integer;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

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

	public static String binaryofint(int val){
		String binaryVal = Long.toBinaryString( Integer.toUnsignedLong(val) | 0x100000000L ).substring(1);
		return binaryVal;
	}
	

	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO

			int pc = containingProcessor.getRegisterFile().getProgramCounter();
			System.out.println(IF_OF_Latch.getInstruction());
			String ins = binaryofint(IF_OF_Latch.getInstruction());
			
			// String ins = binaryofint((int)Long.parseLong("10110000000001100000000000000011",2), 32);

			

			System.out.println("pc= "+pc+" Ins = "+ ins);
			//Control unit
			String optcode = ins.substring(0, 5);
			OF_EX_Latch.setOptCode(optcode);
			
			//Immediate 
			int immediate17 = (int)Long.parseLong(ins.substring(15),2);
			OF_EX_Latch.setImmediate(immediate17);

			// Writing branchTarget
			int offset;
			// for jmp -- rd + imm(17 bits) bits
			if(ins.substring(0,5)=="11000"){

				int rd = containingProcessor.getRegisterFile().getValue(
					(int)Long.parseLong(ins.substring(5,10),2)
					);

				offset = (int)Long.parseLong(ins.substring(10),2) + rd;
			}
			else{
				offset = immediate17;
			}
			
			int branchTarget = offset + pc;
			OF_EX_Latch.setBranchTarget(branchTarget);
			
			
			// set op1
			int op1 = containingProcessor.getRegisterFile().getValue(
				(int)Long.parseLong(ins.substring(5,10),2)
				);

			OF_EX_Latch.setOp1(op1);
			

			int dest = 0;
			//set op2
			int op2;
			int rd;

			// R3 type
			if(insType.get(optcode) == 3){
				op2 = containingProcessor.getRegisterFile().getValue(
					(int)Long.parseLong(ins.substring(10,15),2)  
				);
				dest = (int)Long.parseLong(ins.substring(15,20),2);
				rd = containingProcessor.getRegisterFile().getValue(dest);
			}	
			else if(optcode.equals("10111")){
				// Store
				// it is reverse. ie [rd+imm] = rs1
				op2 = containingProcessor.getRegisterFile().getValue(
					(int)Long.parseLong(ins.substring(10,15),2)  
				);
				rd = containingProcessor.getRegisterFile().getValue(
					(int)Long.parseLong(ins.substring(5,10),2)  
				);
			}		
			else{
				op2 = 0;
				dest = (int)Long.parseLong(ins.substring(10,15),2);
				rd = containingProcessor.getRegisterFile().getValue(dest);
			}

			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setRd(rd);
			OF_EX_Latch.setDestination(dest);
			

			//end simulation if end
			

			// System.out.println("imm17" + immediate17);
			// System.out.println("offset" + offset);
			// System.out.println("bt"+branchTarget);
			// System.out.println("op1 " + op1 + " op2 " + op2 + " rd " +rd);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
