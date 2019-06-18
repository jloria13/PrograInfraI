package Logic;

public class Recurso {
	private boolean used;

	public boolean isUsed() {
		return used;
	}

	public void use() {
		this.used = true;
	}
	
	public void stopUsing() {
		this.used = false;
	}
}
