package processor.cachesystem;

import java.io.ObjectInputFilter.Config;
import java.util.List;
import java.util.Vector;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Processor;
import processor.Clock;

public class Cache implements Element{
    
    cacheLine cache[][];

    // List<List<Integer>> rank;

    int noSets, noWays, address;
    Element requestingElement;
    Processor containingProcessor;
    Vector<Vector<Integer>> rank;


    public Cache( Processor containingProcessor, int noSets, int noWays){
        cache = new cacheLine[noSets][noWays];
        rank = new Vector<>();
        for(int i=0;i<noSets;i++){
            Vector<Integer> tempRank = new Vector<>();
            for(int j=0;j<noWays;j++){
                cache[i][j] = new cacheLine();
                tempRank.add(j);
            }
            rank.add(tempRank);
        }
    
        this.noSets = noSets;
        this.noWays = noWays;
        this.containingProcessor = containingProcessor;

    }

    public int getIndex(int address){
        return address%noSets;
    }

    public int getTag(int address){
        return address/noSets;
    }

    public boolean lookup(int address){
        int tag = getTag(address);
        int index = getIndex(address);
        for(cacheLine c : cache[index]){
            if(!c.isEmpty() && c.getTag() == tag) return true;
        }
        return false;
    }

    public int cacheRead(int address){
        int tag = getTag(address);
        int index = getIndex(address);
        for(cacheLine c : cache[index]){
            if(!c.isEmpty() && c.getTag() == tag) return c.getData();
        }
        return -1;
    }

    public void handleCacheMiss(int address){
       
        Simulator.getEventQueue().addEvent(
            new MemoryReadEvent(
                Configuration.mainMemoryLatency + Clock.getCurrentTime(),
                this,
                containingProcessor.getMainMemory(),
                address
            )
        );
    }

    public int findIndex(int tag, int ind){

        for(int i=0;i<noWays;i++){
                if(!cache[ind][i].isEmpty() && cache[ind][i].getTag() == tag) return i;
        }
        return -1;
    }

    private void implementLRU(int value){

        int tag = getTag(address);
        int index = getIndex(address);
        int tagRank = findIndex(tag, index);

        System.out.println("address tag index tagrank "+address+" "+tag+" "+index+" "+tagRank);

        //in cache
        if(tagRank != -1){
            Integer associativityIndex = rank.elementAt(index).elementAt(tagRank);
            rank.elementAt(index).remove(tagRank);
            rank.elementAt(index).add(0, associativityIndex);  
        }

        //not in cache
        else{
            
            Integer associativityIndex = rank.elementAt(index).elementAt(noWays - 1);
            cache[index][associativityIndex].setTag(tag);
            cache[index][associativityIndex].setData(value);
            rank.elementAt(index).remove(noWays-1);
            rank.elementAt(index).add(0, associativityIndex);  
        }

        // for(int i = 0; i < noWays; i++){
            System.out.println(rank.elementAt(index).toString());
        // }
        System.out.println("Cache contents\n\n\n");
         for(int i = 0; i < noWays; i++){
            System.out.println(i);
            System.out.println(cache[index][i].getTag());
            System.out.println(cache[index][i].getData());
        }
            

    }
    

    @Override
    public void handleEvent(Event e) {
         System.out.println("Cache handle event");
        if(e.getEventType() == EventType.MemoryRead){
            System.out.println("mem read");
            MemoryReadEvent event  = (MemoryReadEvent)e;
            this.requestingElement = event.getRequestingElement();
            this.address = event.getAddressToReadFrom();
            System.out.println("address "+address);
            if(lookup(address)){
                int data = cacheRead(address);
                System.out.println("lookup true data: "+data);
                //lru
                implementLRU(data);
                Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                        Clock.getCurrentTime(),
                        this,
                        this.requestingElement,
                        data
                        )
                );
            }
            else{
                System.out.println("handle misss");
                handleCacheMiss(address);
            }
        }
        else if(e.getEventType() == EventType.MemoryWrite){
            
            //write through


        }
        else if(e.getEventType() == EventType.MemoryResponse){
            System.out.println("mem respone");
            MemoryResponseEvent event = (MemoryResponseEvent)e;

            //if policy is LRU
            
            int value = event.getValue();

            System.out.println("value "+value);

            //lru
            implementLRU(value);
            Simulator.getEventQueue().addEvent(
                new MemoryResponseEvent(
                    Clock.getCurrentTime(),
                    this,
                    this.requestingElement,
                    value
                    )
            );
                                
            
        }
    }

}
