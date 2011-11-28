package thewebsemantic;
/**
 * This class enables to user operate over Model with user selected data, so
 * the Writer instance can write to model more deep - the selected properties
 * will be written together with the shallow one.
 * 
 */
public class Filler {

   private Object target;
   private RDF2Bean writer;

   public Filler(RDF2Bean w, Object o) {
      writer = w;
      target = o;
   }
   
   public void with(String propertyName) {
      writer.fill(target, propertyName);
   }

}
