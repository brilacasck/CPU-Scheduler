/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cpuscheduler;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author soheilchangizi
 */
public class CPU {
    
    private Scheduler sm;
    private double cs = 0.4;
    private int csCount = 0;
    private double currentTime = 0.0;
    private String simData = "";
    private String report = "";
    
    private ArrayList<Process> allProcs = new ArrayList<>();
    private ArrayList<Process> procQueue = new ArrayList<>();
    private ArrayList<Process> readyQueue = new ArrayList<>();
    private static ArrayList<String> randomData = new ArrayList<>();
    private static ArrayList<String> levels = new ArrayList<>();
    private Process preProc = null;
    
    private Process activeProc = null;
    
    private double AWT = 0.0;
    private double ATT = 0.0;
    private double Util = 0.0;
    private double Potency = 0.0;
    
    
    CPU(String data, String schName) {
        levels.clear();
        sm = setSchMethod(schName);
        sm.setScheduler(sm);
        activeProc = null;
        Process proc = null;
        double b = 0, d = 0;
        int p = 0;
        String[] lines = data.split("\n");
        int i = 1;
        for (String line : lines) {
            String[] split = line.split("\\s+");
            b = Double.parseDouble(split[0]);
            d = Double.parseDouble(split[1]);
            p = (int)Double.parseDouble(split[2]);
            proc = new Process(i, b, d, p);
            proc.setLevel((int)Double.parseDouble(split[3]));
            i++;
            allProcs.add(proc);
        }
        initProcQueue(allProcs);
    }
    
    CPU(String data, ArrayList<String> schName, String isPreemptive) {
        levels.addAll(schName);
        sm = setSchMethod(isPreemptive + "Multi Level");
        sm.setScheduler(sm);
        activeProc = null;
        Process proc = null;
        double b = 0, d = 0;
        int p = 0;
        String[] lines = data.split("\n");
        int i = 1;
        for (String line : lines) {
            String[] split = line.split("\\s+");
            b = Double.parseDouble(split[0]);
            d = Double.parseDouble(split[1]);
            p = (int)Double.parseDouble(split[2]);
            proc = new Process(i, b, d, p);
            proc.setLevel((int)Double.parseDouble(split[3]));
            i++;
            allProcs.add(proc);
        }
        initProcQueue(allProcs);
    }
    
    public static void randProc(int processNum, boolean isMulti, int levelNum) {
        Process p;
        randomData.clear();
        for (int i = 0; i < processNum; i++) {
            p = new Process(i+1, 8.33, 2.1, 2.46, 0.7);
            p.setLevel(new Random().nextInt(levelNum)+1);
            randomData.add(p.getBurstTime() + " " + p.getDelayTime() 
                    + " " + p.getPriority() + " " + p.getLevel());
        }
    }
    
    private void initProcQueue(ArrayList<Process> allProcess) {
        Process p;
        double arrivalTime = 0;
        for (int i = 0; i < allProcess.size(); i++) {
            p = (Process) allProcess.get(i);
            arrivalTime += p.getDelayTime();
            p.setArrivalTime(arrivalTime);
            procQueue.add(p);
        }
    }
    
    private void initReadyQueue() {
        Process p;
        for (int i = 0; i < procQueue.size(); i++) {
            p = (Process) procQueue.get(i);
            if (p.getArrivalTime() - currentTime < 1e-1) {
                readyQueue.add(p);
                sm.addProc(p);
            }
        }
        
    }
    
    private void refReadyQueue() {
        Process p;
        for (int i = 0; i < readyQueue.size(); i++) {
            p = (Process) readyQueue.get(i);
            if (p.isIsFinished() == true) {
                readyQueue.remove(i);
                sm.removeProc(p);
            }
        }
    }
    
    private void refProcQueue() {
        Process p;
        for (int i = 0; i < procQueue.size(); i++) {
            p = (Process) procQueue.get(i);
            if (p.isIsFinished() == true) {
                procQueue.remove(i);
                sm.removeProc(p);
            }
        }
    }
    
    void Schedule() {
        Process p = null;
        activeProc = sm.getNextProc(currentTime);
        if(activeProc != preProc && preProc != null){
            if(cs > 0.4) currentTime += (cs - 0.4);
            csCount++;
        }
        if (activeProc != null){
            activeProc.executing(currentTime);
            simData += activeProc.toString();
            preProc = activeProc;
        }
        for (int i = 0; i < readyQueue.size(); ++i) {
            p = (Process) readyQueue.get(i);
            if (p.getPID() != activeProc.getPID()) {
                p.waiting(currentTime);
            }
        }
    }
    
    private void report() {
        
        Process p = null;
        int procCount = 0;
        
        for (int i = 0; i < allProcs.size(); i++) {
            p = (Process) allProcs.get(i);
            
            if (p.isIsFinished()) {
                procCount++;
                double waited = p.getWaitTime();
                double turned = p.getTurnAroundTime();
                AWT += waited;
                ATT += turned;
            }
        }
        
        if (procCount > 0) {
            AWT /= (double) procCount;
            ATT /= (double) procCount;
        } else {
            AWT = 0.0;
            ATT = 0.0;
        }
        
        Util = ((currentTime - (cs * csCount)) / currentTime) * 100;
        Potency = currentTime / procCount;
        
        report = "AWT : " + String.format("%.1f", AWT) + "\nATT : " + String.format("%.1f", ATT)
                + "\nUtil : " + String.format("%.1f", Util) + "%\nPotency : " + String.format("%.1f", Potency);
    }
    
    
    public static Scheduler setSchMethod(String method) {
        
        String split[] = method.split(":");
        
        switch(split[0]){
            case "FCFS":
                return new Sch_FCFS();
            case "PSJF":
                return new Sch_SJF(true);
            case "SJF":
                return new Sch_SJF(false);
            case "Preemptive Priority":
                return new Sch_Priority(true);
            case "Priority":
                return new Sch_Priority(false);
            case "Round Robin":
                return new Sch_RR(Double.valueOf(split[1]));
            case "Lottery":
                return new Sch_Lottery();
            case "Multi Level":
                return new Sch_Multilevel(levels, false);
            case "Preemptive Multi Level":
                return new Sch_Multilevel(levels, true);
        }
        return null;
    }
    
    
    public void Simulate(){
        
        boolean check;
        check = true;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        
        while(check){
            if (procQueue.isEmpty()) {
                check = false;
            } else {
                initReadyQueue();
                check = true;
                if (!readyQueue.isEmpty()) {
                    Schedule();
                    refProcQueue();
                    refReadyQueue();
                }
                currentTime+=1e-1;
                currentTime = Double.valueOf(df.format(currentTime));
            }
        }
        report();
        resetAll();
    }
    
    
    public void resetAll() {
        Process p;
        
        activeProc = null;
        sm = null;
        currentTime = 0;
        csCount = 0;
        AWT = 0.0;
        ATT = 0.0;
        Util = 0.0;
        Potency = 0.0;
        
        
        for (int i = 0; i < allProcs.size(); i++) {
            p = (Process) allProcs.get(i);
            p.resetAll();
        }
        
        procQueue.clear();
        readyQueue.clear();
        initProcQueue(allProcs);
    }
    
    public Process getActiveProc() {
        return activeProc;
    }
    
    public double getCurrentTime() {
        return currentTime;
    }
    
    public String getSimData() {
        return simData;
    }
    
    public String getReport() {
        return report;
    }
    
    public void resetSimData(){
        this.simData = "";
    }
    
    public void resetReport(){
        this.report = "";
    }
    
    public void setCs(double cs) {
        if(cs > 0.4) this.cs = cs;
    }

    public ArrayList<Process> getAllProcs() {
        return allProcs;
    }
    

    public static ArrayList<String> getRandomData() {
        return randomData;
    }
    
}
