/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter_streamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author toddbodnar
 */
public class pull_stream {
    public static void main(String args[]) throws IOException, InterruptedException, Exception
    {
        if(args.length > 0 && args[0].equalsIgnoreCase("help"))
        {
            displayHelp();
            System.exit(0);
        }
        //check if process is already running
        File runLock = new File(".pull_stream_run_lock");
        if(runLock.exists())
        {
            System.out.println("run lock is set, assuming another puller process is running.");
            System.out.println();
            System.out.println("If this seems wrong, then delete .pull_stream_run_lock");
            System.out.println("Goodbye");
            System.exit(0);
        }
        runLock.createNewFile();
        runLock.deleteOnExit();
        
        //read the config file
        Properties settings_file = new Properties();
        settings_file.load(new FileInputStream(new File("settings.properties")));
        
        String job = readProp(settings_file);
       
        String type = job.split(";")[0];
        //jobHandler theJob = null;
        FilterQuery fq = new FilterQuery();
        
        if(type.equals("geo"))
        {
            double locations[][] = new double[2][2];
            String splits[] = job.split(";");
            
            locations[0][0] = Double.parseDouble(splits[1]);
            locations[0][1] = Double.parseDouble(splits[2]);
            locations[1][0] = Double.parseDouble(splits[3]);
            locations[1][1] = Double.parseDouble(splits[4]);
            
            fq.locations(locations);
            
        }else if(type.equals("keyword"))
        {
            String splits[] = job.split(";");
            String words[] = new String[splits.length-1];
            for(int ct=0;ct<words.length;ct++)
                words[ct] = splits[ct+1];
            
           fq.track(words);
        }else if(type.equals("follow"))
        {
         
            String splits[] = job.split(";");
            long users[] = new long[splits.length-1];
            for(int ct=0;ct<users.length;ct++)
                users[ct] = Long.parseLong(splits[ct+1]);
            
           fq.follow(users);
           
        }else
        {
            System.err.println("Error, invalid job file!");
            System.exit(-1);
        }
        
        //run the stream reader
        TwitterStream twitter = null;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
        
        cb.setOAuthAccessToken(settings.OAUTH_ACCESS_TOKEN);
        cb.setOAuthAccessTokenSecret(settings.OAUTH_ACCESS_TOKEN_SECERET);
        
        cb.setOAuthConsumerKey(settings.OAUTH_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(settings.OAUTH_CONSUMER_SECERET);
        
        
        TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        twitter = tf.getInstance();
        
        twitter.addListener(new jobHandler());
        
        twitter.filter(fq);
        
    }
    
    public static String readProp(Properties prop) throws Exception
      {
          settings.OAUTH_CONSUMER_KEY = prop.getProperty("OAUTH_CONSUMER_KEY");
            settings.OAUTH_CONSUMER_SECERET = prop.getProperty("OAUTH_CONSUMER_SECERET");
            settings.OAUTH_ACCESS_TOKEN = prop.getProperty("OAUTH_ACCESS_TOKEN");
            settings.OAUTH_ACCESS_TOKEN_SECERET = prop.getProperty("OAUTH_ACCESS_TOKEN_SECERET");
            
            settings.API_OUTPUT = prop.getProperty("OUTPUT_TYPE").equalsIgnoreCase("api");
            
            return prop.getProperty("JOB_CONFIG");
      }
    
    private static void displayHelp()
    {
        System.out.println("Twitter Stream Collector");
        System.out.println("(c) Todd Bodnar 2014-2015");
        System.out.println();
        System.out.println("Settings File Key:");
        System.out.println();
        System.out.println("OAUTH_CONSUMER_KEY\t\tConsumer Public Key provided by Twitter");
        System.out.println("OAUTH_CONSUMER_SECERET\t\tConsumer Secret Key provided by Twitter");
        System.out.println("OAUTH_ACCESS_TOKEN\t\tAccess Token provided by Twitter");
        System.out.println("OAUTH_ACCESS_TOKEN_SECERET\tSecret Access Token provided by Twitter");
        System.out.println();System.out.println();
        System.out.println("JOB_CONFIG\t\t\tIndividual job configuration. One of four options:");
        System.out.println("\t1. geo;x1;y1;x2;y2\t\tCollect tweets in the given location, defined by lat and lon coords. x1 < x2, y1 < y2");
        System.out.println("\t2. keyword;alpha;beta;delta...\tCollect tweets with any of the given keywords");
        System.out.println("\t3. follow;u1;u2;u3...\t\tCollect tweets from users with id u1,u2,u3...");
        System.out.println();System.out.println();
        System.out.println("OUTPUT_TYPE\t\t\tEither API (default) or SIMPLE to either output the json from the api or a simplified, tsv (timestamp,userid,text)");
        
    }
}
