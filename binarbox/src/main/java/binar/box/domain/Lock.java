package binar.box.domain;

import javax.persistence.*;

/**
 * Created by Timis Nicu Alexandru on 16-Apr-18.
 */
@Entity
@Table(name = "lock_entity")
public class Lock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "message")
    private String message;

    @Column(name = "font_size")
    private int fontSize;

    @Column(name = "font_style")
    private String fontStyle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lock_section_id")
    private LockSection lockSection;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lock_type_id")
    private LockType lockType;

    public Lock() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public LockSection getLockSection() {
        return lockSection;
    }

    public void setLockSection(LockSection lockSection) {
        this.lockSection = lockSection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LockType getLockType() {
        return lockType;
    }

    public void setLockType(LockType lockType) {
        this.lockType = lockType;
    }
}
