/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpuscheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author soheilchangizi
 */
public class Sch_Lottery extends Scheduler {

    private ArrayList<Process> procList;
    private Random gen;
    
    public Sch_Lottery() {
        procList = new ArrayList<>();
        gen = new Random(System.nanoTime());
    }
    
    @Override
    public void addProc(Process p) {
        procList.add(p);
    }
    
    @Override
    public boolean removeProc(Process p) {
        return procList.remove(p);
    }
    
    @Override
    public void setScheduler(Scheduler method) {
        Iterator<Process> itr = procList.iterator();
        while(itr.hasNext()){
            method.addProc(itr.next());
            itr.remove();
        }
    }
    
    @Override
    public Process getNextProc(double currentTime) {
        return procList.get(gen.nextInt(procList.size()));
    }
    
    @Override
    public String getName() {
        return "Lottery";
    }
    
    @Override
    public boolean isProcLeft() {
        return !procList.isEmpty();
    }
    
}
