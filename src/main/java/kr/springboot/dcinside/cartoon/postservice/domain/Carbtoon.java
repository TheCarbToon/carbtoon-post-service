package kr.springboot.dcinside.cartoon.postservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Carbtoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String carbtoonist;

    private String memo;

    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Carbtoon(String title, String carbtoonist, String memo, Boolean active) {
        this.title = title;
        this.carbtoonist = carbtoonist;
        this.memo = memo;
        this.active = active;
    }
}
