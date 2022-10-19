package processor.pipeline;

import processor.Processor;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;

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
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		System.out.println("We are in EX");
		if(OF_EX_Latch.isEX_enable()){

			String optCode = OF_EX_Latch.getOptCode();
			int branchTarget = OF_EX_Latch.getBranchTarget();
			int rs1 = OF_EX_Latch.getOp1();
			int rs2 = OF_EX_Latch.getOp2();
			int rdval = OF_EX_Latch.getRd();
			int destination = OF_EX_Latch.getDestination();
			int aluResult=0;
			int immediate = OF_EX_Latch.getImmediate();
			boolean isWriteback = false;
			boolean isBranchTaken = false;

			System.out.println("rs1 ="+rs1+" rs2="+rs2+" ");;
			System.out.println("rdval "+rdval+" dest "+ destination + " imm "+immediate);
			System.out.println("bt "+branchTarget);
			// beg, bne, blt, bgt
			if(insType.get(optCode) == 21){
				
				switch(optCode){	

					//beq
					case "11001":
					if(rs1 == rdval){
						isBranchTaken = true;
					}
					break;

					//bne
					case "11010":
					if(rs1 != rdval){
						isBranchTaken = true;
					}
					break;

					// blt
					case "11011":
						if(rs1<rdval)
						{
							isBranchTaken = true;
						} 
						break;
					// bgt
					case "11100":
						if(rs1>rdval){
							isBranchTaken = true;
						}
					break;
							
				}
			}
			
			
			
			else if(insType.get(optCode) == 2){
				isWriteback = true;
				switch(optCode){
					// addi
					case "00001":
						aluResult = rs1+immediate;
					
					break;
					// subi
					case "00011":
						aluResult = rs1- immediate;
					
					break;
					// muli
					case "00101":
						aluResult = rs1*immediate;
					break;
					// divi
					case "00111":
						aluResult = rs1/immediate;
						containingProcessor.getRegisterFile().setValue(31, rs1%immediate);
					break;
					// andi
					case "01001":
						aluResult = rs1 & immediate;
					break;
					// ori
					case "01011":
						aluResult = rs1 | immediate;
					break;
					// xori
					case "01101":
						aluResult = rs1 ^ immediate;
					break;
					// slt
					case "01111":
						aluResult = (rs1<immediate) ? 1:0;
					break;
					// sll
					case "10001":
						aluResult = rs1<<immediate;
					break;
					// rsli
					case "10011":
						aluResult = rs1>>> immediate;
					
					break;
					// rsai
					case "10101":
						aluResult = rs1>> immediate;
					break;

					// load
					case "10110":
						aluResult = rs1 + immediate;
					break;

					// store
					case "10111":
						aluResult = rdval + immediate;
						isWriteback = false;
					break;
				}

				
			}

			else if(insType.get(optCode) == 3){
					
				isWriteback = true;
				switch(optCode){

					//add
					case "00000":
					aluResult = rs1 + rs2;
					break;
					
					//sub
					case "00010":
					aluResult = rs1 - rs2;
					break;

					//mul
					case "00100":
					aluResult = rs1*rs2;
					break;
						
					//div
					case "00110":
					aluResult = (int)rs1/rs2;
					
					//x31 write remainder here 
					containingProcessor.getRegisterFile().setValue(31, rs1%rs2);
					break;
					
					//and
					case "01000":
					aluResult = rs1 & rs2;
					break;

					//or
					case "01010":
					aluResult = rs1 | rs2;
					break;
					
					//xor
					case "01100":
					aluResult = rs1^rs2;
					break;
					
					//slt
					case "01110":
					aluResult = rs1 < rs2 ? 1 : 0;
					break;
					
					//sll
					case "10000":
					aluResult = rs1 << rs2;
					break;

					//srl
					case "10010":
					aluResult = rs1 >>> rs2;
					break;

					//sra
					case "10100":
					aluResult = rs1 >> rs2;
					break;
				}
			}

			// jmp / jump
			else if(insType.get(optCode) == 1){
				isBranchTaken = true;
			}

			// set all the things in the next latch.
			EX_MA_Latch.setOptCode(optCode);
			EX_MA_Latch.setAluResult(aluResult);
			EX_MA_Latch.setDestination(destination);
			EX_MA_Latch.setWriteBack(isWriteback);
			EX_MA_Latch.setrs1(rs1);

			System.out.println("alu "+ aluResult);

			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_enable(false);

			//Success
			//Erasing latch data
			OF_EX_Latch.setDestination(32);

			//setiing pc if branch taken
			if(isBranchTaken){
				containingProcessor.getRegisterFile().setProgramCounter(branchTarget);
				OF_EX_Latch.setIsBranchTaken(true);
			}
		// if OF_EX enabled
		}

	// performEX
	}

}
