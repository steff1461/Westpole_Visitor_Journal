package be.technobel.westpole_visitor_journal.repository.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter

@Entity
@Table(name = "stored_visitors")

public class StoredVisitorEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(min = 2, max = 15)
    @NotNull
    @Column(length = 15, name = "sv_fname", nullable = false)
    private String fName;

    @Size(min = 2, max = 40)
    @NotNull
    @Column(length = 40, name = "sv_lname", nullable = false)
    private String lName;


    @Size(min=4, max = 10)
    @NotNull
    @Column(length = 10, name = "sv_lplate", nullable = false)
    private String lPLate;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(length = 40, name = "sv_comp", nullable = false)
    private String companyName;

    public Integer getId() {
        return id;
    }

    public StoredVisitorEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getfName() {
        return fName;
    }

    public StoredVisitorEntity setfName(String fName) {
        this.fName = fName;
        return this;
    }

    public String getlName() {
        return lName;
    }

    public StoredVisitorEntity setlName(String lName) {
        this.lName = lName;
        return this;
    }

    public String getlPLate() {
        return lPLate;
    }

    public StoredVisitorEntity setlPLate(String lPLate) {
        this.lPLate = lPLate;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public StoredVisitorEntity setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }
}
