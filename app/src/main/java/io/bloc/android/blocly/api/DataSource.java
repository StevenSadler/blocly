package io.bloc.android.blocly.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.bloc.android.blocly.BloclyApplication;
import io.bloc.android.blocly.R;
import io.bloc.android.blocly.api.model.RssFeed;
import io.bloc.android.blocly.api.model.RssItem;
import io.bloc.android.blocly.api.network.GetFeedsNetworkRequest;
import io.bloc.android.blocly.api.network.GetFeedsNetworkRequest.FeedResponse;
import io.bloc.android.blocly.api.network.GetFeedsNetworkRequest.ItemResponse;

/**
 * Created by Steven on 11/30/2015.
 */
public class DataSource {

    private List<RssFeed> feeds;
    private List<RssItem> items;

    public DataSource() {
        feeds = new ArrayList<RssFeed>();
        items = new ArrayList<RssItem>();
        //createFakeData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FeedResponse> feedResponses = new GetFeedsNetworkRequest("http://feeds.feedburner.com/androidcentral?format=xml").performRequest();
                convertFeedResponses(feedResponses);

//                Date longDate = new Date(0l);
//                Log.i("DataSource", " testLongDate   " + longDate.toString());
//                        //+ " : long " + Long.valueOf(longDate.toString()));
//
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
//                Date stringDate = null;
//                try{
//                    stringDate = simpleDateFormat.parse("Fri, 11 Dec 2015 21:48:29 GMT");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    return;
//                }
//
//                Log.i("DataSource", " testStringDate   " + stringDate.toString());
//
//                long stringDateLong = stringDate.getTime();
//                Date newDate = new Date(stringDateLong);
//
//                Log.i("DataSource", " testNewDate   " + newDate.toString());
//                //Log.i("DataSource", " Long.valueOf(stringDate.toString()) " + Long.valueOf(stringDate.toString()));


            }
        }).start();
    }

    public List<RssFeed> getFeeds() {
        return feeds;
    }

    public List<RssItem> getItems() {
        return items;
    }

    void createFakeData() {
        feeds.add(new RssFeed("My Favorite Feed",
                "This feed is just incredible, I can't even begin to tell you...",
                "http://favoritefeed.net",
                "http://feeds.feedburner.com/favorite_feed?format=xml"));
        for (int i = 0; i < 10; i++) {
            items.add(new RssItem(String.valueOf(i),
                    BloclyApplication.getSharedInstance().getString(R.string.placeholder_headline) + " " + i,
                    BloclyApplication.getSharedInstance().getString(R.string.placeholder_content),
                    "http://favoritefeed.net?story_id=an-incredible-news-story",
                    "https://upload.wikimedia.org/wikipedia/en/5/56/Loopy_Funny_Dog.jpg",
                    0, System.currentTimeMillis(), false, false, false));
        }
    }

    void convertFeedResponses(List<FeedResponse> feedResponses) {

        int itemGuid = 0;
        long longFeedIndex = 0l;

        for (int feedIndex = 0; feedIndex < feedResponses.size(); feedIndex++) {

            /*
             * convert the feedResponse to an rssFeed
             * add the rssFeed to feeds list
             */

            FeedResponse feedResponse = feedResponses.get(feedIndex);
            feeds.add(new RssFeed(feedResponse.channelTitle,
                    feedResponse.channelDescription,
                    feedResponse.channelURL,
                    feedResponse.channelFeedURL));

            for (int itemIndex = 0; itemIndex < feedResponse.channelItems.size(); itemIndex++) {

                /*
                 * convert the itemResponse to an rssItem
                 * add unique tracking guid to each rssItem
                 * add feed tracking int to each rssItem
                 * add the rssItem to items list
                 */

                ItemResponse itemResponse = feedResponse.channelItems.get(itemIndex);
                String imageUrl = "https://upload.wikimedia.org/wikipedia/en/5/56/Loopy_Funny_Dog.jpg";

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

                Date pubDate = new Date(0l);
                try{
                    pubDate = simpleDateFormat.parse(itemResponse.itemPubDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Log.i("DataSource", "convertFeedResponses : pubDate.toString() = " + pubDate.toString());
                long longPubDate = pubDate.getTime();
                items.add(new RssItem(String.valueOf(itemGuid),
                        itemResponse.itemTitle,
                        itemResponse.itemDescription,
                        itemResponse.itemURL,
                        imageUrl,
                        longFeedIndex,
                        longPubDate,
                        false,
                        false,
                        false));

                itemGuid++;
            }
            longFeedIndex++;
        }
    }
}
