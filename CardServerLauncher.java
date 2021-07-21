package card.validator.server;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class CardServerLauncher {	
	public static void main(String[] args) throws Exception {
		CardServer cardServer = new CardServer();		
		Thread thread = new Thread(cardServer);
		thread.start();
		
		ValidatorReport report = new ValidatorReport();

		Scanner scanner = new Scanner(System.in);
		
		String line;
		while ((line = scanner.nextLine()) != null) {
			if (line.equals("QUIT")) {
				cardServer.quit();
				break;
			} else if (line.equals("REPORT")) {
				if (report.reportValidator()) {
					System.out.println("REPORT FINISH");
				}
			} else { // Date
				String option = null;
				if (line.length() > 9) {
					option = line.split(" ")[1];
				}
				report.printReport(line.substring(0, 8), option);
			}
		}
	}
}