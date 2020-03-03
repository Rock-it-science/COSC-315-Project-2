
public class Buffer {
	private Request[] buffer;
	private int counter = 0;
	
	public Buffer(int size) {
		buffer = new Request[size];
	}
	
	public synchronized void Add(Request r) {
		if(isFull()) {
			try {
				wait();//master sleeps if buffer is full
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(!isFull()) {
			int i;
			for(i = 0; i < buffer.length; i++) {//looks for empty section in buffer
				if(buffer[i]==null) {
					break;
				}
			}
			buffer[i] = r;//add request to buffer
		}
	}
	
	public synchronized Request Take() {
		Request holder = new Request();//placeholder request(returns a 0 length request if buffer is empty)
		for(int i = 0; i < buffer.length; i++) {
			if(buffer[i]!=null) {
				holder = buffer[i];
				buffer[i] = null;
				break;
			}
		}
		notify();
		return holder;
	}
	
	public int getLength() {
		return buffer.length;
	}
	
	public boolean isFull() {
		if(counter == buffer.length) {
			return true;
		}
		else
			return false;
	}
}
