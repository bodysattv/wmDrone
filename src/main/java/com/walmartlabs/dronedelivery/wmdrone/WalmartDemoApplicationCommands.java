package com.walmartlabs.dronedelivery.wmdrone;

import java.io.IOException;
import com.walmartlabs.dronedelivery.wmdrone.exception.BadInputFileException;
import com.walmartlabs.dronedelivery.wmdrone.service.DeliveryLaunchCalcualtion;
import com.walmartlabs.dronedelivery.wmdrone.util.FileReadWriteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * This is entry class. Create a Spring boot application alongwith a Shell
 * command prompt line to run required CLIs.
 */
@SpringBootApplication
@ShellComponent
public class WalmartDemoApplicationCommands {

	@Autowired
	private DeliveryLaunchCalcualtion delCal;

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(WalmartDemoApplicationCommands.class, args);
	}

	/**
	 * Method for the main command from Drone Delivery Challange requirement. Takes
	 * an input file with orders, parse, and outputs optimized delivery/launching
	 * schedule along-with its Net Promoters Score.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws BadInputFileException
	 */
	@ShellMethod("</path/to/file> (Creates the output file with launch schedule and NPS)")
	public String createSchedule(final String filePath) throws BadInputFileException, IOException {

		return delCal.generateOptimizedSequence(filePath);
	}

	/**
	 * Creates mock input file for testing purposes
	 * 
	 * @param n
	 * @return
	 */
	@ShellMethod("<n> (Creates mock n inputs in a file)")
	public String createMockInput(final int n) {
        final FileReadWriteUtil fileUtil = new FileReadWriteUtil();
		return fileUtil.createMockInput(n);
	}

}
