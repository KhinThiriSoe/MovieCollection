package com.khinthirisoe.mymoviecollection;

import java.util.Comparator;

public class GridItem {
    private String image;
    private String title;
    private String overview;
    private String userRating;
    private String releaseDate;
    private double popularity;

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getPopularity() {
        return popularity;
    }

    public GridItem() {
        super();
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Comparator<GridItem> PopularityComparator = new Comparator<GridItem>() {

        public int compare(GridItem g1, GridItem g2) {

            double p1 = g1.getPopularity();
            double p2 = g2.getPopularity();
            //ascending order
            return (int) (p2 - p1);
        }
    };

    public static Comparator<GridItem> RatingComparator = new Comparator<GridItem>() {

        public int compare(GridItem g1, GridItem g2) {
            int r1 = Integer.parseInt(g1.getUserRating());
            int r2 = Integer.parseInt(g2.getUserRating());

            return r2 - r1;
        }
    };
}