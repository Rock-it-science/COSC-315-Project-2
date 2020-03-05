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
		while(!bufferMade) { //loops until user inputs a correct buffer size
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
		while(!timerMade) { //loops until user inputs a correct timer length
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
		while(!sleepMade) { //loops until user inputs a correct sleep length
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
		Master masterThread = new Master(bufferSize, timerLength, sleepLength); //Creates a master thread with user parameters
		
	}
}
