package generic;
import java.io.*;
import java.nio.ByteBuffer;

import processor.Clock;
import processor.Processor;
import processor.pipeline.IF_EnableLatchType;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		simulationComplete = false;
		eventQueue = new EventQueue();
	}

	public static EventQueue getEventQueue(){
		return eventQueue;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */

		 try {
			InputStream inputfile = new BufferedInputStream(new FileInputStream(assemblyProgramFile));
			// InputStream inputfile = new FileInputStream(assemblyProgramFile);

			// read 4 bytes at a time.
			byte[] buffer = new byte[4];
			
			// write the data into the memory starting from 0
			inputfile.read(buffer);
			int pc = ByteBuffer.wrap(buffer).getInt();
			processor.getRegisterFile().setProgramCounter(pc);

			int lineNo = 0;
			
			int byteRead = -1;
			while((byteRead = inputfile.read(buffer)) != -1)
			{
				// int content = buffer[0]<<24 + buffer[1]<<16 + buffer[2]<<8 + buffer[3];
				int content = ByteBuffer.wrap(buffer).getInt();

				// System.out.println(buffer[0]<<24);

				processor.getMainMemory().setWord(lineNo, content);
				lineNo++;
			}

			// System.out.println(processor.getMainMemory().getContentsAsString(0, 50));

			//TODO : move only data to some part
			// int numOfData = processor.getRegisterFile().getProgramCounter();			
			// while(numOfData > 0){
			// 	processor.getMainMemory().setWord(lineNo, byteRead);
			// 	numOfData--;
			// }

			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);	
			// processor.getRegisterFile().setValue(3, 12);	
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			// System.out.println("simulator");
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			System.out.println("\n\n\n\n\n\n");
		}
		// TODO
		// set statistics
		Statistics statistics = new Statistics();
		statistics.setNumberOfInstructions(processor.getIfEnableLatch().getNumberOfInstructions());
		statistics.setNumberOfCycles((int) Clock.getCurrentTime());
		statistics.setThroughput();
		// statistics.printStatistics();
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
