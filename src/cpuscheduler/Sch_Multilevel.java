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
public class Sch_Multilevel extends Scheduler{
    
    private ArrayList<Scheduler> levels;
    private boolean preemptive;
    
    public Sch_Multilevel(ArrayList<String> lvls, boolean isPreemptive) {
        levels = new ArrayList<>();
        for (String lvl : lvls) {
            levels.add(CPU.setSchMethod(lvl));
        }
        this.preemptive = isPreemptive;
        activeProc = null;
    }
    
    @Override
    public void addProc(Process p) {
        levels.get(p.getLevel()-1).addProc(p);
    }
    
    @Override
    public boolean removeProc(Process p) {
        for(int i=0; i<levels.size(); i++){
            if(levels.get(i).removeProc(p)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void setScheduler(Scheduler method) {
        for(int i=0; i<levels.size(); i++){
            levels.get(i).setScheduler(method);
        }
    }
    
    @Override
    public Process getNextProc(double currentTime) {
        if(activeProc != null && activeProc.isIsFinished()){
            activeProc = null;
        }
        if(isPreemptive() || activeProc == null){
            for (int i = 0; i < levels.size(); i++) {
                if(levels.get(i).isProcLeft()){
                    activeProc = levels.get(i).getNextProc(currentTime);
                    break;
                }
            }
        }
        return activeProc;
    }
    
    public String getName() {
        return !isPreemptive() ? "Multi Level" : "Preemptive Multi Level";
    }
    
    public boolean isPreemptive() {
        return preemptive;
    }
    
    @Override
    public boolean isProcLeft() {
        return false;
    }
}
