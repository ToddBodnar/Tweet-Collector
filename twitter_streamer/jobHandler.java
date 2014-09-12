/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter_streamer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
/**
 *
 * @author toddbodnar
 */
public class jobHandler implements StatusListener{
    
    public jobHandler(String title)
    {
        this.title=title;
    }
    
    public jobHandler()
    {
        this("");
    }
    
    PrintStream out = null;
    
    public void onStatus(Status status) {
        
            
            try {
            File theFile = new File("tweets");
            if(!theFile.exists() || out == null || out.checkError())
            {
                if(!theFile.exists())
                    theFile.createNewFile();
                
            
            out = new PrintStream(new FileOutputStream(theFile,true));
            
            
            }
            } catch (IOException ex) {
                    Logger.getLogger(jobHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
        
            
        out.println(status.getId()+"|"+TwitterObjectFactory.getRawJSON(status)+"\n");
              
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onStallWarning(StallWarning warning) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onException(Exception ex) {
             //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            String title;
}
 

