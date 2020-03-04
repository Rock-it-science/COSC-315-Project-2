
public class Slave implements Runnable {
	
	private Thread slaveThread;
	private int id;
	private Request heldReq;
	private Buffer bBuffer;
	public Slave(Buffer b, int i) {
		bBuffer = b;
		id = i;
		slaveThread = new Thread(this);
		System.out.println("active " + id);
		slaveThread.start();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				heldReq = bBuffer.Take();
				if(heldReq==null) {
					//System.out.println("Buffer currently empty " + id);
					//slaveThread.sleep(1000);
				}
				else {
					System.out.println("Slave number " +id +" Resolving request id "+heldReq.getID());
					Thread.sleep(heldReq.getTime());
				}
			}
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
