package Logic;

import java.util.ArrayList;
import java.util.Timer;

public class Dispatcher {

    private int memory;
    private ArrayList<Process> running;
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> blockedQueue;
    //private Recurso recurso1;
    //private Recurso recurso2;
    int semCount = 2;
    ArrayList<Process> semQueue;
    static Timer timer;

    //Se crean las listas de espera del dispatcher de 5 estados
    public Dispatcher() {
        this.memory = 0;
        this.running = new ArrayList<Process>(3);
        this.readyQueue = new ArrayList<Process>();
        this.blockedQueue = new ArrayList<Process>();
        this.semQueue = new ArrayList<Process>();
        //this.recurso1 = new Recurso();
        //this.recurso2 = new Recurso();

    }

    public void runProcess(Process process) {
        process.run();
        freeProcess(process);
    }

    public void runProcessSem(Process process) {
        process.run();
        semSignal();
        freeProcess(process);
    }

    //Creates a new process, if the memory queque is full the process goes to the blockedQueue, else if the running queque is full the process
    //goes to readyQueue and if there is not a problem with memory and running queque the process goes to the running queque
    public Process createNewProcess(String pName) {
        Process process = new ProcessFactory().createProcess(pName);
        int calcultion = this.memoryCalcultion(process.getMemoryUse());

        //Si el pName es igual a B hay que hacer otro if para ver los recursos y usar el semaforo
        if (calcultion <= 40) {//reparar

            if (running.size() <= 2) {
                if (process.getType().equals("B")) {
                    semWait(process);
                } else {
                    running.add(process);
                    addMemory(process.getMemoryUse());
                    process.setState("Running");
                    new Thread(() -> {
                        runProcess(process);
                    }).start();
                }
            } else {
                if (process.getType().equals("B")) {
                    System.out.println("Entre");
                    semWait(process);
                } else {
                    readyQueue.add(process);
                    process.setState("Ready");
                }
            }

        } else {
            blockedQueue.add(process);
            process.setState("Blocked");
        }
        return process;
    }

    public void freeProcess(Process process) {
        for (int i = 0; i < running.size(); i++) {
            Process procs = running.get(i);
            if (procs.getID() == process.getID()) {
                running.remove(i);
            }
        }
        //Hacer metodo LRU

        freeMemory(process.getMemoryUse());
        process.setState("Finished");
    }

    public void seeReady() {
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
                    if (process.getType().equals("B")) {
                        new Thread(() -> {
                            runProcessSem(process);
                        }).start();
                    } else {
                        new Thread(() -> {
                            runProcess(process);
                        }).start();
                    }
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
                    if (process.getType().equals("B")){
                        new Thread(() -> {
                        runProcessSem(process);
                    }).start();
                    }else{
                        new Thread(() -> {
                            runProcess(process);
                        }).start();
                    }
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

    void semWait(Process process) {
        semCount--;
        if (semCount < 0) {
            /*place this process in s.queue */
            process.setState("Blocked");
            semQueue.add(process);
            /*block this process */
        }else{
            running.add(process);
            addMemory(process.memoryUse);
            process.setState("Running");
            new Thread(() -> {
                runProcessSem(process);
            }).start();
        }
        //se ejecuta el proceso/

    }

    void semSignal() {
        semCount++;
        if (semCount <= 0) {
            Process process = semQueue.get(0);
            semQueue.remove(0);
            process.setState("Ready");
            readyQueue.add(process);
            /*remove a process P from semQueue */;
            /*place process P on ready list */;
        }
    }

}
