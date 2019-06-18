package Logic;

import java.util.ArrayList;
import java.util.Timer;

public class Dispatcher {

    private int memory;
    private ArrayList<Process> running;
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> blockedQueue;
    private Recurso recurso1;
    private Recurso recurso2;
    int semCount;
    ArrayList<Process> semQueue;
    static Timer timer;

    //Se crean las listas de espera del dispatcher de 5 estados
    public Dispatcher() {
        this.memory = 0;
        this.running = new ArrayList<Process>(3);
        this.readyQueue = new ArrayList<Process>();
        //this.readySuspendedQueue = new ArrayList<Process>(); 
        this.blockedQueue = new ArrayList<Process>();
        //this.blockedSuspendedQueue = new ArrayList<Process>(); 
        this.recurso1 = new Recurso();
        this.recurso2 = new Recurso();

    }

    public void runProcess() {
        Process process = running.get(0);
        process.run();
        freeProcess();
    }

    //Creates a new process, if the memory queque is full the process goes to the blockedQueue, else if the running queque is full the process
    //goes to readyQueue and if there is not a problem with memory and running queque the process goes to the running queque
    public Process createNewProcess(String pName) {
        Process process = new ProcessFactory().createProcess(pName);
        int calcultion = this.memoryCalcultion(process.getMemoryUse());

        //Si el pName es igual a B hay que hacer otro if para ver los recursos y usar el semaforo
        if (calcultion <= 40) {//reparar

            if (running.size() <= 2) {

                /*
				if(process.getType().equals("B")) {
					semWait(process);
					semSignal(process);		
				}*/
                running.add(process);
                addMemory(process.getMemoryUse());
                process.setState("Running");
                new Thread(() -> {
                    runProcess();
                }).start();
            } else {
                readyQueue.add(process);
                process.setState("Ready");
            }

        } else {

            blockedQueue.add(process);
            process.setState("Blocked");
        }
        return process;
    }

    public void freeProcess() {

        //Hacer metodo LRU
        int index = 0;

        freeMemory(running.get(index).getMemoryUse());
        running.get(index).setState("Finished");
        running.remove(index);

    }

    public void seeReady() {
        //

        if (readyQueue.isEmpty() == false) {
            int cont = 0;

            while (readyQueue.size() != cont & running.size() <= 2) {

                Process process = readyQueue.get(cont);
                int calcultion = this.memoryCalcultion(process.getMemoryUse());

                if (calcultion <= 40) {
                    running.add(process);
                    addMemory(process.getMemoryUse());
                    process.setState("Running");
                    readyQueue.remove(cont);
                    new Thread(() -> {
                        runProcess();
                    }).start();
                    break;
                } else {
                    blockedQueue.add(process);
                    process.setState("Blocked");
                    readyQueue.remove(cont);
                }

                cont++;

            }

        }
    }

    public void seeBlocked() {

        if (blockedQueue.isEmpty() == false) {
            int cont = 0;

            while (blockedQueue.size() != cont & running.size() <= 2) {

                Process process = blockedQueue.get(cont);
                int calcultion = this.memoryCalcultion(process.getMemoryUse());

                if (calcultion <= 40) {
                    running.add(process);
                    addMemory(process.getMemoryUse());
                    process.setState("Running");
                    blockedQueue.remove(cont);
                    new Thread(() -> {
                        runProcess();
                    }).start();
                    break;
                } else {
                    cont++;
                }

            }
        }
    }

    //Se hace el calculo si la cola de la memoria esta llena o no
    private int memoryCalcultion(int processSize) {
        int temp;
        int memorySize = memory;
        temp = memorySize + processSize;
        return temp;

    }

    private void addMemory(int processSize) {

        memory = memory + processSize;

    }

    private void freeMemory(int processSize) {

        memory = memory - processSize;

    }

    //Metodo auxilar para saber cuanto espacio ocupado tiene la memoria 
    /*private int addPriority(){
		int temp = 0;
		switch(running.size()) {
		  case 0:
		    
			  temp = 1;
		    
		  case 1:
			  
			  temp = 2;
			  
		  case 2:
			  
			  temp = 3;
		}
		return temp;
	}*/
    //semaforo
    void semWait(Process p) {
        semCount--;
        if (semCount < 0) {
            /*place this process in s.queue */
            semQueue.add(p);
            /*block this process */
        }
        running.add(p);
        addMemory(p.memoryUse);
        p.setState("Running");
        new Thread(() -> {
            runProcess();
        }).start();
        //se ejecuta el proceso/

    }

    void semSignal(Process p) {
        semCount++;
        if (semCount <= 0) {
            int index = semQueue.size() - 1;
            readyQueue.add(semQueue.get(index));
            semQueue.remove(index);
            /*remove a process P from semQueue */;
            /*place process P on ready list */;
        }
    }

    public static void main(String[] args) {

    }

}
