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
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			String optCode = MA_RW_Latch.getOptCode();
			if(optCode.equals("11101")){
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
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
