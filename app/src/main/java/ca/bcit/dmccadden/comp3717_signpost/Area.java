package ca.bcit.dmccadden.comp3717_signpost;

/**
 * Created by knome on 2016-11-18.
 */

public class Area
{
    private static final double OFFSET = 200;
    private static final double BEARING_TOPLEFT = 315;
    private static final double BEARING_BOTRIGHT = 135;

    public String topLeftLon;
    public String topLeftLat;
    public String botRightLon;
    public String botRightLat;
    public static void main (String[] args) {
        Area a = new Area(-96, 32);
        System.out.println(a.topLeftLon + " , " + a.topLeftLat + " , " + a.botRightLon + " , " + a.botRightLat);
    }

    /**
     * @param lon Center longitude of the area.
     * @param lat Center latitude of the area.
     *
     * Calculates an area whose center is lon and lat and whose
     * width and height are the OFFSET * 2 (last I checked
     * that equaled 10 meters).
     *
     * Area coords are accessed by public topLeftLon, etc...
     * instance variables.
     */
    public Area(double lon, double lat) {
        Point topLeft = calLatLon(lon, lat, BEARING_TOPLEFT);
        Point bottomRight = calLatLon(lon, lat, BEARING_BOTRIGHT);
        topLeftLon = "" + topLeft.longitude;
        topLeftLat = "" + topLeft.latitude;
        botRightLon = "" + bottomRight.longitude;
        botRightLat = "" + bottomRight.latitude;
    }

    private  Point calLatLon(double lon, double lat, double bearing) {
        double earthRadious = 6378140; //metres
        bearing = deg2rad(bearing); //45 degrees
        lat = deg2rad(lat);
        lon = deg2rad(lon);
        double resultLat = Math.asin(Math.sin(lat) * Math.cos(OFFSET / earthRadious)
                + Math.cos(lat) * Math.sin(OFFSET / earthRadious) * Math.cos(bearing));
        double resultLon = lon + Math.atan2(Math.sin(bearing) * Math.sin(OFFSET / earthRadious) * Math.cos(lat),
                Math.cos(OFFSET / earthRadious) - Math.sin(lat) * Math.sin(resultLat));
        return new Point(rad2deg(resultLon), rad2deg(resultLat));
    }

    private class Point {
        public double longitude;
        public double latitude;
        Point(double lon, double lat) {
            longitude = lon;
            latitude = lat;
        }
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}