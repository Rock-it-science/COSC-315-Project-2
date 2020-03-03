
public class Request {
	private Integer id;
	private Integer time;
	public Request() {//no-arg const
		id = 0;
		time = 5000;
	}
	
	public Request(Integer i, Integer t) {
		id = i;
		time = t;
	}

	public Integer getID() {
		return id;
	}
	
	public Integer getTime() {
		return time;
	}
}
