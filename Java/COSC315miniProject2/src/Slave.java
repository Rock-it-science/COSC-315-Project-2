
public class Slave implements Runnable {
	
	private Thread slaveThread;
	private int id;
	private Request heldReq;
	private Buffer bBuffer;
	public Slave(Buffer b, int i) {
		bBuffer = b;
		id = i;
		slaveThread = new Thread(this);
		System.out.println("Starting slave no." + id);
		slaveThread.start();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				heldReq = bBuffer.Take(); //Takes a Request from Buffer
				if(heldReq!=null) {
					System.out.println("Slave number " + id + " Resolving request id " + heldReq.getID());
					Thread.sleep(heldReq.getTime());
					System.out.println("Slave number " + id + " Resolved request id " + heldReq.getID());
				}
			}
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
