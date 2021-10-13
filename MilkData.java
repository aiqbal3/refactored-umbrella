import java.util.ArrayList;

/**
 * Filename: MilkStorage.java
 * Project: ATeam - GotMilk Project
 * Author: Isaac Weber (iweber2@wisc.edu)
 * Course: CS400 LEC 001
 * Date: April 21, 2020
 * 
 * Collaborators: 
 * Alvin Romano(aromano2@wisc.edu)
 * Adeel Iqbal (aiqbal3@wisc.edu)
 * Moubarak Jeje (jeje@wisc.edu)
 * Jonathan Luong (jluong@wisc.edu)
 */

/**
 * This class is an object that holds all of the milk data for a given FarmID
 */
 public class MilkData{

    int allTimeTotal;
    ArrayList<Integer> yearList; // used to find the ArrayList index of a year, index of year matches for all ArrayLists
    ArrayList<int[][]> milkCalendar;
    ArrayList<Integer> yearTotal;
    ArrayList<int[]> monthTotals;
    
    /**
     * No-arg constructor
     */
    public MilkData(){
        allTimeTotal = 0;
        yearList = new ArrayList<>();
        milkCalendar = new ArrayList<>();
        yearTotal = new ArrayList<>();
        monthTotals = new ArrayList<>();
    }

    /**
     * 
     * @param year  2020 should be formatted as 2020
     * @param month January should be formatted as 1
     * @param day   First day of month should be formatted as 1
     * @param weight
     */
    public void setSale(int year, int month, int day, int weight){

        // convert parameters to array arithmetic
        month--;
        day--;

        // case of new year, add a new index to all arrayLists
        if (!yearList.contains(year)){
            yearList.add((Integer) year);
            milkCalendar.add(new int[12][31]);
            yearTotal.add(0);
            monthTotals.add(new int[12]);
        }

        int listIndex = yearList.indexOf(year);
        int[][] calendar = milkCalendar.get(listIndex);

        // calculate the difference in existing sale weight vs new sale weight
        allTimeTotal -= calendar[month][day];
        allTimeTotal += weight;
        yearTotal.set(listIndex, (yearTotal.get(listIndex) - calendar[month][day] + weight));
        monthTotals.get(listIndex)[month] -= calendar[month][day];
        monthTotals.get(listIndex)[month] += weight;


        // set the data point to the new weight value
        calendar[month][day] = weight;
    }

    /**
     * 
     * @param index
     * @return year at a given ArrayList index
     */
    public int getYearAtIndex(int index){
        return yearList.get(index);
    }

    /**
     * 
     * @param year 2020 = 2020
     * @param month January = 1
     * @param day First day of month = 1
     * @return weight at a given date
     */
    public int getWeightAtDate(int year, int month, int day){
        // convert parameters to array arithmetic
        month--;
        day--;
        return milkCalendar.get(yearList.indexOf(year))[month][day];
    }

    /**
     * 
     * @return number of years in data
     */
    public int numberOfYears(){
        return yearList.size();
    }

    /**
     * 
     * @return total weight of all stored data points
     */
    public int getAllTimeTotal(){
        return allTimeTotal;
    }

    /**
     * 
     * @param year
     * @return total weight in a given year
     */
    public int getYearTotal(int year){
        return yearTotal.get(yearList.indexOf(year));
    }

    /**
     * Index 0 = January
     * Index 11 = December
     * @param year
     * @return an int array of the monthly totals of a year for this farmID
     */
    public int[] getMonthTotals(int year){
        return monthTotals.get(yearList.indexOf(year));
    }

    
 }