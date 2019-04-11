//
// Name                 Euan Haahr
// Student ID           S1716375
// Programme of Study   Bsc Computing
//
package net.chinzer.seismicincidenttracker.Model;

import android.os.Parcel;
import android.os.Parcelable;

import net.chinzer.seismicincidenttracker.Converters.DateTimeTypeConverters;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;

@Entity(tableName = "seismic_incidents", primaryKeys = {"dateTime", "latitude", "longitude"})
@TypeConverters({DateTimeTypeConverters.class})
public class SeismicIncident implements Parcelable {
    @NonNull
    private OffsetDateTime dateTime;
    private int depth;
    private double magnitude;
    private String severity;
    private String locality;
    @NonNull
    private double latitude;
    @NonNull
    private double longitude;
    private String link;

    @Ignore
    public SeismicIncident(OffsetDateTime dateTime, int depth, double magnitude, String locality, double latitude, double longitude, String link) {
        this.dateTime = dateTime;
        this.depth = depth;
        this.magnitude = magnitude;
        this.severity = calculateSeverity(magnitude);
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.link = link;
    }

    public SeismicIncident(OffsetDateTime dateTime, int depth, double magnitude, String severity, String locality, double latitude, double longitude, String link) {
        this.dateTime = dateTime;
        this.depth = depth;
        this.magnitude = magnitude;
        this.severity = severity;
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.link = link;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String calculateSeverity(double magnitude){
        String severity = "";
        if (magnitude <= 1.9){
            severity = "Micro";
        }
        else if (magnitude >= 2.0 && magnitude <= 3.9){
            severity = "Minor";

        }
        else if (magnitude >= 4.0 && magnitude <= 4.9){
            severity = "Light";

        }
        else if (magnitude >= 5.0 && magnitude <= 5.9){
            severity = "Moderate";

        }
        else if (magnitude >= 6.0 && magnitude <= 6.9){
            severity = "Strong";

        }
        else if (magnitude >= 7.0 && magnitude <= 7.9){
            severity = "Major";

        }
        else if (magnitude >= 8.0){
            severity = "Great";

        }
        return severity;
    }

    //parcel magic
    protected SeismicIncident(Parcel in) {
        dateTime = DateTimeTypeConverters.fromDateTimetoOffset(in.readString());
        depth = in.readInt();
        magnitude = in.readDouble();
        severity = in.readString();
        locality = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        link = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DateTimeTypeConverters.fromOffsettoDatetime(dateTime));
        dest.writeInt(depth);
        dest.writeDouble(magnitude);
        dest.writeString(severity);
        dest.writeString(locality);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(link);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SeismicIncident> CREATOR = new Creator<SeismicIncident>() {
        @Override
        public SeismicIncident createFromParcel(Parcel in) {
            return new SeismicIncident(in);
        }

        @Override
        public SeismicIncident[] newArray(int size) {
            return new SeismicIncident[size];
        }
    };
}
