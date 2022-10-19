package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
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
			System.out.println("MA Enabled");
			if(EX_MA_Latch.IsMABusy()){
				System.out.println("MA busy");
				return;
			}
			else{
				System.out.println("MA not busy");
				// System.out.println("we are in MEM");
				int aluResult = EX_MA_Latch.getAluresult();
				int destination = EX_MA_Latch.getDestination();
				boolean isWriteback = EX_MA_Latch.getWriteBack();
				String optCode = EX_MA_Latch.getOptCode();
				int rs1 = EX_MA_Latch.getrs1();
				// int loadResult = 0;
				// load
				if(optCode.equals("10110")){
					int memloc = aluResult;
					
					// loadResult= containingProcessor.getMainMemory().getWord(
					// 	memloc
					// 	);
					Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency,
						 	this,
							containingProcessor.getMainMemory(), 
							memloc
							)
					);

					EX_MA_Latch.setMABusy(true);
					System.out.println("REq mem read in MA");

				}

				//store
				else if(optCode.equals("10111")){
					// System.out.println("storing in mem "+aluResult+" rs1 "+rs1);
					int memLoc = aluResult;
					// containingProcessor.getMainMemory().setWord(memLoc, rs1);
					Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency,
							this, 
							containingProcessor.getMainMemory(), 
							memLoc, 
							rs1)						
					);
					System.out.println("req mem write in ma");
					EX_MA_Latch.setMABusy(true);
				}
				else{

					MA_RW_Latch.setAluResult(aluResult);
					MA_RW_Latch.setWriteBack(isWriteback);
					MA_RW_Latch.setDestination(destination);
					MA_RW_Latch.setOptCode(optCode);

					MA_RW_Latch.setRW_enable(true);
					EX_MA_Latch.setMA_enable(false);
					// EX_MA_Latch.setMABusy(false);
					///success
					EX_MA_Latch.setDestination(32);

				}

				
			}
			

			
		}
	}

	@Override
	public void handleEvent(Event e) {
		// TODO Auto-generated method stub
		System.out.println("handle event MA");
		System.out.println(e.getEventType());
		if(e.getEventType() == EventType.MemoryResponse){

			System.out.println("MEm response");

			MemoryResponseEvent event = (MemoryResponseEvent)e;

			

			int aluResult = EX_MA_Latch.getAluresult();
			boolean isWriteback = EX_MA_Latch.getWriteBack();
			int destination = EX_MA_Latch.getDestination();
			String optCode = EX_MA_Latch.getOptCode();
			int loadResult = event.getValue();
			
			MA_RW_Latch.setAluResult(aluResult);
			MA_RW_Latch.setLoadResult(loadResult);
			MA_RW_Latch.setWriteBack(isWriteback);
			MA_RW_Latch.setDestination(destination);
			MA_RW_Latch.setOptCode(optCode);

			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
			EX_MA_Latch.setMABusy(false);
			///success
			EX_MA_Latch.setDestination(32);
		}
			// EX_MA_Latch.setDestination(32);

			// MA_RW_Latch.setAluResult(aluResult);
			// MA_RW_Latch.setLoadResult(loadResult);
			// MA_RW_Latch.setWriteBack(isWriteback);
			// MA_RW_Latch.setDestination(destination);
			// MA_RW_Latch.setOptCode(optCode);

			// MA_RW_Latch.setRW_enable(true);
			// EX_MA_Latch.setMA_enable(false);
	}

}
