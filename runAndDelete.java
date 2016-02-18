import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import javax.tools.*;
//import java.nio.file.Files;

class runAndDelete{
   public static void main(String[] args){
      System.out.println("Running and deleting files");
      
      //READ IN TEST GEN STUFF
      ArrayList<ArrayList<String>> testSeqs = readEventsIn(); 
      ArrayList<ArrayList<String>> oracle = readOracleLog();
      
      System.out.println(oracle);
      int numFiles = 0;
      //READ IN NUM OF TESTS
      try{
         BufferedReader readNum = new BufferedReader(new FileReader("NumVariations"));
         numFiles = Integer.parseInt(readNum.readLine());
         System.out.println(numFiles);
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
      
      //RUN AND DELETE EACH VARIATION 
      int failed = 0;
      int actualTotal = numFiles;
      for(int x = 0; x < numFiles; x++){
         int change = runGeneratedCode(new ArrayList<String>(), testSeqs, oracle, x);
         if(change != -1){
            failed += change; 
         }
         else{
            actualTotal = actualTotal - 1;
         }
      }
      
      for(int x = 0; x < numFiles; x++){
         deleteFile("Airlock" + x + ".java");
         deleteFile("Airlock" + x + ".class");
      }
      
      System.out.println(failed + " " + actualTotal);
   }

   public static int runGeneratedCode(ArrayList<String> startEvents, ArrayList<ArrayList<String>> testSeqs, ArrayList<ArrayList<String>> oracle, int num){
      
      Class<?> specClass = null;
      Constructor<?> ctor = null;
      Object spec = null;
      //String className = "Airlock"+num;
      
      try{
         specClass = Class.forName("Airlock"+num);
         ctor = specClass.getConstructor();
         spec = ctor.newInstance(new Object[]{});
      }
      catch(Exception e){ 
         e.printStackTrace();
         return -1; 
      }
      callInitMethod(spec, specClass);
      
      int count = 0;//index of arraylist
      outerloop:
        for(ArrayList<String> seq: testSeqs){
            
         System.out.println(count);
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
               //callMethod(spec, specClass, array[0], spec.getSpecificValue(array[1]));
               callMethod(spec, specClass, array[0], callGetValueMethod(spec, specClass,array[1]));
            }
            
            HashMap<String, Integer> genValues = new HashMap<String, Integer>();
            genValues.put("cInDoor", callGetValueMethod(spec, specClass, "cInDoor"));
            genValues.put("cOutDoor", callGetValueMethod(spec, specClass, "cOutDoor"));
            genValues.put("cChPres", callGetValueMethod(spec, specClass, "cChPres"));
            
            //HashMap<String, Integer> genValues = callGetHashMethod(spec, specClass);
            //HashMap<String, Integer> genValues = spec.getValues();
                //check values with ones in log
                //System.out.println(genValues);
         
            if(!oracleChanges.get(indexInSeq).contains("NO CHANGE")){
               for(int x = 0; x < (controlledChanges.length/2); x++){
                  if(callGetValueMethod(spec, specClass,controlledChanges[x+1]).intValue() != (genValues.get(controlledChanges[x])).intValue()){
                     System.out.println("-------ERROR-------");
                     System.out.println(callGetValueMethod(spec, specClass,controlledChanges[x+1]) + " " + (genValues.get(controlledChanges[x])));
                     System.out.println("Expected: " + controlledChanges[x+1]);
                     //System.out.println("Actual: " + spec.tempEnumReturn(genValues.get(controlledChanges[x])));
                     return 1;
                     //break outerloop;
                  }
               }
            }
                //what if implemented changed but other didnt-- fix that. check old genValues with new to figure out if it is changed
            indexInSeq++;
         }
            //spec.printValues();
         callInitMethod(spec, specClass);
         //spec.init();
         count++;
      }
      return 0;
      //return count;
        //System.out.println(count);
   }
   
     //=====================================================
    //* Calls a method based on name in generated code    *
    //=====================================================  
   public static void callMethod(Object spec, Class specClass, String methodName, Integer value){
      methodName = "change"+methodName;
      Method[] methods = specClass.getDeclaredMethods();
      for (Method method : methods) {
         try{
            if(method.getName().equals(methodName)){
               method.invoke(spec, value);
            }
         }
         catch(Exception e){  }
      }        
   }
   
   public static Integer callGetValueMethod(Object spec, Class specClass, String param){
      Method[] methods = specClass.getDeclaredMethods();
      for(Method method: methods){
         try{
            if(method.getName().equals("getSpecificValue")){
               return ((Integer)(method.invoke(spec, param))).intValue();
            }
         }
         catch(Exception e){}
      }
      return -1;
   }
   
   public static void callInitMethod(Object spec, Class specClass){
      Method[] methods = specClass.getDeclaredMethods();
      for(Method method: methods){
         try{
            if(method.getName().equals("init")){
               method.invoke(spec);
            }
         }
         catch(Exception e){ }
      }
   }
   
   public static void deleteFile(String filename){
      try{
         File file = new File("D:\\New Folder\\"+filename);
         //File file = new File("C:\\Users\\Valerie\\Documents\\School\\12th Grade\\New Folder\\"+filename);
         if(file.delete()){
            System.out.println(file.getName() + " is deleted!");
         }
         else{
            System.out.println("Delete operation is failed.");
         }
            
      }
      catch(Exception e){
         
      }
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
   
   public static ArrayList<ArrayList<String>> readEventsIn(){
      ArrayList<ArrayList<String>> allSeqs = new ArrayList<ArrayList<String>>();
      try{
         System.out.println("reading from events file...");
         
         //heuristic 4
         //BufferedReader readFile = new BufferedReader(new FileReader("Airlock_ID_memocode.ev"));
         
         //initial
         BufferedReader readFile = new BufferedReader(new FileReader("Airlock_ID_memocodeRANDOM4.ev"));
         
         
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
         //e.printStackTrace();
      }
      System.out.println(allSeqs.size());
      return allSeqs;
   }

   public static ArrayList<ArrayList<String>> readOracleLog(){
      try{
         System.out.println("reading from log file...");
         
         //HEURISTIC
         //BufferedReader readFile = new BufferedReader(new FileReader("Airlock_ID_memocodeFOUR.log"));
         
         //INITIAL
         BufferedReader readFile = new BufferedReader(new FileReader("Airlock_ID_memocodeRANDOM4.log"));

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
         //e.printStackTrace();
      }
      return null;
   }

}