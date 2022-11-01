package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	
	public void performIF()
	{
		System.out.println("ins fetch");
		if(IF_EnableLatch.isIF_enable())
		{
			if(IF_EnableLatch.isIFBusy()){
				return;
			}
			else{
				System.out.println("ready to send request");
				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				Simulator.getEventQueue().addEvent(
					new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,this,containingProcessor.getMainMemory() , currentPC)
				);

				IF_EnableLatch.setIFBusy(true);
				// int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				containingProcessor.getRegisterFile().setProgramCounter(currentPC+1);
			
			}
		}
	}

	@Override
	public void handleEvent(Event e) {
		System.out.println("IF he");
		if(IF_OF_Latch.isOFBusy()){
			System.out.println("IF he busy");
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else{
			System.out.println("If he mem resp");
			MemoryResponseEvent event = (MemoryResponseEvent)e;
			IF_OF_Latch.setInstruction(event.getValue());

			// int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			// containingProcessor.getRegisterFile().setProgramCounter(currentPC+1);

			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIFBusy(false);

			//lets see bro
			// IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setResponse(true);

			// // MemoryResponseEvent event = MemoryResponseEvent(e.getEventTime(),)
			// // event.getAddressToReadFrom();
			// // IF_OF_Latch.setInstruction(event.getValue());
			// // containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			
			// IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);
			
			//incementing instructions
			// IF_EnableLatch.incrementIns();

		}

		
	}

}
