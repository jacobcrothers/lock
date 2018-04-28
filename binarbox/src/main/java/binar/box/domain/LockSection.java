package binar.box.domain;

import javax.persistence.*;

/**
 * Created by Timis Nicu Alexandru on 16-Apr-18.
 */
@Entity
@Table(name = "lock_section")
public class LockSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "section")
    private String section;

    public LockSection() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
