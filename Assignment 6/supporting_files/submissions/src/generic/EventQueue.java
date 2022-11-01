package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import processor.Clock;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event)
	{
		queue.add(event);
	}

	public void processEvents()
	{
		System.out.println("Process events");
		System.out.println(queue.size());
		System.out.println(Clock.getCurrentTime());
		if(queue.isEmpty() == false ){
			System.out.println("top element's time");
			System.out.println(queue.peek().getEventTime());
			
		}
		

		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			System.out.println("queue while loop");
			
			Event event = queue.poll();
			System.out.println(event.getEventType());
			System.out.println(event.getEventTime());
			System.out.println(Clock.getCurrentTime());
			event.getProcessingElement().handleEvent(event);
		}
	}
}

class EventComparator implements Comparator<Event>
{
	@Override
    public int compare(Event x, Event y)
    {
		if(x.getEventTime() < y.getEventTime())
		{
			return -1;
		}
		else if(x.getEventTime() > y.getEventTime())
		{
			return 1;
		}
		else
		{
			return 0;
		}
    }
}
