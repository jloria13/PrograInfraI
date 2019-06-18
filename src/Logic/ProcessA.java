package Logic;

public class ProcessA extends Process{

	public ProcessA(){
		super();
		this.memoryUse = 20;
		this.type = "A";
		this.processTime = 20000;
	}
	
	@Override
	public void run() {
		 try {
	        	counter = 0;
	    		while(counter != 20000){
	    			counter++;
	    			
	    		}
	            Thread.sleep(20000);
	            System.out.println("counter : " + counter);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    } 

}
