package com.walmartlabs.dronedelivery.wmdrone;

import java.io.IOException;
import java.util.List;
import com.walmartlabs.dronedelivery.wmdrone.exception.BadInputFileException;
import com.walmartlabs.dronedelivery.wmdrone.service.DeliveryLaunchCalcualtion;
import com.walmartlabs.dronedelivery.wmdrone.service.InputFileParser;
import com.walmartlabs.dronedelivery.wmdrone.domain.OrderData;
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
	private InputFileParser parserService;

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
	 */
	@ShellMethod("</path/to/file> (Creates the output file with launch schedule and NPS)")
	public String createSchedule(final String filePath) {
		String outputFilePath = "";
		try {
			outputFilePath = parserService.getOutputFileName(filePath);
			parserService.readFromInputFile(filePath).forEach(System.out::println);
			final List<OrderData> inputList = parserService
					.convertToOrderDataList(parserService.readFromInputFile(filePath));

			inputList.forEach(System.out::println);

			delCal.generateOptimizedSequence(inputList, outputFilePath);
			// delCal.tagInput(inputList);

		} catch (final BadInputFileException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return outputFilePath;
	}

	/**
	 * Creates mock input file for testing purposes
	 * 
	 * @param n
	 * @return
	 */
	@ShellMethod("<n> (Creates mock n inputs in a file)")
	public String createMockInput(final int n) {

		return parserService.createMockInput(n);
	}

}
