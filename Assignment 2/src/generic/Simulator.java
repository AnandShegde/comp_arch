package generic;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import generic.Operand.OperandType;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;

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

	public static String binaryofint(int val, int size){
		String binaryVal = Long.toBinaryString( Integer.toUnsignedLong(val) | 0x100000000L ).substring(1);
        return binaryVal.substring(32-size);
	}
	
	public static String ConvertLabelled(Operand obj, int bitlen){
		if(obj == null) return binaryofint(0, bitlen);
		if(obj.getOperandType() == OperandType.Label){
			return binaryofint(ParsedProgram.symtab.get(obj.getLabelValue()), bitlen);
		}
		return binaryofint(obj.value, bitlen);
	}

	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	public static String r3type(Instruction ins){
		String encoded = "";
		encoded += ConvertLabelled(ins.sourceOperand1, 5);
		encoded += ConvertLabelled(ins.sourceOperand2, 5);
		encoded += ConvertLabelled(ins.destinationOperand, 5);
		encoded += binaryofint(0, 12);
		return encoded;
	}

	public static String r2type(Instruction ins){
		String encoded = "";
		encoded += ConvertLabelled(ins.sourceOperand1, 5);
		encoded += ConvertLabelled(ins.destinationOperand, 5);
		encoded += ConvertLabelled(ins.sourceOperand2, 17);
		return encoded;
	}

	public static String branchType(Instruction ins){
		String encoded = "";
		encoded += ConvertLabelled(ins.sourceOperand1, 5);
		encoded += ConvertLabelled(ins.sourceOperand2, 5);
		Integer immediate = Integer.parseInt(ConvertLabelled(ins.destinationOperand,17),2) - ins.programCounter;
		encoded += binaryofint(immediate, 17);
		return encoded;
	}

	public static String r1type(Instruction ins){
		String encoded = "";
		if(ins.destinationOperand.getOperandType() == Operand.OperandType.Register){
			encoded += ConvertLabelled(ins.destinationOperand, 5);
			encoded += binaryofint(0, 22);
			return encoded;
		}
		else{
			encoded += binaryofint(0, 5);
			Integer immediate = Integer.parseInt(ConvertLabelled(ins.destinationOperand,22),2) - ins.programCounter;
			encoded += binaryofint(immediate, 22);
		}
		return encoded;
	}
	
	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		//2. write the firstCodeAddress to the file
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file

		try {
			FileOutputStream file = new FileOutputStream(objectProgramFile);

			byte [] codeAddress = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
			try {
				file.write(codeAddress);
			} catch (IOException e) {

				e.printStackTrace();
			}

			for(int i = 0; i < ParsedProgram.data.size(); i++){
				byte [] data = ByteBuffer.allocate(4).putInt(ParsedProgram.data.get(i)).array();
				try {
					file.write(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < ParsedProgram.code.size(); i++) {

				Instruction ins = ParsedProgram.code.get(i);
				int optCode = Arrays.asList(ins.operationType.values()).indexOf(ins.getOperationType());
				String binary = binaryofint(optCode, 5);
				Integer instructionType = insType.get(binary);
				if(instructionType == 3){						//R3 Type
					binary += r3type(ins);
				}
				else if(instructionType == 2){       			//R2I Type
					binary += r2type(ins);
				}
				else if(instructionType == 21){					//R2I - branching type
					binary += branchType(ins);
				}
				else if(instructionType == 1){		     		//R1 Type
					binary += r1type(ins);
				}
				else{
					binary += binaryofint(0,27);      //Only optcode
				}
				
				byte [] data = ByteBuffer.allocate(4).putInt((int)Long.parseLong(binary,2)).array();

				try {
					file.write(data);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
