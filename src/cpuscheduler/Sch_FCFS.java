/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cpuscheduler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @author soheilchangizi
 */
public class Sch_FCFS extends Scheduler{
    
    private PriorityQueue<Process> pq;
    
    public Sch_FCFS() {
        pq = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return (o1.getArrivalTime() >= o2.getArrivalTime()) ? 1 : -1;
            }
        });
    }
    
    @Override
    public void addProc(Process p) {
        pq.add(p);
    }
    
    @Override
    public boolean removeProc(Process p) {
        return pq.remove(p);
    }
    
    @Override
    public void setScheduler(Scheduler method) {
        Iterator<Process> itr = pq.iterator();
        while(itr.hasNext()){
            method.addProc(itr.next());
            itr.remove();
        }
    }
    
    @Override
    public Process getNextProc(double currentTime) {
        return pq.peek();
    }
    
    @Override
    public String getName() {
        return "FCFS";
    }

    @Override
    public boolean isProcLeft() {
        return !pq.isEmpty();
    }
    
}
