class Airlocksmall extends Airlock{
   HashMap<String, Integer> variabletoInt;
   public Airlocksmall() {
      init();
        //construct map
        //variabletoInt 
   
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