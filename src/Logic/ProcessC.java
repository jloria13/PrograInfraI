package Logic;

public class ProcessC extends Process{

public ProcessC(){
		super();
		this.memoryUse = 1;
		this.type = "C";
		this.processTime = 10000000;
	}
	
	@Override
	public void  run() {
		counter = 0;
		while(counter != 50){
			counter++;
			System.out.println("counter : " + counter);
			counter--;
		}
		//return 1;
	}
}
