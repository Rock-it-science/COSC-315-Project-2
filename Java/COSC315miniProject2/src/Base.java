import java.util.Scanner;
public class Base {

	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		Integer bufferSize = 0;
		Integer timerLength = 0;
		Integer sleepLength = 0;
		boolean bufferMade = false;
		boolean timerMade = false;
		boolean sleepMade = false;
		while(!bufferMade) {
			System.out.println("Input buffer size: ");
			String buff = input.nextLine();
			try {
				bufferSize = Integer.parseInt(buff);
				bufferMade = true;
			}
			catch (NumberFormatException nfe) {
				System.out.println("Please input integers only.");
			}
		}
		while(!timerMade) {
			System.out.println("Input max request time: ");
			String time = input.nextLine();
			try {
				timerLength = Integer.parseInt(time);
				timerMade = true;
			}
			catch (NumberFormatException nfe) {
				System.out.println("Please input integers only.");
			}
		}
		while(!sleepMade) {
			System.out.println("Input sleep length: ");
			String sleep = input.nextLine();
			try {
				sleepLength = Integer.parseInt(sleep);
				sleepMade = true;
			}
			catch (NumberFormatException nfe) {
				System.out.println("Please input integers only.");
			}
		}
		input.close();
		Master masterThread = new Master(bufferSize, timerLength, sleepLength);
		
	}

}
