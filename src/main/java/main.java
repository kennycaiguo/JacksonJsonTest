import java.io.*;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonInclude;
import Tweet.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class main {
    public static void main(String args[]) throws FileNotFoundException {
        StringBuffer str = new StringBuffer("");
        FileReader fr = new FileReader("/root/cloudberry/examples/twittermap/noah/src/test/resources/sample.json");
        BufferedReader bfr = new BufferedReader(fr);
        String ln = null;
        //
        int ok = 0;
        int notok = 0;
        //
        try {
            File fn = new File("JacksonJson.txt");
            File fn2 = new File("ErrorTweets.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(fn, true));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(fn2, true));
            ObjectMapper om = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Long parseTime = 0L;
            //
            while ((ln = bfr.readLine()) != null) {
//                parseTime =ReadSpecificFields(ln, om);
                parseTime =ReadAllFields(ln, om);
                if (parseTime > 0) {
                    bw.write(parseTime + "\r\n");
                    ok++;
                } else {
                    notok++;
                    bw2.write(ln);
                }
            }
            bw.close();
            bw2.close();
            bfr.close();
            fr.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("#Parsed: " + ok + ", #Not parsed: " + notok);
    }

    public static Long ReadAllFields(String ln, ObjectMapper om) {
        try {
            Long s = System.nanoTime();
            MyTweet mt = om.readValue(ln, MyTweet.class);
            Long e = System.nanoTime();
            return e - s;
        } catch (Exception ex) {
            return 0L;

        }
    }

    public static Long ReadSpecificFields(String ln, ObjectMapper om) {
        try {
            Long s = System.nanoTime();
            JsonNode rootNode = om.readTree(ln);
            String tweetCreatedAt = rootNode.path("created_at").asText();
            Long tweetId = rootNode.path("id").asLong();
            String tweetText = rootNode.path("text").asText();
            Long tweetInReplyToStatusId = rootNode.path("in_reply_to_status_id").asLong();
            Long tweetInReplytoUserId = rootNode.path("in_reply_to_user_id").asLong();
            JsonNode tweetUser = rootNode.path("user");//subnodes
            JsonNode tweetCoordinates = rootNode.path("coordinates");//subnodes
            JsonNode tweetPlace = rootNode.path("place");//subnodes
            Long tweetRetweetCount = rootNode.path("retweet_count").asLong();
            Long tweetFavoriteCount = rootNode.path("favorite_count").asLong();
            String tweetLang = rootNode.path("lang").asText();
            JsonNode tweetEntities = rootNode.path("entities");//subnodes
            boolean tweetRetweeted = rootNode.path("retweeted").asBoolean();
            //
            if (!tweetUser.isNull()) {
                Long userId = tweetUser.path("id").asLong();
                String userName = tweetUser.path("name").asText();
                String userScreenName = tweetUser.path("screen_name").asText();
                String userProfileImageUrl = tweetUser.path("profile_image_url").asText();
                String userLang = tweetUser.path("lang").asText();
                String userLocation = tweetUser.path("location").asText();
                String userCreatedAt = tweetUser.path("created_at").asText();
                String userDescription = tweetUser.path("description").asText();
                int userfollowersCount = tweetUser.path("followers_count").asInt();
                int userFriendsCount = tweetUser.path("friends_count").asInt();
                Long userStatusesCount = tweetUser.path("statuses_count").asLong();
            }
            if (!tweetCoordinates.isNull()) {
                String coordinatesType = tweetCoordinates.path("type").asText();
                JsonNode coordinatesColl = tweetCoordinates.path("coordinates");
                if (!coordinatesColl.isNull()) {
                    Double[] LatLang = new Double[2];
                    LatLang[0]=coordinatesColl.get(0).asDouble();
                    LatLang[1]=coordinatesColl.get(1).asDouble();
                }
            }
            if (!tweetPlace.isNull()) {
                String placCountry = tweetPlace.path("country").asText();
                String placeCoutrnyCode = tweetPlace.path("country_code").asText();
                String placeFullName = tweetPlace.path("full_name").asText();
                String placeId = tweetPlace.path("id").asText();
                String placeName = tweetPlace.path("name").asText();
                String placeType = tweetPlace.path("placeType").asText();
                JsonNode placeBoundingBox = tweetPlace.path("bounding_box");
            }
            if (!tweetEntities.isNull()) {
                JsonNode hashTags = tweetEntities.path("hashtags");
            }
            Long e = System.nanoTime();
            return e-s;
        } catch (Exception ex) {
            return 0L;
        }

    }
}
