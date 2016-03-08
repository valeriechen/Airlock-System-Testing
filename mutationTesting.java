import java.io.*;
import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.tools.*;
//import java.nio.file.Files;

class mutationTesting{
	
   public static void main(String[] args){
      System.out.println("Hello there :)");
   
      String filename = "Airlock";
      Airlock spec = new Airlock();
      spec.init();
      Class specClass = Airlock.class;
   
   	//READ IN ORIGINAL IMPLEMENTATION FILE
      ArrayList<String> originalFile = readInFile("DONOTCHANGE");
   
      //READ IN TEST GEN STUFF
      ArrayList<ArrayList<String>> testSeqs = readEventsIn(); 
      ArrayList<ArrayList<String>> oracle = readOracleLog();
   
      runGeneratedCode(new ArrayList<String>(), testSeqs, spec, specClass, oracle);
   }

   
    //=====================================================
    //* Returns if a string is an integer                 *
    //=====================================================  
   public static boolean isInteger(String s) {
      try { 
         Integer.parseInt(s); 
      } 
      catch(NumberFormatException e) { 
         return false; 
      } 
      catch(NullPointerException e) {
         return false;
      }
        // only got here if we didn't return false
      return true;
   }   
    //=====================================================
    //* Method that removes unnecessary characters from   *
    //* the line read in from the .ssl file.              *
    //=====================================================  
   public static String[] cleanLine(String currentLine){
      currentLine = currentLine.replaceAll("\"", "");
      currentLine = currentLine.replaceAll(";", "");
      currentLine = currentLine.replaceAll(",", "");
      currentLine = currentLine.replaceAll("\\[", "");
      currentLine = currentLine.replaceAll("\\]", "");
      currentLine = currentLine.replaceAll("=", "");
      currentLine = currentLine.replaceAll("-", "");            
      return currentLine.split("\\s+");
   }

    //=====================================================
    //* Calls a method based on name in generated code    *
    //=====================================================  
   public static void callMethod(Airlock spec, Class specClass, String methodName, Integer value){
      methodName = "change"+methodName;
      Method[] methods = specClass.getDeclaredMethods();
      for (Method method : methods) {
         try{
            if(method.getName().equals(methodName)){
               method.invoke(spec, value);
            }
         }
         catch(Exception e){}
      }        
   }
    
	//edit this to what needed 
   public static void writeFile(ArrayList<String> file, String filename){
      try{
         System.out.println("writing to file...");
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename+".java"))); //save copy elsewhere
         for(String s : file){
            bw.write(s);
            bw.newLine();
         }
         bw.close();
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
   }
   public static ArrayList<String> readInFile(String filename){
      ArrayList<String> file = new ArrayList<String>();
      try{
         System.out.println("reading from implementation file...");
         BufferedReader readFile = new BufferedReader(new FileReader(filename));
         String currentLine = null;
         while((currentLine = readFile.readLine())!= null){
            file.add(currentLine);
         }
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
      return file;
   }


	//=====================================================
    //* Reads in sequence covering array events for       *
    //* for mutation testing                              *
    //=====================================================  
   public static ArrayList<ArrayList<String>> readEventsIn(){
      ArrayList<ArrayList<String>> allSeqs = new ArrayList<ArrayList<String>>();
      try{
         System.out.println("reading from events file...");
         BufferedReader readFile = new BufferedReader(new FileReader("Airlock_ID_memocode.ev"));
         String currentLine = null;
         ArrayList<String> toAdd = new ArrayList<String>();
         while((currentLine = readFile.readLine())!= null){
            if(currentLine.equals("RESET")){
               allSeqs.add(toAdd);
               toAdd = new ArrayList<String>();
            }
            else{
               toAdd.add(currentLine);
            }
         }
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
      System.out.println(allSeqs.size());
      return allSeqs;
   }

    //=====================================================
    //* Reads log from SCR and stores expected behavior   *
    //=====================================================  
   public static ArrayList<ArrayList<String>> readOracleLog(){
      try{
         System.out.println("reading from log file...");
         BufferedReader readFile = new BufferedReader(new FileReader("Airlock_ID_memocodeFOUR.log"));
         String currentLine = null;
            
         ArrayList<ArrayList<String>> allSeqs = new ArrayList<ArrayList<String>>();
         ArrayList<String> values = new ArrayList<String>();
            
         int count = 0;
         while((currentLine = readFile.readLine())!= null){
            count++;
                //System.out.println(currentLine);
                //System.out.print(count + " ");
         
            String[] array = cleanLine(currentLine);
         
            if(array.length == 5 && currentLine.contains("Initial State")){
            
            }
            else if(array.length == 7 && currentLine.contains("Initial State")){
               if(values.size() > 0){
                  allSeqs.add(values);
               }
               values = new ArrayList<String>();
            
               currentLine = readFile.readLine();
               if(currentLine != null){
                  String[] array1 = cleanLine(currentLine);
                  if(array1.length == 4){
                     values.add(array1[2] + " " + array1[3]);
                  }
                  else{
                     values.add("NO CHANGE");
                  }
               }
            
                    // while((currentLine = readFile.readLine())!= null && !currentLine.contains("State")){
                    //     //System.out.println(currentLine);
                    //     count++;
                    //     //System.out.print(count + " ");
                    //     continue;
                    //     //System.out.println(currentLine);
                    // }
            }
            else if(currentLine.contains("Initial State")){
               if(values.size() > 0){
                  allSeqs.add(values);
               }
               values = new ArrayList<String>();
               while((currentLine = readFile.readLine())!= null && !currentLine.contains("State")){
                        //System.out.println(currentLine);
                  count++;
                        //System.out.print(count + " ");
                  continue;
                        //System.out.println(currentLine);
               }
            }
            if(currentLine != null && currentLine.contains("State")){
               currentLine = readFile.readLine();
                    //System.out.println(currentLine);
               count++;
                    //System.out.print(count + " ");
               if(currentLine != null){
                  String[] array1 = cleanLine(currentLine);
                  if(array1.length == 4){
                     values.add(array1[2] + " " + array1[3]);
                  }
                  else{
                     values.add("NO CHANGE");
                  }
               }
            
            }
                //System.out.print(count + " ");
                //System.out.println(values);
                //System.out.println(allSeqs);
         }
            //System.out.println(allSeqs.size());
            //System.out.println(count);
         return allSeqs;
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

    //=====================================================
    //* Runs the test sequence with the generated code    *
    //* implementation, compared output to predicted      *
    //=====================================================  
   public static int runGeneratedCode(ArrayList<String> startEvents, ArrayList<ArrayList<String>> testSeqs, Airlock spec, Class specClass, ArrayList<ArrayList<String>> oracle){
   
      int count = 0;//index of arraylist
      outerloop:
        for(ArrayList<String> seq: testSeqs){
            
         System.out.println(count);
            //testing....
            // if(count == 50){
            //     break;
            // }
         ArrayList<String> oracleChanges = oracle.get(count);
         System.out.println(oracleChanges);
      
            //count++;
            //NOT RELEVANT AT MOMENT
         ArrayList<String> total = new ArrayList<String>(startEvents);
         total.addAll(seq);
            //System.out.println(total);
         int indexInSeq = 0;
         for(String s : total){
            System.out.println(s);
            System.out.println(oracleChanges.get(indexInSeq));
            String[] controlledChanges = oracleChanges.get(indexInSeq).split(" ");
         
            String[] array = s.split(" ");
         
                //TO DO LATER--No booleans in current spec so all others will have an integer value
         
            if(isInteger(array[1])){
               callMethod(spec, specClass, array[0], Integer.parseInt(array[1]));
            }
            else{
                    //System.out.println(array[1]);
                    //callMethod(spec, specClass, array[0], getIntValue(spec, array[1]));
               callMethod(spec, specClass, array[0], spec.getSpecificValue(array[1]));
            }
            HashMap<String, Integer> genValues = spec.getValues();
                //check values with ones in log
                //System.out.println(genValues);
         
            if(!oracleChanges.get(indexInSeq).contains("NO CHANGE")){
               for(int x = 0; x < (controlledChanges.length/2); x++){
                  if(spec.getSpecificValue(controlledChanges[x+1]).intValue() != (genValues.get(controlledChanges[x])).intValue()){
                     System.out.println("-------ERROR-------");
                     System.out.println(spec.getSpecificValue(controlledChanges[x+1]) + " " + (genValues.get(controlledChanges[x])));
                     System.out.println("Expected: " + controlledChanges[x+1]);
                     System.out.println("Actual: " + spec.tempEnumReturn(genValues.get(controlledChanges[x])));
                            //System.out.println("ERROR");
                     return 1;
                     //break outerloop;
                  }
               }
            }
                //what if implemented changed but other didnt-- fix that. check old genValues with new to figure out if it is changed
            indexInSeq++;
         }
            //spec.printValues();
         spec.init();
         count++;
      }
      return 0;
      //return count;
        //System.out.println(count);
   }
}