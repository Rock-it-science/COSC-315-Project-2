
public class Master implements Runnable {
	Thread masterThread;
	private Buffer bBuffer;
	private Slave[] slaveList;
	private Integer bufferSize;
	private Integer maxRequest;
	private Integer sleep;
	private Integer rid = 0;
	
	public Master() {//no-arg constructor
		bufferSize = 8;
		maxRequest = 5000;
		sleep = 3000;
		bBuffer = new Buffer(bufferSize);
		System.out.println("Initializing with default buffer of size 8, a 5 second requests, and a 3 second sleep");
		masterThread = new Thread(this);
		Run(8);
	}
	
	public Master(int n, int t, int s) {
		bBuffer = new Buffer(n);
		bufferSize = n;
		maxRequest = t*1000;
		sleep = s*1000;
		System.out.println("Initializing with buffer of size " +n +", a " +t +" second timer, and a " + t +" second sleep");
		masterThread = new Thread(this);
		Run(n);
	}
	
	private void Run(int n) {
		slaveList = new Slave[n];//Create slaves
		for(int i = 0; i < n; i++) {
			int j = i+1;
			slaveList[i] = new Slave(bBuffer, j);
			System.out.println("Slave "+j +" created");
		}
		while(true) {
			int currSleep = (int)(Math.random()*sleep);
			int reqDurr = (int)(Math.random()*(maxRequest-1000)+1000);
			Request newReq = new Request(rid, reqDurr);
			System.out.println("Generating request id " +rid +" with duration of " +reqDurr);
			rid++;
			bBuffer.Add(newReq);
			try {
				Thread.sleep(currSleep);//sleep random amount of seconds
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
