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
    private static final int priorRefresh = 10;
    
    public Sch_Multilevel(ArrayList<Scheduler> lvls) {
        levels = lvls;
        activeProc = null;
    }
    
    @Override
    public void addProc(Process p) {
        levels.get(p.getLevel()).addProc(p);
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
        if(activeProc != null){
            activeProc.setPriority(activeProc.getPriority()+1);
            if(activeProc.getPriority() == priorRefresh && activeProc.getLevel() > 0){
                levels.get(activeProc.getLevel()).removeProc(activeProc);
                activeProc.setLevel(activeProc.getLevel()-1);
                activeProc.setPriority(0);
                levels.get(activeProc.getLevel()).addProc(activeProc);
            }else if(activeProc.getLevel() < levels.size()-1){
                levels.get(activeProc.getLevel()).removeProc(activeProc);
                activeProc.setLevel(activeProc.getLevel()+1);
                levels.get(activeProc.getLevel()).addProc(activeProc);
            }
        }
        if (activeProc == null || activeProc.isIsFinished()) {
            activeProc = null;
            for(int i=0; i<levels.size(); i++){
                if((activeProc = levels.get(i).getNextProc(currentTime)) != null){
                    break;
                }
            }
        }
        return activeProc;
    }
    
    public String getName() {
        return "Multilevel Priority";
    }
    
    
}
