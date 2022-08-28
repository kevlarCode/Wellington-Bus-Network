package comp261.assig1;

// decide the data structure for stops
public class Stop {
   //probably always have these three    
   private GisPoint loc;
   private String name;
   private String id;

   // Constructor - Takes in the location, name and id of the stop
   public Stop(GisPoint locatoin, String name, String id) {
     this.loc = locatoin;
     this.name = name;
     this.id = id;
   }
   /* Getters */
   public String getName() {
      return name;
   }

   public String getId() {
      return id;
   }
  public GisPoint getPoint(){
     return loc;
  } 
}
