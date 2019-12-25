package com.walmartlabs.dronedelivery.wmdrone.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class InputFileParser {

    

    public Set<String> readFromInputFile(String filePath)
  throws IOException {

    Set<String> orderSet
     = new HashSet<String>();

    try (BufferedReader br
      = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            orderSet.add(line);
        }
    }
  return orderSet;
}

}