package bot.telegram.flashcards.models;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@Data
public class User {
    @Id
    @Column
    private long id;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<FlashcardPackage> flashcardPackageList;

    @OneToMany(mappedBy = "flashcardEducationListPK.user")
    private List<FlashcardEducationList> flashcardEducationList;

    @OneToMany(mappedBy = "flashcardRepetitionListPK.user")
    private List<FlashcardRepetitionList> flashcardRepetitionList;

    @Column
    private Long currentFlashcard;

    @Column
    private LocalDateTime startStudyTime;

    @Column
    private LocalDateTime endStudyTime;

    @Column
    private Long hardCard;

    @Column
    private Long hardestCard;

    public User(){
        this.hardCard = 0L;
        this.hardestCard = 0L;
    }

    public void addHardCard(Long increase) {
        this.hardCard += increase;
    }

    public void addHardestCard(Long increase) {
        this.hardestCard += increase;
    }

    public void setZeroForCards(){
        this.hardCard = 0L;
        this.hardestCard = 0L;
    }

    @Builder
    private static User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
