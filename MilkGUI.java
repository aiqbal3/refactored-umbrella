/**
 * Filename: MilkGUI.java
 * Project: ATeam - GotMilk Project
 * Author: Moubarak Jeje (jeje@wisc.edu)
 * Course: CS400 LEC 001
 * Date: April 30, 2020
 * 
 * Collaborators: Alvin Romano(aromano2@wisc.edu), 
 * Adeel Iqbal (aiqbal3@wisc.edu), 
 * Isacc Weber (iweber2@wisc.edu), 
 * Jonathan Luong (jluong@wisc.edu) 
 */

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creates a JavaFX window as directed by p5 instructions
 */
//public class MilkGUI extends Application {
public class MilkGUI extends Application {

    // lower level class to provide functionality.
    private static MilkStorage storage;
    private static MilkProcess process;

    public static void main(String[] args) {
        storage = new MilkStorage();// allows GUI to access storage class
        process = new MilkProcess(storage);// allows GUI to access process class
        launch(args);
    }

    /**
     * Closes the program
     * 
     * @param primary
     */
    private void closeProgram(Stage primary) {
        primary.close();
    }

    /**
     * Work in progress. Show milk weight sales per month for a given farm.
     * 
     * @param primary
     */
    private void annual(Stage primary) {
        primary.close();
        
        GridPane layout = new GridPane();
        Label yearPrompt = new Label("Enter Year: ");
        TextField typeYear = new TextField();
        Button confirm = new Button("Confirm");
        Button home = new Button("Home");

        layout.addRow(0, yearPrompt, typeYear, confirm);
        layout.addRow(1, home);

        confirm.setOnAction(e -> showAnnual(typeYear.getText(), primary));
        primary.setScene(new Scene(layout));
        primary.show();
    }

    /**
     * Display monthly weight sales for a given year for all farms
     * 
     * @param strYear year of interest "YYYY"
     * @param primary stage
     */
    private void showAnnual(String strYear, Stage primary) {
        primary.close();
        
        //array of months in a year
        String[] strMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        // these two arraylists' indexes are aligned. The monthlyWeights array at any
        // given index
        // is from the farmID of the same given index
        ArrayList<String> farmList = storage.getFarmList();
        ArrayList<String[]> monthlyWeightsList = new ArrayList<String[]>();
        ArrayList<String> yearlyTotalByFarm = new ArrayList<String>();
        
        int intYear = Integer.parseInt(strYear);
        for (int i = 0; i < farmList.size(); i++) {

            // fill the monthly data String[] arrays
            int[] intArray = storage.getMonthlyMilk(intYear, farmList.get(i));
            monthlyWeightsList.add(Arrays.stream(intArray).mapToObj(String::valueOf).toArray(String[]::new));

            // fill the list of yearly totals for each farm
            yearlyTotalByFarm.add(Integer.toString(storage.getTotalMilkInYearOfFarm(intYear, farmList.get(i))));
        }
        
        //Initialize ui elements
        GridPane layout = new GridPane();
        Label farmCol = new Label("| Farm | ");
        Label totalCol =new Label("Total |");
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e->home(primary));
        
        //add table headers
        /*
         * Farm | Jan | Feb | Apr | May | Jul | Jun | Aug | Sep | Oct | Nov | Dec | Total
         * a       -     -     -     -     -     -     -     -     -     -     -     -
         * b       -     -     -     -     -     -     -     -     -     -     -     -
         * c       -     -     -     -     -     -     -     -     -     -     -     -
         * d       -     -     -     -     -     -     -     -     -     -     -     -
         * 
         */
        layout.addColumn(0, farmCol);
        layout.addColumn(13, totalCol);
        for(int i=0; i<strMonths.length;i++) {
            layout.addColumn(i+1, new Label(strMonths[i]+" | "));
            
        }
        
        //insert applicable info into table
        for (int i = 0; i < farmList.size(); i++) {
            //lblMonths[i].setText(strMonths[i]);// match month data with corresponding label
                                                                                
            layout.addRow(i+1, new Label(farmList.get(i)));//add name of farm to first column
            
            //iterate through months and add to table
            for(int k = 0;k<monthlyWeightsList.get(i).length;k++) {
                layout.add(new Label(monthlyWeightsList.get(i)[k]), i+1, k+1);
            }
            
            //add yearly milk weight sold at last column
            layout.add(new Label(yearlyTotalByFarm.get(i)), i+1, 13);
        }
        
        layout.setPadding(new Insets(20));
        primary.setScene(new Scene(layout));
        primary.show();
    }

    /**
     * Work in progress. Show milk weight sales per month for a given farm
     * 
     * @param primary
     */
    private void farm(Stage primary) {
        primary.close();
        // Initialize ui elements
        GridPane layout = new GridPane();
        Label farmPrompt = new Label("Enter Farm ID");
        Label yearPrompt = new Label("Enter year");
        TextField typeFarm = new TextField();
        TextField typeYear = new TextField();
        Button confirm = new Button("confirm");
        Button home = new Button("Home");

        // add elements to page
        layout.addRow(0, farmPrompt, typeFarm);
        layout.addRow(1, yearPrompt, typeYear, confirm);
        layout.addRow(1, home);

        // confirm button creates new page
        confirm.setOnAction(e -> getFarmData(typeFarm.getText(), typeYear.getText(), primary));
        home.setOnAction(e -> home(primary));

        primary.setScene(new Scene(layout));
        primary.show();
    }

    /**
     * Displays the farm information for a given farm
     * 
     * @param primary
     */
    private void getFarmData(String farmId, String strYear, Stage primary) {
        primary.close();
        if (!storage.farmExists(farmId)) {// do nothing if farm does not exist
            return;
        }

        // declare output variables
        String[] strMonths = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        String outputTotalMilkWeightInYearOfFarm;
        String outputPercentWeight;
        String[] outputMonthlyWeights = new String[12];

        // Initialize output variables with information from given farm and year
        int intYear = Integer.parseInt(strYear);
        outputTotalMilkWeightInYearOfFarm = Integer.toString(storage.getTotalMilkInYearOfFarm(intYear, farmId));
        outputPercentWeight = Double.toString(storage.getPercentMilkInYearOfFarm(intYear, farmId));
        for (int i = 0; i < outputMonthlyWeights.length; i++) {
            outputMonthlyWeights[i] = Integer.toString(storage.getFarmMilkInMonth(intYear, i, farmId));
        }

        // Initialize ui elements
        GridPane layout = new GridPane();
        Label showPercent = new Label("Percent weight: " + outputPercentWeight);
        Label showTotal = new Label("Total Milk Weight for " + farmId + ": " + outputTotalMilkWeightInYearOfFarm);

        // add ui elements to page
        layout.addRow(0, showTotal);
        layout.addRow(1, showPercent);

        Label[] lblMonths = new Label[12];
        for (int i = 0; i < lblMonths.length; i++) {
            lblMonths[i].setText(strMonths[i] + ": " + outputMonthlyWeights[i]);// match month data with corresponding
                                                                                // label
            layout.addRow(i + 2, lblMonths[i]);
        }

        primary.setScene(new Scene(layout));
        primary.show();
    }

    /**
     * Edit a sale for a user specified farm.
     * 
     * @param primary
     */
    private void edit(Stage primary) {
        primary.close();
        // Create labels
        Label farmPrompt = new Label("Enter Farm ID");
        Label datePrompt = new Label("Enter date of sale");
        Label weightPrompt = new Label("Enter new milk weight");

        // Create text input fields
        TextField typeFarm = new TextField("No special characters");
        TextField typeDate = new TextField("mm-dd-yyyy");
        TextField typeWeight = new TextField("lbs");
        Button confirmButton = new Button("Confirm");
        Button homeButton = new Button("Home");

        // add ui elements to page
        GridPane layout = new GridPane();
        layout.addRow(0, farmPrompt, typeFarm);
        layout.addRow(1, datePrompt, typeDate);
        layout.addRow(2, weightPrompt, typeWeight, confirmButton);
        layout.addRow(3, homeButton);

        confirmButton.setOnAction(e -> doEdit(typeDate.getText(), typeFarm.getText(), typeWeight.getText(), primary));
        confirmButton.setOnAction(e -> home(primary));

        primary.setScene(new Scene(layout));
        primary.show();
    }

    /**
     * Edit a sale on a given date at a given farm. Use MilkStorage Backend
     * 
     * @param date "mm-dd-yyyy" date of interest
     * @param farm farm of interest
     * @param weight new weight 
     * @param primary stage
     */
    private void doEdit(String date, String farm, String weight, Stage primary) {
        primary.close();
        String[] splitDate = date.split("-"); // split date into separate parts [year, month, day]
        storage.editSale(Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[0]),
                farm, Integer.parseInt(weight));
        home(primary);
    }

    /**
     * Home page for got milk software
     * @param primary stage
     */
    private void home(Stage primary) {
        primary.close();
        VBox centerPanel = new VBox();
        Button farmButton = new Button("Farm Report");
        Button annualButton = new Button("Annual Report");
        Button rangeButton = new Button("Data Range Report");
        Button editButton = new Button("Edit Sale");
        Button closeButton = new Button("Close");

        centerPanel.getChildren().addAll(farmButton, annualButton, rangeButton, editButton, closeButton);

        Scene home = new Scene(centerPanel);// HOME PAGE SCENE
        closeButton.setOnAction(e -> closeProgram(primary));
        farmButton.setOnAction(e -> farm(primary));
        annualButton.setOnAction(e -> annual(primary));
        editButton.setOnAction(e -> edit(primary));

        primary.setScene(home);
        primary.show(); // displays the screen
    }

    /**
     * The main entry point for all JavaFX applications. The start method is called
     * after the init method has returned, and after the system is ready for the
     * application to begin running.
     * 
     * @param primaryStage
     */
    public void start(Stage primaryStage){
        home(primaryStage);
    }

}