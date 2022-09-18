package generic;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import generic.Operand.OperandType;
import java.nio.ByteBuffer;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	
	public static char swap(char mychar) {
		if(mychar == '0'){
			return '1';
		}
		return '0';
    }


	public static String binaryConversion(String zeroOnes) {
        String dows = "";
		String eks = "";

        for (int j = 0; j < zeroOnes.length(); j++) {
            eks =eks + swap(zeroOnes.charAt(j));
        }

        StringBuilder labour = new StringBuilder(eks);

        boolean boolValue = false;

        for (int j = eks.length() - 1; j > 0; j--) {
            if (eks.charAt(j) == '1')
			{
                labour.setCharAt(j, '0');
            } else 
			{
                labour.setCharAt(j, '1');
                boolValue = true;
                break;
            }
        }
        if (!boolValue)
            labour.append("1", 0, 7);

        dows = labour.toString();

        return dows;
    }

	public static void setupSimulation(String apf)
	{	
		int pehlaCA = ParsedProgram.parseDataSection(apf);
		ParsedProgram.parseCodeSection(apf, pehlaCA);
		ParsedProgram.printState();
	}

	public static void assemble(String opf)
	{
		FileOutputStream outflowFS;
		try {
			
			outflowFS = new FileOutputStream(opf);

			BufferedOutputStream buffOS = new BufferedOutputStream(outflowFS);
			
			byte[] cA = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();

			buffOS.write(cA);
			
			for (int j=0; j<ParsedProgram.data.size(); j++) {
				byte[] value = ByteBuffer.allocate(4).putInt(ParsedProgram.data.get(j)).array();

				buffOS.write(value);
			}
			
			for (int j=0; j<ParsedProgram.code.size(); j++) 
			{
				int instruction_ocode = ParsedProgram.code.get(j).getOperationType().ordinal() ;

				String instBits = String.format("%05d", Integer.valueOf(Integer.toBinaryString(instruction_ocode)));

				if (instruction_ocode == 29) 
				{
					instBits = instBits + String.format("%027d", Integer.valueOf(Integer.toBinaryString(j)));
				} else if (instruction_ocode <= 21 && instruction_ocode % 2 != 0 || instruction_ocode == 22 || instruction_ocode == 23) 
				{
					int source_register_1 = ParsedProgram.code.get(j).getSourceOperand1().getValue();

					instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(source_register_1)));

					int destination_register = ParsedProgram.code.get(j).getDestinationOperand().getValue();

					instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(destination_register))); //r21

					int immediate_value = ParsedProgram.code.get(j).getSourceOperand2().getValue();

					instBits = instBits + String.format("%017d", Integer.valueOf(Integer.toBinaryString(immediate_value)));
				}else if (instruction_ocode <= 21 && instruction_ocode % 2 == 0) 
				{
					int source_register_1 = ParsedProgram.code.get(j).getSourceOperand1().getValue();

					instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(source_register_1)));

					int source_register_2 = ParsedProgram.code.get(j).getSourceOperand2().getValue();

					instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(source_register_2)));

					int destination_register = ParsedProgram.code.get(j).getDestinationOperand().getValue();

					instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(destination_register)));

					instBits = instBits + String.format("%012d", Integer.valueOf(Integer.toBinaryString(j)));
				}else if (instruction_ocode == 24) 
				{
					int myInstruction = ParsedProgram.code.get(j).getDestinationOperand().getValue();

					if (ParsedProgram.code.get(j).destinationOperand.getOperandType() == Operand.OperandType.Register) 
					{
						instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(myInstruction)));

						instBits = instBits + String.format("%022d", Integer.valueOf(Integer.toBinaryString(j)));
					} else 
					{
						instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(j)));

						String myop = ParsedProgram.code.get(j).getDestinationOperand().getLabelValue();

						int lv = ParsedProgram.symtab.get(myop);

						int programCounter = ParsedProgram.code.get(j).getProgramCounter();

						if (lv - programCounter < 0) 
						{
							String modifyBinary = String.format("%022d", Integer.valueOf(Integer.toBinaryString(programCounter - lv)));
							instBits = instBits + binaryConversion(modifyBinary);
						} else 
						{
							instBits = instBits + String.format("%022d", Integer.valueOf(Integer.toBinaryString(lv - programCounter)));
						}
					}
				}else 
				{
					int source_register_1 = ParsedProgram.code.get(j).getSourceOperand1().getValue();

					instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(source_register_1)));

					int source_register_2 = ParsedProgram.code.get(j).getSourceOperand2().getValue();

					instBits = instBits + String.format("%05d", Integer.valueOf(Integer.toBinaryString(source_register_2)));

					String myNewOp = ParsedProgram.code.get(j).getDestinationOperand().getLabelValue();

					int lv = ParsedProgram.symtab.get(myNewOp);

					int programCounter = ParsedProgram.code.get(j).getProgramCounter();

					if (lv - programCounter < 0)
					 {
						String to_complement = String.format("%017d", Integer.valueOf(Integer.toBinaryString(programCounter - lv)));
						
						instBits = instBits + binaryConversion(to_complement);
					} else {
						instBits = instBits + String.format("%017d", Integer.valueOf(Integer.toBinaryString(lv - programCounter)));
					}
				}
				int instruction_integer_matching = (int) Long.parseLong(instBits, 2);

				byte[] instruction_bits_matching = ByteBuffer.allocate(4).putInt(instruction_integer_matching).array();

				buffOS.write(instruction_bits_matching);
			}
			
			buffOS.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

