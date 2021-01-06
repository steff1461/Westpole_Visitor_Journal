package be.technobel.westpole_visitor_journal.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class VisitorDto {

    private int id;
    private String fName;
    private String lName;
    private String lPlate;
    private String companyName;
    private String contactName;
    private String curDate;
    private String inTime;
    private String outTime;

    public VisitorDto(int id, String fName, String lName, String lPlate, String companyName, String contactName,
                      LocalDate curDate, LocalTime inTime, LocalTime outTime) {


        setId(id);
        setfName(fName);
        setlName(lName);
        setlPlate(lPlate);
        setCompanyName(companyName);
        setContactName(contactName);
        setCurDate(curDate);
        setInTime(inTime);
        setOutTime(outTime);
    }

    public VisitorDto() {

    }

    public int getId() {
        return id;
    }

    public VisitorDto setId(int id) {

        this.id = id;
        return this;
    }

    public String getfName() {
        return fName;
    }

    public VisitorDto setfName(String fName) {

        this.fName = fName;
        return this;
    }

    public String getlName() {
        return lName;
    }

    public VisitorDto setlName(String lName) {

        this.lName = lName;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public VisitorDto setCompanyName(String companyName) {

        this.companyName = companyName;
        return this;
    }

    public String getContactName() {
        return contactName;
    }

    public VisitorDto setContactName(String contactName) {

        if (contactName != null) this.contactName = contactName;
        else this.contactName = "NONE";
        return this;
    }

    public String getCurDate() {
        return curDate;
    }

    public VisitorDto setCurDate(LocalDate curDate) {

        this.curDate = curDate.toString();
        return this;
    }

    public String getlPlate() {
        return lPlate;
    }

    public VisitorDto setlPlate(String lPlate) {

        this.lPlate = lPlate;
        return this;
    }

    public String getInTime() {
        return inTime;
    }

    public VisitorDto setInTime(LocalTime inTime) {

        this.inTime = inTime.toString();
        return this;
    }

    public String getOutTime() {
        return outTime;
    }

    public VisitorDto setOutTime(LocalTime outTime) {

        if (outTime != null) this.outTime = outTime.toString();
        else this.outTime = "NONE";

        return this;
    }

}
