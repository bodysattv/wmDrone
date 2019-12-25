package com.walmartlabs.dronedelivery.wmdrone;

import java.io.IOException;

import com.walmartlabs.dronedelivery.wmdrone.service.InputFileParser;

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
	
