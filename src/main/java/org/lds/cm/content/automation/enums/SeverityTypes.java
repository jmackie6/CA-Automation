package org.lds.cm.content.automation.enums;

public enum SeverityTypes {
    INFO(1)
    , WARNING(2)
    , ERROR(3)
    , FATAL(4);

   private final int enumId;

   SeverityTypes(int newValue){ this.enumId = newValue;}

   public int enumId(){
       return enumId;
   }

   public static SeverityTypes getTypeFromInt(int enumValue){
       switch(enumValue){
           case 1:
               return SeverityTypes.INFO;
           case 2:
               return SeverityTypes.WARNING;
           case 3:
               return SeverityTypes.ERROR;
           case 4:
               return SeverityTypes.FATAL;
       }

       return null;
   }

   public static SeverityTypes getTypeFromString(String newEnumValue){
       switch(newEnumValue){
           case "INFO":
               return SeverityTypes.INFO;
           case "WARNING":
               return SeverityTypes.WARNING;
           case "ERROR":
               return SeverityTypes.ERROR;
           case "FATAL":
               return SeverityTypes.FATAL;
           default:
               return null;
       }
   }

}
