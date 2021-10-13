
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Filename: MilkStorage.java
 * GotMilk Project
 * Adeel Iqbal (aiqbal3@wisc.edu)
 */

 /**
  * README: Parameter order will always match the CSV File:
  *  - Year, Month, Day, FarmID, Weight
  * Any missing parameter will simply be removed from the ordering.
  */
class MilkStorage {

    // This is a hashmap with key of farmID and value of MilkData
    HashMap<String, MilkData> milkMap;

    ArrayList<String> farmList;
    

    /**
     * No-arg Constructor
     */
    public MilkStorage() {
        milkMap = new HashMap<String, MilkData>(11);
    }


    /**
     * Stores data from each of the parameters.
     * All PARAMETERS MUST BE SAME SIZE. Store all the data together for each index.
     * @param year      array of the year for a data point
     * @param month     array of the month for a data point
     * @param day       array of the day for a data point
     * @param farmID    array of the farmID for a data point
     * @param weight    array of the weight for a data point
     */
    public void store(int[] year, int[] month, int[] day, String[] farmID, int[] weight){

        for (int i = 0; i < year.length; i++){

            String currentKey = farmID[i];
            MilkData currentData;

            // if farm hasn't been initalized yet, initialize it
            if (!farmExists(currentKey)){
                milkMap.put(currentKey, new MilkData());
                farmList.add(farmID[i]);
            }

            // set the data point to the values for this index
            currentData = milkMap.get(currentKey);
            currentData.setSale(year[i], month[i], day[i], weight[i]);
        }
    }

    /**
     * Used to retrieve the data when writing to a file
     * @param farmID
     * @return a 2D string array of width 5 and length 372 * N where n is the number of years stored
     * Format is as follows: {years[], months[], days[], farmIDs[], weights[]}
     * Each month will have 31 days regardless of reality, any excess days will have weight 0.
     * This means that February will have days 29 (unless a leap year), 30, and 31 having weight 0;
     */
    public String[][] getFarmData(String farmID){
        
        MilkData dataSet = dataOf(farmID);
        int numberOfYears = dataSet.numberOfYears();
        String[][] toReturn = new String[5][372*numberOfYears];

        // loop through each year
        for (int i = 0; i < numberOfYears; i++){
            // loop through each month
            for(int j = 0; j < 12; j++){
                // loop through each day
                for(int k = 0; k < 31; k++){
                    // set index values of year, month, day, farmID, and 
                    int year = dataSet.getYearAtIndex(i);
                    int month = j + 1;
                    int day = k + 1;
                    toReturn[0][(month)*31 + k] = Integer.toString(year);
                    toReturn[1][(month)*31 + k] = Integer.toString(month);
                    toReturn[2][(month)*31 + k] = Integer.toString(day);
                    toReturn[3][(month)*31 + k] = farmID;
                    toReturn[4][(month)*31 + k] = Integer.toString(dataSet.getWeightAtDate(year, month, day));
                }
            }
        }
        return toReturn;
    }

    /**
     * 
     * @param farmID
     * @return true if farm is already stored in data at least once
     */
    public boolean farmExists(String farmID){
        return milkMap.containsKey(farmID);
    }

    private MilkData dataOf(String farmID){
        return milkMap.get(farmID);
    }

    public ArrayList<String> getFarmList(){
        return farmList;
    }

    /**
     * @return sum of all time milk weight
     */
    public int getAllTimeTotalMilk(){

        int allTimeTotal = 0;
        for (int i = 0; i < farmList.size(); i++){
            allTimeTotal += dataOf(farmList.get(i)).allTimeTotal;
        }
        return allTimeTotal;
    }

    /**
     * 
     * @param year
     * @return int total of all milk sale weights in a year
     */
    public int getTotalMilkInYear(int year){
        
        int yearTotal = 0;
        for (int i = 0; i < farmList.size(); i++){
            yearTotal += dataOf(farmList.get(i)).getYearTotal(year);
        }
        return yearTotal;
    }

    /**
     * 
     * @param year
     * @return int total of all milk sale weights in a year
     */
    public int getTotalMilkInYearOfFarm(int year, String farmID){
        return dataOf(farmID).getYearTotal(year);
    }

    /**
     * Index  0 - January milk weight for given farmID
     * Index 11 - December milk weight for given farmID
     * @param year
     * @param farmID
     * @return int[12] of all milk sale weights for a given farmID in a given year
     */
    public int[] getMonthlyMilk(int year, String farmID){
        return dataOf(farmID).getMonthTotals(year);
    }
    
    /**
     * 
     * @param year
     * @param month
     * @return int total of all milk sale weights in a month for a given year and month
     */
    public int getTotalMilkInMonth(int year, int month){

        int monthTotal = 0;
        for (int i = 0; i < farmList.size(); i++){
            monthTotal += dataOf(farmList.get(i)).getMonthTotals(year)[month];
        }
        return monthTotal;
    }

    /**
     * 
     * @param year
     * @param month
     * @param farmID
     * @return int total of all milk sale weights in a month for a given farmID
     */
    public int getFarmMilkInMonth(int year, int month, String farmID){
        return dataOf(farmID).getMonthTotals(year)[month];
    }

    /**
     * 
     * @param year
     * @param month
     * @param farmID
     * @return percent of milk sold by a farm in a month vs total milk sold in a month
     */
    public double getPercentMilkOfFarmInMonth(int year, int month, String farmID){
        double percent = getFarmMilkInMonth(year, month, farmID) / getTotalMilkInMonth(year, month);
        return percent;
    }

    /**
     * Given a year provide the percent of milk weight that year provides
     * for all farms compared to all milk sold.
     * 
     * @param year year of interest in "YYYY" format
     * @return percentage milk sold in a year out of total milk sold in all time
     */
    public double getPercentMilkInYearOfFarm(int year, String farmID){
        double percent = getTotalMilkInYearOfFarm(year, farmID) / getAllTimeTotalMilk();
        return percent;
    }
    
    /**
     * Given a year provide the percent of milk weight that year provides
     * for all farms compared to all milk sold.
     * 
     * @param year year of interest in "YYYY" format
     * @return percentage milk sold in a year out of total milk sold in all time
     */
    public double getPercentMilkInYear(int year){
        double percent = getTotalMilkInYear(year) / getAllTimeTotalMilk();
        return percent;
    }

    /**
     * Overwrites any existing sale (or lack thereof) for a given date with the given weight
     * @param year      2020 = 2020
     * @param month     January = 1
     * @param day       First day of month = 1
     * @param farmID    
     * @param weight
     */
    public void editSale(int year, int month, int day, String farmID, int weight){
        MilkData dataSet = dataOf(farmID);
        dataSet.setSale(year, month, day, weight);
    }
}
