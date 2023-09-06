package com.spring.challenge.controllers;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class DataController {



  @GetMapping("/run")
  public  static void runPythonScript() throws InterruptedException, IOException {
    String pythonScriptPath = "src/main/resources/Customer_Analysis.py";

    // Execute the Python script
    ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
    Process process = processBuilder.start();

    // Optionally, you can wait for the process to complete
    int exitCode = process.waitFor();
    System.out.println("Python script executed with exit code: " + exitCode);
    process.destroy();
  }


}
