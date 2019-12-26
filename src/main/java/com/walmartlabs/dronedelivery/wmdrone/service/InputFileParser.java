package com.walmartlabs.dronedelivery.wmdrone.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.walmartlabs.dronedelivery.wmdrone.domain.InputData;
import com.walmartlabs.dronedelivery.wmdrone.exception.BadInputFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InputFileParser {

  Logger logger = LoggerFactory.getLogger(InputFileParser.class);

  public Set<String> readFromInputFile(final String filePath) throws IOException {

    final Set<String> orderSet = new HashSet<String>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        orderSet.add(line);
      }
    }
    return orderSet;
  }

  public List<InputData> convertToInputDataList(final Set<String> orderSet) throws BadInputFileException {

    final List<InputData> inputList = new ArrayList<InputData>();

    // orderSet.forEach((order -> inputList.add(convertedData(order))));

    for (final String order : orderSet) {
      inputList.add(convertedData(order));
    }

    return inputList;

  }

  private InputData convertedData(final String order)
      throws BadInputFileException, DateTimeParseException, NumberFormatException {
    final InputData datum = new InputData();
    final String[] fields = order.split(" ");
    if (fields.length != 3) {
      throw new BadInputFileException("Input file does not have all data in 3 columns");
    }
    datum.setId(fields[0]);
    datum.setLocation(fields[1]);
    datum.setTimeStampStr(fields[2]);
    datum.setTimeStamp(LocalTime.parse(fields[2]));
    String location = fields[1];
    location = location.replaceAll("[N|W|S|E]", " ");
    final String[] steps = location.trim().split(" ");
    if (steps.length != 2) {
      throw new BadInputFileException("Input file has error in location format");
    }
    int timeToLocation = 0;
    for (int i = 0; i < 2; i++) {

      timeToLocation += Integer.parseInt(steps[i]);
    }
    datum.setTimeToLocation(timeToLocation);
    logger.debug(new Gson().toJson(datum));
 //   System.out.print(new Gson().toJson(datum));
    return datum;
  }

}