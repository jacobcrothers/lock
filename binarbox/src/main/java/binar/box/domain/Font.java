package binar.box.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name="font")
public class Font extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lockTemplate_id")
    private LockTemplate lockTemplate;

    @Column(name = "font_size")
    private Integer fontSize;

    @Column(name = "font_style")
    private String fontStyle;

    @Column(name = "font_color")
    private String fontColor;
}
