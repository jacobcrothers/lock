package binar.box.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity(name = "video")
public class Video extends BaseEntity {

    @Column
    private String streamingURL;

    @Column
    private Date uploadExpirationDate;

    @Column
    private boolean uploadedSourceFile;

    @Column
    private Date uploadTime;

    @Column
    private String thumbnailURL;

    @Column
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lock_id")
    private Lock lock;
}
