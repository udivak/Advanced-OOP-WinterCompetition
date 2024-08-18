package utilities;
import java.util.concurrent.*;

import java.util.concurrent.Executors;
/**
 * @author Itzhak Eretz Kdosha
 * Main class(run demo)
 */
public class Program {
	public static void main(String[] args) {
		CompetitionGUI gui = new CompetitionGUI();
		gui.setVisible(true);
		ExecutorService pool = Executors.newFixedThreadPool(5);

	}
}
