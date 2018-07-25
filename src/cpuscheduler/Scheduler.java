/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpuscheduler;

/**
 *
 * @author soheilchangizi
 */
public  abstract class Scheduler {
    
    protected Process activeProc;
    public abstract void addProc(Process p);
    public abstract boolean removeProc(Process p);
    public abstract void setScheduler(Scheduler method);
    public abstract Process getNextProc(double currentTime);
    public abstract boolean isProcLeft();
    public abstract String getName();
    public boolean isJobFinished(){
		if (activeProc != null) return activeProc.isIsFinished();
		return true;
    }
    
}
