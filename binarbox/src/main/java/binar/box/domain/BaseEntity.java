package binar.box.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by Timis Nicu Alexandru on 21-Mar-18.
 */
@MappedSuperclass
public class BaseEntity {

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @CreatedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "last_modified_by")
    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    public BaseEntity() {
    }

    public BaseEntity(String createdBy, Date createdDate, String lastModifiedBy, Date lastModifiedDate) {
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
