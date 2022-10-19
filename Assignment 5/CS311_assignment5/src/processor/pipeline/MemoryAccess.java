package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		System.out.println("we are MEM");
		if(EX_MA_Latch.isMA_enable()){
			System.out.println("we are in MEM");
			int aluResult = EX_MA_Latch.getAluresult();
			int destination = EX_MA_Latch.getDestination();
			boolean isWriteback = EX_MA_Latch.getWriteBack();
			String optCode = EX_MA_Latch.getOptCode();
			int rs1 = EX_MA_Latch.getrs1();
			int loadResult = 0;
			// load
			if(optCode.equals("10110")){
				int memloc = aluResult;
				
				loadResult= containingProcessor.getMainMemory().getWord(
					memloc
					);

			}

			//store
			if(optCode.equals("10111")){
				System.out.println("storing in mem "+aluResult+" rs1 "+rs1);
				int memLoc = aluResult;
				containingProcessor.getMainMemory().setWord(memLoc, rs1);
			}

			//succes
			EX_MA_Latch.setDestination(32);

			MA_RW_Latch.setAluResult(aluResult);
			MA_RW_Latch.setLoadResult(loadResult);
			MA_RW_Latch.setWriteBack(isWriteback);
			MA_RW_Latch.setDestination(destination);
			MA_RW_Latch.setOptCode(optCode);

			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
