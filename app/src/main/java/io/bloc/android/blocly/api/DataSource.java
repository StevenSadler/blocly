package io.bloc.android.blocly.api;

import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocly.api.model.RssFeed;
import io.bloc.android.blocly.api.model.RssItem;

/**
 * Created by Steven on 11/30/2015.
 */
public class DataSource {

    private List<RssFeed> feeds;
    private List<RssItem> items;
    private String backgroundImageUrl;

    public DataSource() {
        feeds = new ArrayList<RssFeed>();
        items = new ArrayList<RssItem>();
        createFakeData();
    }

    public List<RssFeed> getFeeds() {
        return feeds;
    }

    public List<RssItem> getItems() {
        return items;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    void createFakeData() {
        feeds.add(new RssFeed("My Favorite Feed",
                "This feed is just incredible, I can't even begin to tell you...",
                "http://favoritefeed.net",
                "http://feeds.feedburner.com/favorite_feed?format=xml"));
        for (int i = 0; i < 10; i++) {
            items.add(new RssItem(String.valueOf(i),
                    "An incredible news story #" + i,
                    "You won't believe how exciting this news story is, get ready to be blown away by its amazingness.",
                    "http://favoritefeed.net?story_id=an-incredible-news-story",
                    "https://upload.wikimedia.org/wikipedia/en/5/56/Loopy_Funny_Dog.jpg",
                    0, System.currentTimeMillis(), false, false, false));
        }
        //backgroundImageUrl = "http://m5.paperblog.com/i/80/805796/the-worlds-top-10-best-images-of-dogs-wearing-L-6s9Zgt.jpeg";
        backgroundImageUrl = "http://www.photos-public-domain.com/wp-content/uploads/2011/01/red-pawprint-wall-decal-190x190.jpg";
    }
}
