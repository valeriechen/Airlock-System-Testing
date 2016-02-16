import java.util.*;

public class Airlock {
   public static final int INPRES = (10000);
    
   public static final int CLOSEOUTDOOR = (10006);
    
   public static final int OUTPRES = (10001);
    
   public static final int OPENOUTDOOR = (10007);
    
   public static final int TOINPRES = (10002);
    
   public static final int CLOSEINDOOR = (10008);
    
   public static final int OPENINDOOR = (10009);
    
   public static final int CLOSED = (10005);
    
   public static final int TOOUTPRES = (10003);
    
   public static final int OPEN = (10004);
    
   protected int oldMInCmd;
    
   protected int newMInCmd;
    
   protected int oldMOutCmd;
    
   protected int newMOutCmd;
    
   protected int oldMChCmd;
    
   protected int newMChCmd;
    
   protected int oldCChPres;
    
   protected int newCChPres;
    
   protected int oldCInDoor;
    
   protected int newCInDoor;
    
   protected int oldCOutDoor;
    
   protected int newCOutDoor;
   
   public void init() {
        
      newMInCmd = (CLOSEINDOOR);
        
      oldMInCmd = (CLOSEINDOOR);
         
      newMOutCmd = (CLOSEOUTDOOR);
        
      oldMOutCmd = (CLOSEOUTDOOR);
        
      newMChCmd = (TOINPRES);
        
      oldMChCmd = (TOINPRES);
        
      newCChPres = (INPRES);
        
      oldCChPres = (INPRES);
        
      newCInDoor = (CLOSED);
        
      oldCInDoor = (CLOSED);
        
      newCOutDoor = (CLOSED);
        
      oldCOutDoor = (CLOSED);
      
   }
    
   private void update() {
        
        
      if (((oldCChPres) == (OUTPRES)) && ((newMChCmd) == (TOINPRES)) && ((oldCInDoor) == (CLOSED)) && ((oldCOutDoor) == (CLOSED)) && (!((oldMChCmd) == (TOINPRES)))) {
            
         newCChPres = (INPRES);
            
         controlCChPres((newCChPres));
      } 
      else if (((oldCInDoor) == (CLOSED)) && ((newMChCmd) == (TOOUTPRES)) && ((oldCChPres) == (INPRES)) && ((oldCOutDoor) == (CLOSED)) && (!((oldMChCmd) == (TOOUTPRES)))) {
            
         newCChPres = (OUTPRES);
            
         controlCChPres((newCChPres));
      }
        
      if (((newMInCmd) == (CLOSEINDOOR)) && ((oldCInDoor) == (OPEN)) && (!((oldMInCmd) == (CLOSEINDOOR))) && ((oldCChPres) == (INPRES))) {
            
         newCInDoor = (CLOSED);
            
         controlCInDoor((newCInDoor));
      } 
      else if (((oldCInDoor) == (CLOSED)) && (!((oldMInCmd) == (OPENINDOOR))) && ((oldCChPres) == (INPRES)) && ((newMInCmd) == (OPENINDOOR))) {
            
         newCInDoor = (OPEN);
            
         controlCInDoor((newCInDoor));
      }
        
      if ((!((oldMOutCmd) == (CLOSEOUTDOOR))) && ((oldCOutDoor) == (OPEN)) && ((newMOutCmd) == (CLOSEOUTDOOR)) && ((oldCChPres) == (OUTPRES))) {
            
         newCOutDoor = (CLOSED);
            
         controlCOutDoor((newCOutDoor));
      } 
      else if (((oldCChPres) == (OUTPRES)) && (!((oldMOutCmd) == (OPENOUTDOOR))) && ((oldCOutDoor) == (CLOSED)) && ((newMOutCmd) == (OPENOUTDOOR))) {
            
         newCOutDoor = (OPEN);
            
         controlCOutDoor((newCOutDoor));
      }
        
      oldCChPres = newCChPres;
        
      oldCInDoor = newCInDoor;
        
      oldCOutDoor = newCOutDoor;
   }
    
   public void changemInCmd(int value) {
        
      newMInCmd = value;
        
      update();
        
      oldMInCmd = newMInCmd;
   }
    
   public void changemOutCmd(int value) {
        
      newMOutCmd = value;
        
      update();
        
      oldMOutCmd = newMOutCmd;
   }
    
   public void changemChCmd(int value) {
      newMChCmd = value;
        
      update();
        
      oldMChCmd = newMChCmd;
   }
    
   public String toString(){
      return "here";
   }

   public String tempEnumReturn(Object value){
      if(value instanceof Boolean){
         return ""+value;
      }
      else if(value instanceof Integer){
         if(((Integer)value).intValue() < 10000){
            return ""+value;
         }
         if(((Integer)value).intValue() == 10004){
            return "OPEN";
         }
         else if(((Integer)value).intValue() == 10000){
            return "INPRES";
         }
         else if(((Integer)value).intValue() == 10001){
            return "OUTPRES";
         }
         else if(((Integer)value).intValue() == 10002){
            return "TOINPRES";
         }
         else if(((Integer)value).intValue() == 10003){
            return "TOOUTPRES";
         }
         else if(((Integer)value).intValue() == 10006){
            return "CLOSEOUTDOOR";
         }
         else if(((Integer)value).intValue() == 10007){
            return "OPENOUTDOOR";
         }
         else if(((Integer)value).intValue() == 10008){
            return "CLOSEINDOOR";
         }
         else if(((Integer)value).intValue() == 10009){
            return "OPENINDOOR";
         }
         else if(((Integer)value).intValue() == 10005){
            return "CLOSED";
         }
      }
    
      return null;
   }

   public Integer getSpecificValue(String actualName){
      if(actualName.equals("InPres")){
         return 10000;
      }
      if(actualName.equals("CloseOutDoor")){
         return 10006;
      }
      if(actualName.equals("OutPres")){
         return 10001;
      }
      if(actualName.equals("OpenOutDoor")){
         return 10007;
      }
      if(actualName.equals("ToInPres")){
         return 10002;
      }
      if(actualName.equals("CloseInDoor")){
         return 10008;
      }
      if(actualName.equals("OpenInDoor")){
         return 10009;
      }
      if(actualName.equals("closed")){
         return 10005;
      }
      if(actualName.equals("ToOutPres")){
         return 10003;
      }
      if(actualName.equals("open")){
         return 10004;
      }
      return -1;
   }
   public HashMap<String, Integer> getValues(){
      HashMap<String, Integer> values = new HashMap<String, Integer> ();
        //ONLY CONTROLLED VARIABLES AS OF RIGHT NOW
      values.put("cInDoor", newCInDoor);
      values.put("cOutDoor", newCOutDoor);
      values.put("cChPres", newCChPres); 
      return values;
   }
   public void printValues(){
      System.out.println("newMInCmd " + tempEnumReturn(newMInCmd));
      System.out.println("newMOutCmd " + tempEnumReturn(newMOutCmd));
      System.out.println("newMChCmd " + tempEnumReturn(newMChCmd));
      System.out.println("newCChPres " + tempEnumReturn(newCChPres));
      System.out.println("newCInDoor " + tempEnumReturn(newCInDoor));
      System.out.println("newCOutDoor " + tempEnumReturn(newCOutDoor));
   }

   protected void controlCInDoor(int value){
      oldCInDoor = newCInDoor;
      newCInDoor = value;
   }
    
   protected void controlCOutDoor(int value){
      oldCOutDoor = newCOutDoor;
      newCOutDoor = value;
   }
    
   protected void controlCChPres(int value){
      oldCChPres = newCChPres;
      newCChPres = value;
   }
    
}
