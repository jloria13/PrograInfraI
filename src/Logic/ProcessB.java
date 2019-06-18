package Logic;

public class ProcessB extends Process{

	
	public ProcessB(){
		super();
		this.memoryUse = 2;
		this.type = "B";
		this.processTime = 50;
	}
	
	@Override
	public void run() {
		 try {
	        	counter = 0;
	    		while(counter != 50){
	    			counter++;
	    			
	    		}
	            Thread.sleep(10000);
	            System.out.println("counter : " + counter);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	

}
