package be.technobel.westpole_visitor_journal.repository;


import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter

@Entity
@Table(name = "VISITORS")

public class VisitorEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 15)
    @NotNull
    @Column(length = 15, name = "v_fname", nullable = false)
    private String fName;

    @Size(min = 2, max = 40)
    @NotNull
    @Column(length = 40, name = "v_lname", nullable = false)
    private String lName;

    @Size(max = 10)
    @Column(length = 10, name = "v_lplate")
    private String lPLate;

    @Size(max = 40)
    @Column(length = 40, name = "v_comp")
    private String companyName;

    @Size(max = 60)
    @Column(length = 60, name = "iris_pname")
    private String contactName;

    @NotNull
    @Column(name = "v_date", nullable = false)
    private LocalDate curDate;

    @NotNull
    @Type(type = "org.hibernate.type.LocalTimeType")
    @Column(name = "v_in", nullable = false)
    private LocalTime inTime;

    @Type(type = "org.hibernate.type.LocalTimeType")
    @Column(name = "v_out")
    private LocalTime outTime;

    public Integer getId() {
        return id;
    }

    public VisitorEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getfName() {
        return fName;
    }

    public VisitorEntity setfName(String fName) {
        this.fName = fName;
        return this;
    }

    public String getlName() {
        return lName;
    }

    public VisitorEntity setlName(String lName) {
        this.lName = lName;
        return this;
    }

    public String getlPLate() {
        return lPLate;
    }

    public VisitorEntity setlPLate(String lPLate) {
        this.lPLate = lPLate;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public VisitorEntity setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getContactName() {
        return contactName;
    }

    public VisitorEntity setContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public LocalDate getCurDate() {
        return curDate;
    }

    public VisitorEntity setCurDate(LocalDate curDate) {
        this.curDate = curDate;
        return this;
    }

    public LocalTime getInTime() {
        return inTime;
    }

    public VisitorEntity setInTime(LocalTime inTime) {
        this.inTime = inTime;
        return this;
    }

    public LocalTime getOutTime() {
        return outTime;
    }

    public VisitorEntity setOutTime(LocalTime outTime) {
        this.outTime = outTime;
        return this;
    }
}
