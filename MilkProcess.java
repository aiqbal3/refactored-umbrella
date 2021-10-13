
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Filename: MilkAnalyzer.java
 * GotMilk Project
 * Adeel Iqbal
  */
public class MilkProcess{
  MilkStorage storage;
  // File file;
  // boolean fileCreated = false;
  // String fileName;

    MilkProcess(MilkStorage storage){
        this.storage = storage;
    }

    static int[] years;
    static int[] months;
    static int[] days;
    static String[] farmID;
    static int[] weight;

    /**
     * Saves all milk weight sales from a given farm ID to a specified file
     * 
     * @param filename path of file to create
     * @param farmId   name of farm of interest
     * @return true if file created successfully, false if otherwise
     */
    public boolean saveFarmData(String filename, String farmId) {
      if (!storage.farmExists(farmId)) {
        // farm does not exist
        return false;
      }

      String[][] farmData = storage.getFarmData(farmId);

      // parse information
      String outFormat = "date,farm_id,weight";
      for (int i = 0; i < farmData[0].length; i++) {

        if (Integer.parseInt(farmData[4][i]) != 0) {
          outFormat = outFormat + "\n" + farmData[0][i] + "-" + farmData[1][i] + "-" + farmData[2][i] + ","
              + farmData[3][i] + "," + farmData[4][i];
        }

      }

      try {
        FileWriter writer = new FileWriter(filename);
        writer.write(outFormat);
        writer.close();
      } catch (Exception e) {
        return false;
      }

      return true;
    }

    public static boolean parseCSV(File CSV) throws IOException {
      try {
        Scanner dataScan = new Scanner(CSV);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(CSV));
        String input;
        int countOfLines = 0;
        while ((input = bufferedReader.readLine()) != null) {
          countOfLines++;
        }
        years = new int[countOfLines]; // creates an array of size Lines for years
        months = new int[countOfLines]; // creates an array of size Lines for months
        days = new int[countOfLines];// creates an array of size Lines for day
        farmID = new String[countOfLines];// creates an array of size Lines for farmId
     weight = new int[countOfLines]; //creates an array of size Lines for weight
    for(int currLine = 0; currLine<countOfLines;currLine++){ //for loop to iterate through all lines and hold counter of current line
      String nextLine = dataScan.nextLine();  //Set string for nextline
      String[] splitAll = nextLine.split(","); //split file into 3 parts [year-month-day, farm id, milk ammount]
      String[] splitDate = splitAll[0].split("-"); //split date into separate parts [year, month, day]
      String[] fullArray; //initialize new Array
      fullArray = new String[] {splitDate[0], splitDate[1], splitDate[2], splitAll[1], splitAll[2]};//create new fullArray with every data in there [year month day, Farm Id, milk ammount]
      if(fullArray.length!=5){
        return false;
      }
      years[currLine] = Integer.parseInt(fullArray[0]);
      months[currLine] = Integer.parseInt(fullArray[1]);
      days[currLine] = Integer.parseInt(fullArray[2]);
      farmID[currLine] = fullArray[3];
      weight[currLine]=Integer.parseInt(fullArray[4]);
    }
    bufferedReader.close();
    dataScan.close(); //close Scanner
    return true;

  }catch (FileNotFoundException e){
       e.printStackTrace();
       return false;
       }
       catch(Exception e){
           return false;
       }


}

}
