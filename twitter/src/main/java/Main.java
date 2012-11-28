package main.java;

/**
 * エントリーポイント
 * 
 * @author keichanzahorumon
 *
 */
public class Main {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("☆☆Start☆☆");

        if (args.length != 2) {
            System.out.println("引数は2つで。  => (\"method\",\"querry\")");
            System.exit(-1);
        }

        String methodName = args[0];
        TweetControler tweetControler = new TweetControler();
        
        // ツイートする
        if (methodName.equals("tweet")) {
            tweetControler.tweet(args[1]);
        }

        // 指定した文字列を含むツイートを検索する
        else if (methodName.equals("search")) {
            tweetControler.searchTweets(args[1]);
        }

        // 指定したユーザ名のツイートを取得する
        else if (methodName.equals("timeline")) {
            tweetControler.getTimeLine(args[1]);
        }

        // 引数が不正の場合(第1引数が「tweet」「search」「timeline」でない場合)
        else {
            System.out.println("第1引数は「tweet」,「search」,「timeline」のどれかでよろしくです。");
            System.exit(-1);
        }

        System.out.println("☆☆Finished☆☆");
    }
}