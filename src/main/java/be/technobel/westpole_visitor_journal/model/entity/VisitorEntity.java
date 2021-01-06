package be.technobel.westpole_visitor_journal.model.entity;

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
@Table(name = "visitors")

public class VisitorEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @JoinColumn(name="sv_id")
    @OneToOne(targetEntity = StoredVisitorEntity.class,cascade = CascadeType.ALL)
    private StoredVisitorEntity svEntity;


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

    public StoredVisitorEntity getSvEntity() {
        return svEntity;
    }

    public VisitorEntity setSvEntity(StoredVisitorEntity svEntity) {
        this.svEntity = svEntity;
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
