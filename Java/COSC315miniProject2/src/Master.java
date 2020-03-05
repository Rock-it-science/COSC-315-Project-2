
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
		masterThread.start();
	}
	
	//initialize master thread with parameters
	public Master(int bufferSize, int timerLength, int sleepLength) {
		bBuffer = new Buffer(bufferSize);
		this.bufferSize = bufferSize;
		maxRequest = timerLength*1000;
		sleep = sleepLength*1000;
		System.out.println("Initializing with buffer of size " + bufferSize +", a " + timerLength +" second timer, and a " + sleepLength +" second sleep");
		masterThread = new Thread(this);
		masterThread.start();
	}

	@Override
	public void run() {
		slaveList = new Slave[bufferSize]; //Create slaves
		for(int i = 0; i < bufferSize; i++) {
			int j = i+1;
			slaveList[i] = new Slave(bBuffer, j);
			System.out.println("Slave "+ j +" created");
		}
		while(true) {
			int currSleep = (int)(Math.random()*sleep); //random sleep duration within max sleep duration
			int reqDurr = (int)(Math.random()*(maxRequest-1000)+1000); //random request duration within 1 to max request duration
			Request newReq = new Request(rid, reqDurr); //make a new request
			System.out.println("Generating request id " + rid +" with duration of " +reqDurr);
			rid++;
			bBuffer.Add(newReq); //add new request to buffer
			try {
				Thread.sleep(currSleep); //sleep random amount of seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
