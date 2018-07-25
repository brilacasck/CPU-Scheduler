/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpuscheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @author soheilchangizi
 */
public class Sch_RR extends Scheduler{
    
    private double quantum;
    private PriorityQueue<Process> pq;
    private ArrayList<Process> rrList;
    private int currProc;
    private double curTimeQuantum;
    
    Sch_RR(double q) {
        pq = new PriorityQueue<Process>(new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return (o1.getArrivalTime() >= o2.getArrivalTime()) ? 1 : -1;
            }
        });
        rrList = new ArrayList<Process>();
        quantum = q;
        curTimeQuantum = 0.0;
        activeProc = null;
        currProc = 0;
    }
    
    
    @Override
    public void addProc(Process p) {
        pq.add(p);
    }
    
    @Override
    public boolean removeProc(Process p) {
        return (pq.remove(p) || rrList.remove(p));
    }
    
    @Override
    public void setScheduler(Scheduler method) {
        while (pq.size() > 0) {
            rrList.add(pq.poll());
        }
        Iterator<Process> itr = pq.iterator();
        while(itr.hasNext()){
            method.addProc(itr.next());
            itr.remove();
        }
    }
    
    @Override
    public Process getNextProc(double currentTime) {
        while (pq.size() > 0) {
            rrList.add(pq.poll());
        }
        
        if (quantum <= 1e-1) {
            activeProc = null;
        } else if (rrList.size() > 0) {
            if (activeProc == null) {
                activeProc = rrList.get(currProc);
                curTimeQuantum = 0;
            } else if ((quantum - curTimeQuantum < 1e-1) && !activeProc.isIsFinished()) {
                currProc = (currProc + 1) % rrList.size();
                activeProc = rrList.get(currProc);
                curTimeQuantum = 0;
            } else if (activeProc.isIsFinished()) {
                if (currProc == rrList.size()) {
                    currProc--;
                }
                activeProc = rrList.get(currProc);
                curTimeQuantum = 0;
            }
            curTimeQuantum += 1e-1;
        } else {
            activeProc = null;
        }
        return activeProc;
        
    }
    
    @Override
    public String getName() {
        return "RR";
    }
    
    @Override
    public boolean isProcLeft() {
        return !pq.isEmpty();
    }
    
}
