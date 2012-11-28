package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * 以下のTweet関連の機能を保持するクラス
 * ・OAuth認可をつかってツイートする。
 * ・指定したユーザのタイムラインを取得する。
 * ・ツイートの検索をする。
 * 
 * @author keichanzahorumon
 *
 */
public class TweetControler {
    
    Twitter twitter = TwitterFactory.getSingleton();
    
    // getTimeLineで何ページ目のツイートかを指定する。(1以上を指定)
    private static final int PAGE_NUMBER = 1;
    
    // searchTweetsで何ページ取得するかを指定する。
    private static final int PAGE_COUNT = 1;
    
    // 1ページ分の取得件数を指定する。(最大100まで指定可能。未設定:-1)
    private static final int RETURN_PER_PAGE = 100;
    
    /**
     * OAuth認可を使ってツイートする
     * 
     * @param message ツイート内容
     */
    public void tweet(String message) {
        System.out.println("■Statrt tweet■");
        
        try {
            // accessToken がすでに有効な場合はエラー
            RequestToken requestToken = twitter.getOAuthRequestToken();
            
            System.out.println("Request token: " + requestToken.getToken());
            System.out.println("Request token secret: " + requestToken.getTokenSecret());
            
            AccessToken accessToken = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            // PINが入力されるまで待ち
            while (accessToken == null) {
                System.out.println("以下のURLにアクセスして承認してくらさい。");
                System.out.println(requestToken.getAuthorizationURL());
                System.out.print("PINを入力して、ENTER押してくらさい。 [PIN]:");
                
                String pin = br.readLine();
                
                try {
                    if (pin.length() > 0) {
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    } else {
                        accessToken = twitter.getOAuthAccessToken();
                    }
                } catch (TwitterException te) {
                    if (401 == te.getStatusCode()) {
                        System.out.println("accessToken の取得に失敗。。。");
                    } else {
                        te.printStackTrace();
                    }
                }
            }
            
            // ツイートする
            Status status = twitter.updateStatus(message);
            System.out.println("ツイートに成功 : [" + status.getText() + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("OAuthトークンの取得に失敗: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("System input の取得に失敗。。。");
            System.exit(-1);
        }
        System.out.println("■End tweet■");
    }
    
    /**
     * 指定したユーザのタイムラインの内容を取得する。
     * 以下は出力形式
     * "時間@ユーザ名--ツイート内容"
     * 
     * @param user 取得したタイムラインのユーザ名
     */
    public void getTimeLine(String user) {
        System.out.println("■Statrt getTimeLine■");
        
        try {
            // 指定したページ番号のタイムラインを取得
            ResponseList<Status> statuses = twitter.getUserTimeline(user, new Paging(PAGE_NUMBER));
            
            // 取得したタイムラインの数だけ出力
            for (Status status : statuses) {
                System.out.println(status.getCreatedAt() + "@" + status.getUser().getName() + "--" + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("指定したユーザのタイムラインの取得に失敗。。。");
            System.exit(-1);
        }
        System.out.println("■End getTimeLine■");        
    }
    
    /**
     * 指定した文字列を含むツイートを検索する。
     * 
     * @param query
     */
    public void searchTweets(String queryString) {
        System.out.println("■Statrt searchTweets■");
        
        Query query = new Query(queryString);
        
        // 1ページ分の取得件数を指定
        query.setRpp(RETURN_PER_PAGE);
        
        for (int i = 1; i <= PAGE_COUNT; i++) {

            // ページ数を指定
            query.setPage(i);
            QueryResult result;
            try {
                // 検索
                result = twitter.search(query);
           
                // 検索結果の分だけ検索内容を出力
                for (Tweet tweet : result.getTweets()) {
                    System.out.println(tweet.getText());
                }
            
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("指定した文字列での検索に失敗。。。");
                System.exit(-1);
            }
        }
        System.out.println("■End searchTweets■");
    }
}
