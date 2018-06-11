package binar.box.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@Entity
@Table(name = "panel_entity")
public class Panel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "panel")
    @Column(name = "panel_lock_entity")
    private List<Lock> locks;

    public Panel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Lock> getLocks() {
        return locks;
    }

    public void setLocks(List<Lock> locks) {
        this.locks = locks;
    }
}
