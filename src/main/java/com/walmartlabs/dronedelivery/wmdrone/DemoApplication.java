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

@SpringBootApplication
@ShellComponent
public class DemoApplication {

	@Autowired
	private InputFileParser parserService;

	@Autowired
	private DeliveryLaunchCalcualtion delCal;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@ShellMethod("Add two integers together.")
	public int add(int a, int b) {
		return a + b;
	}

	@ShellMethod("Parse the file (cmd: parse </path/to/file>")
	public void parse(String filePath) {
		try {
			parserService.readFromInputFile(filePath).forEach(System.out::println);
			List<OrderData> input = parserService.convertToOrderDataList(parserService.readFromInputFile(filePath));
			input.forEach(System.out::println);

			delCal.tagInput(input);

		} catch (BadInputFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
