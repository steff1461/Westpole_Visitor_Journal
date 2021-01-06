package be.technobel.westpole_visitor_journal.model.dto;

public class StoredVisitorDto {

    private Integer id;
    private String fName;
    private String lName;
    private String lPlate;
    private String companyName;

    public StoredVisitorDto() {

    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getlPlate() {
        return lPlate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public StoredVisitorDto setfName(String fName) {
        this.fName = fName;
        return this;
    }

    public StoredVisitorDto setlName(String lName) {
        this.lName = lName;
        return this;
    }

    public StoredVisitorDto setlPlate(String lPlate) {

        this.lPlate = lPlate;
        return this;
    }

    public StoredVisitorDto setCompanyName(String companyName) {

        this.companyName = companyName;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public StoredVisitorDto setId(Integer id) {
        this.id = id;
        return this;
    }
}
