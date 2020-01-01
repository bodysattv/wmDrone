package com.walmartlabs.dronedelivery.wmdrone.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.walmartlabs.dronedelivery.wmdrone.domain.OrderData;
import com.walmartlabs.dronedelivery.wmdrone.exception.BadInputFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class deals with the input file for parsing and validity checks
 */
@Component
public class FileReadWriteUtil {

  Logger logger = LoggerFactory.getLogger(FileReadWriteUtil.class);

   /**
   * read the input file into a list or lines. Exception if input file is empty
   * 
   * @param filePath
   * @return
   * @throws IOException
   * @throws BadInputFileException
   */
  public List<String> readFromInputFile(final String filePath) throws IOException, BadInputFileException {

    final List<String> inputLines = new ArrayList<String>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        inputLines.add(line);
      }
    }
    if (inputLines.size() == 0) {
      throw new BadInputFileException("Input file does not have data");
    }

    return inputLines;
  }

  

  
  /**
   * 
   * @param n
   * @return
   */
public String createMockInput(int n) {
  String filePath = "src/main/resources/";
  StringBuilder filePathBldr = new StringBuilder(filePath);
  
	return null;
}



    /**
     * generate the output file
     * 
     * @param orderList
     * @param fileName
     * @throws IOException
     */
    public void generateOutput(final List<OrderData> orderList, final String fileName) throws IOException {
      Integer numberOfPromoters = 0;
      Integer numberOfDertactors = 0;
      final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
      for (final OrderData input : orderList) {
          final String outputLine = new StringBuilder(input.getId()).append(" ")
                  .append(input.getLaunchTime().format(DateTimeFormatter.ISO_LOCAL_TIME)).toString();

          if (input.getTag().equals(OrderData.Tag.PROMOTER)) {
              numberOfPromoters += 1;
          } else if (input.getTag().equals(OrderData.Tag.DETRACTOR)) {
              numberOfDertactors += 1;
          }

          writer.write(outputLine + "\n");

      }
      // write NPS calculation
      final Integer npsValue = (numberOfPromoters - numberOfDertactors) * 100 / orderList.size();
      final String npsLine = new StringBuilder("NPS ").append(npsValue).toString();
      writer.write(npsLine);

      writer.close();

  }

}