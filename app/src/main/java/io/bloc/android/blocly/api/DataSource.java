package io.bloc.android.blocly.api;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.bloc.android.blocly.BloclyApplication;
import io.bloc.android.blocly.BuildConfig;
import io.bloc.android.blocly.R;
import io.bloc.android.blocly.api.model.RssFeed;
import io.bloc.android.blocly.api.model.RssItem;
import io.bloc.android.blocly.api.model.database.DatabaseOpenHelper;
import io.bloc.android.blocly.api.model.database.table.RssFeedTable;
import io.bloc.android.blocly.api.model.database.table.RssItemTable;
import io.bloc.android.blocly.api.network.GetFeedsNetworkRequest;

/**
 * Created by Steven on 11/30/2015.
 */
public class DataSource {

    private static String TAG = DataSource.class.getSimpleName();

    private DatabaseOpenHelper databaseOpenHelper;
    private RssFeedTable rssFeedTable;
    private RssItemTable rssItemTable;
    private List<RssFeed> feeds;
    private List<RssItem> items;

    public DataSource() {
        rssFeedTable = new RssFeedTable();
        rssItemTable = new RssItemTable();

        databaseOpenHelper = new DatabaseOpenHelper(BloclyApplication.getSharedInstance(),
                rssFeedTable, rssItemTable);

        feeds = new ArrayList<RssFeed>();
        items = new ArrayList<RssItem>();
        createFakeData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (BuildConfig.DEBUG && false) {
                    BloclyApplication.getSharedInstance().deleteDatabase("blocly_db");
                }
                SQLiteDatabase writeableDatabase = databaseOpenHelper.getWritableDatabase();
                List<GetFeedsNetworkRequest.FeedResponse> feedResponses =
                        new GetFeedsNetworkRequest("http://feeds.feedburner.com/androidcentral?format=xml").performRequest();
                GetFeedsNetworkRequest.FeedResponse androidCentral = feedResponses.get(0);

                /*
                 * search the feed table to see if this feed is already recorded
                 * if it is not a dupe, add it to the table and return the feedId
                 * if it is a dupe, return -1
                 */

                long androidCentralFeedId = new RssFeedTable.Builder()
                        .setFeedURL(androidCentral.channelFeedURL)
                        .setSiteURL(androidCentral.channelURL)
                        .setTitle(androidCentral.channelTitle)
                        .setDescription(androidCentral.channelDescription)
                        .insert(writeableDatabase);


                if (androidCentralFeedId != -1) {
                    Log.i(TAG, "insert feed : " + androidCentral.channelURL);
                } else {
                    Log.i(TAG, "insert feed FAILED, possible duplicate : " + androidCentral.channelURL);
                }

                for (GetFeedsNetworkRequest.ItemResponse itemResponse : androidCentral.channelItems) {
                    long itemPubDate = System.currentTimeMillis();
                    DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss z", Locale.ENGLISH);
                    try {
                        itemPubDate = dateFormat.parse(itemResponse.itemPubDate).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    /*
                     * search the item table to see if this item is already recorded
                     * if it is not a dupe, add it to the table and return itemId
                     * if it is a dupe return -1
                     */

                    long androidCentralItemId = new RssItemTable.Builder()
                            .setTitle(itemResponse.itemTitle)
                            .setDescription(itemResponse.itemDescription)
                            .setEnclosure(itemResponse.itemEnclosureURL)
                            .setMIMEType(itemResponse.itemEnclosureMIMEType)
                            .setLink(itemResponse.itemURL)
                            .setGUID(itemResponse.itemGUID)
                            .setPubDate(itemPubDate)

                            .setRSSFeed(androidCentralFeedId)
                            .insert(writeableDatabase);


                    if (androidCentralItemId != -1) {
                        Log.i(TAG, "insert item : " + itemResponse.itemGUID);
                    } else {
                        Log.i(TAG, "insert item FAILED, possible duplicate : " + itemResponse.itemGUID);
                    }

                }
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
}
