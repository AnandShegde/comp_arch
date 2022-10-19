package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		System.out.println("we are REG");
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			System.out.println("we are in REG");
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			String optCode = MA_RW_Latch.getOptCode();
			if(optCode.equals("11101")){

				//PC should be set to end+1 th statement
				int pc = containingProcessor.getRegisterFile().getProgramCounter();
				// containingProcessor.getRegisterFile().setProgramCounter(pc+1);
				Simulator.setSimulationComplete(true);
			}

			int destination = MA_RW_Latch.getDestination();
			int aluResult = MA_RW_Latch.getAluresult();
			boolean isWriteBack = MA_RW_Latch.getWriteBack();
			int loadResult = MA_RW_Latch.getLoadResult();

			if(isWriteBack){
				// containingProcessor.getRegisterFile().setValue(destination, aluResult);
				if(optCode.equals("10110")){

					// load
					containingProcessor.getRegisterFile().setValue(destination, loadResult);
				}
				else{

					// store
					containingProcessor.getRegisterFile().setValue(destination, aluResult);
				}
			}

			System.out.println(containingProcessor.getRegisterFile().getContentsAsString());

			//success
			MA_RW_Latch.setDestination(32);
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
