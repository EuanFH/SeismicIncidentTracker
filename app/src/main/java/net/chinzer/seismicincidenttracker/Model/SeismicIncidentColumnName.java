//
// Name                 Euan Haahr
// Student ID           S1716375
// Programme of Study   Bsc Computing
//
package net.chinzer.seismicincidenttracker.Model;

public enum SeismicIncidentColumnName {
    DATETIME("dateTime", "Date & Time"),
    DEPTH("depth", "Depth"),
    MAGNITUDE("magnitude", "Magnitude"),
    SEVERITY("severity", "Severity"),
    LOCALITY("locality", "Locality"),
    LATITUDE("latitude", "Latitude"),
    LONGITUDE("longitude", "Longitude"),
    LINK("link", "Link");

    private String columnName;
    private String humanReadableColumnName;


    SeismicIncidentColumnName(String columnName, String humanReadableColumnName) {
        this.columnName = columnName;
        this.humanReadableColumnName = humanReadableColumnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getHumanReadableColumnName() {
        return humanReadableColumnName;
    }
}
