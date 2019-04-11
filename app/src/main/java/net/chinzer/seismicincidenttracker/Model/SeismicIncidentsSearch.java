//
// Name                 Euan Haahr
// Student ID           S1716375
// Programme of Study   Bsc Computing
//
package net.chinzer.seismicincidenttracker.Model;

import net.chinzer.seismicincidenttracker.Converters.DateTimeTypeConverters;

import java.time.OffsetDateTime;
import java.time.OffsetTime;

public class SeismicIncidentsSearch {
    private String locality;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private OffsetTime startTime;
    private OffsetTime endTime;
    private Double startMagnitude;
    private Double endMagnitude;
    private Integer startDepth;
    private Integer endDepth;
    private String severity;

    public SeismicIncidentsSearch(String locality, OffsetDateTime startDate, OffsetDateTime endDate, OffsetTime startTime, OffsetTime endTime, Double startMagnitude, Double endMagnitude, Integer startDepth, Integer endDepth, String severity) {
        this.locality = locality;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startMagnitude = startMagnitude;
        this.endMagnitude = endMagnitude;
        this.startDepth = startDepth;
        this.endDepth = endDepth;
        this.severity = severity;
    }

    public SeismicIncidentsSearch(String locality, String startDate, String endDate, String startTime, String endTime, String startMagnitude, String endMagnitude, String startDepth, String endDepth, String severity) {
        this.locality = locality;
        setStartDate(startDate);
        setEndDate(endDate);
        setStartTime(startTime);
        setEndTime(endTime);
        setStartMagnitude(startMagnitude);
        setEndMagnitude(endMagnitude);
        setStartDepth(startDepth);
        setEndDepth(endDepth);
        setSeverity(severity);
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }
    public String getStartDateString() {
        if(startDate == null){
            return "";
        }
        return DateTimeTypeConverters.fromOffsetDateTimeToUserInputDate(startDate);
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(String startDate) {
        if(!startDate.isEmpty()){
            this.startDate = DateTimeTypeConverters.fromUserInputDateToOffsetDateTime(startDate);
        }
        else {
            this.startDate = null;
        }
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public String getEndDateString() {
        if(endDate == null){
            return "";
        }
        return DateTimeTypeConverters.fromOffsetDateTimeToUserInputDate(endDate);
    }


    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(String endDate) {
        if(!endDate.isEmpty()){
            this.endDate = DateTimeTypeConverters.fromUserInputDateToOffsetDateTime(endDate);
        }
        else{
            this.endDate = null;
        }
    }

    public OffsetTime getStartTime() {
        return startTime;
    }

    public String getStartTimeString() {
        if(startTime == null){
            return "";
        }
        return DateTimeTypeConverters.fromOffsetTimeToUserInputTime(startTime);
    }


    public void setStartTime(OffsetTime startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime) {
        if(!startTime.isEmpty()){
            this.startTime = DateTimeTypeConverters.fromUserInputTimeToOffsetTime(startTime);
        }
        else{
            this.startTime = null;
        }
    }

    public OffsetTime getEndTime() {
        return endTime;
    }

    public String getEndTimeString() {
        if(endTime == null){
            return "";
        }
        return DateTimeTypeConverters.fromOffsetTimeToUserInputTime(endTime);
    }


    public void setEndTime(OffsetTime endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime) {
        if(!endTime.isEmpty()){
            this.endTime = DateTimeTypeConverters.fromUserInputTimeToOffsetTime(endTime);
        }
        else{
            this.endTime = null;
        }
    }

    public Double getStartMagnitude() {
        return startMagnitude;
    }

    public String getStartMagnitudeString() {
        if(endMagnitude == null){
            return "";
        }
        return String.valueOf(startMagnitude);
    }

    public void setStartMagnitude(Double startMagnitude) {
        this.startMagnitude = startMagnitude;
    }

    public void setStartMagnitude(String startMagnitude) {
        this.startMagnitude = getDoubleFromString(startMagnitude);
    }

    public Double getEndMagnitude() {
        return endMagnitude;
    }

    public String getEndMagnitudeString() {
        if(endMagnitude == null){
            return "";
        }
        return String.valueOf(endMagnitude);
    }


    public void setEndMagnitude(Double endMagnitude) {
        this.endMagnitude = endMagnitude;
    }

    public void setEndMagnitude(String endMagnitude) {
        this.endMagnitude = getDoubleFromString(endMagnitude);
    }

    public Integer getStartDepth() {
        return startDepth;
    }
    public String getStartDepthString() {
        if(startDepth == null){
            return "";
        }
        return String.valueOf(startDepth);
    }


    public void setStartDepth(Integer startDepth) {
        this.startDepth = startDepth;
    }

    public void setStartDepth(String startDepth) {
        this.startDepth = getIntegerFromString(startDepth);
    }

    public Integer getEndDepth() {
        return endDepth;
    }
    public String getEndDepthString() {
        if(endDepth == null){
            return "";
        }
        return String.valueOf(endDepth);
    }


    public void setEndDepth(Integer endDepth) {
        this.endDepth = endDepth;
    }

    public void setEndDepth(String endDepth) {
        this.endDepth = getIntegerFromString(endDepth);
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        if(!severity.equals("Not Specified"))
        this.severity = severity;
    }

    private Double getDoubleFromString(String text){
        try{
            return Double.valueOf(text);
        }
        catch(NumberFormatException e){
            return null;
        }
    }

    private Integer getIntegerFromString(String text){
        try{
            return Integer.valueOf(text);
        }
        catch(NumberFormatException e){
            return null;
        }
    }
}
