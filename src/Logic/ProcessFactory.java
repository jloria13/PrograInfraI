package Logic;

public class ProcessFactory {

    public Process createProcess(String type) {
        Process x;
        switch (type) {
            case "A":
                x = new ProcessA();
                break;
            case "B":
                x = new ProcessB();
                break;
            default:
                x = new ProcessC();
                break;
        }
        return x;
    }

}
