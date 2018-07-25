# CPU-Scheduler
> simulating scheduling algorithms of operating system for processes e.g. Priority Scheduling, Multi Level Scheduling, ...

![Overall1](./assets/1_overall.png)
![Overall2](./assets/2_overall.png)

<hr />

*
*   @ASCK-TEAM
*

## Getting Started
This repository exposed here, is actually a university project of the course `operating system`

And implemented with `Java` language.

    
## Documentation 

The hierarchy display of the classes is demonstrated here:


----cpuscheduler

-----------| 

-----------src

------------------|

------------------Process                //data of each process e.g. start time, burst time, ...

------------------CPU                    //cpu for simulating schedule

------------------Scheduler              //abstract class for primitive methods and fields

------------------Sch_FCFS               //First Come First Serve Scheduler

------------------Sch_SJF                //Shortest Job First Scheduler (can be preemptive)

------------------Sch_Priority           //Priority Scheduler (can be preemptive)

------------------Sch_RR                 //Round Robin Scheduler 

------------------Sch_Lottery            //Lottery Scheduler

------------------Sch_Multilevel         //Multi Level Scheduler (can be preemptive)

------------------FXMLDocumentController //controller for main gui 

------------------SimulationController   //controller for simulation gui 

----/

<hr />

> NOTES

***Note that simulation time unit is 0.1 so burst time or delay time must be multiple of 0.1***

***Note that order of each line specifies order of process arrival***

***Note that input format is like this for each line (Burst Time, Delay Time, Priority, Level)***

  - Burst Time : the duration for which a process gets control of the CPU.
  - Delay Time : the duration for which a process takes to come after the previous process for example: first process delay time is 1 so it will arrive at time 1 and second process delay time is 3 so it will arrive at time 4.
  - Priority : priority of a process (for Priority and Lottery Scheduler but it can't be omitted from input)
  - Level : level of a process (for Multi Level Scheduler but it can't be omitted from input)

***Note that minimum duration of context switch in this simulation is 0.4***

***Note that minimum duration of quantum for round robbin scheduler in this simulation is 0.2 because time unit is 0.1 and quantum can't be less than time unit***

<br />

<hr />

## Results



<hr />

## Authors

  - Alireza Kavian ( [@alirezakay](https://github.com/alirezakay) )
  - Soheil Changizi ( [@cocolico14](https://github.com/cocolico14) )
  
## Org.

  - ***[Brilacasck](https://brilacasck.ir)*** 
  
## Team
  
  - ***ASCK TEAM***

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details

