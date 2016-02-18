import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import javax.tools.*;
//import java.nio.file.Files;

class createAndCompile{
   public static void main(String[] args){
      System.out.println("Creating and Compiling files");
      
      String filename = "Airlock";
      
      //READ IN ORIGINAL IMPLEMENTATION FILE
      ArrayList<String> originalFile = readInFile("DONOTCHANGE");
      
      ArrayList<ArrayList<String>> variationFiles = generateMutatedFiles(originalFile, filename);
      for(int x = 0; x < variationFiles.size(); x++){
         writeFile(variationFiles.get(x), filename+x);
         compileFile(filename+x+".java");
      }
      
      //write out number of variations to a file
      try{
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("NumVariations"))); 
         bw.write(""+variationFiles.size());
         bw.close();
      }
      catch(IOException e){}
   }

   public static void writeFile(ArrayList<String> file, String filename){
      try{
         System.out.println("writing to file...");
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename+".java"))); //edit this to make multiple files
         for(String s : file){
            if(s.contains("Airlock")){
               s = s.replaceAll("Airlock", filename);
            }
            bw.write(s);
            bw.newLine();
         }
         bw.close();
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
   }
  
   //return all variations of code
   public static ArrayList<ArrayList<String>> generateMutatedFiles(ArrayList<String> originalFiles, String filename){
      HashMap<String, String> switched = switchedWords(filename);      
      ArrayList<ArrayList<String>> variationFiles = new ArrayList<ArrayList<String>>();
      //variationFiles.add(originalFiles);
      outerloop:
            for(int x = 0; x < originalFiles.size(); x++){
         String line = originalFiles.get(x);
                  //System.out.println(line);
         for(String toSwitch : switched.keySet()){
            if(line.contains(toSwitch) && line.contains("==")){
               ArrayList<String> copy = new ArrayList<String>(originalFiles);
               String switchWith = switched.get(toSwitch);
               String newLine = line.replaceAll(toSwitch, switchWith);
               copy.set(x, newLine);
               variationFiles.add(copy);
               //break outerloop;
            }
         }
      }
      return variationFiles;
   }
   
   public static boolean compileFile(String filename){ 
      try{
         JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
         DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
         StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
         Iterable<? extends JavaFileObject> compilationUnits = fileManager
              .getJavaFileObjectsFromStrings(Arrays.asList(filename));
         JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null,
              null, compilationUnits);
         boolean success = task.call();
         fileManager.close();
         System.out.println("Success: " + success);
         if(!success){
            deleteFile(filename);
         }
         return success;
         
      }
      catch(IOException e){ 
         return false;}
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
   
   //returns hashmap of commonly switched words
   public static HashMap<String, String> switchedWords(String filename){
      HashMap<String, String> switched = new HashMap<String, String>();
   	//read in file
      try{
         System.out.println("reading from variable mutations file...");
         BufferedReader readFile = new BufferedReader(new FileReader(filename + "VariableMutations"));
         String currentLine = null;
         while((currentLine = readFile.readLine())!= null){
            String[] array = currentLine.split(" ");
            switched.put(array[0], array[1]);
            switched.put(array[1], array[0]);
         }
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
      return switched;
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

}