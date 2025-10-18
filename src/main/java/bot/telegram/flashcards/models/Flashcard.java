package bot.telegram.flashcards.models;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @ManyToOne
    @JoinColumn(name = "packageId", referencedColumnName = "id")
    private FlashcardPackage flashcardPackage;

    @OneToMany(mappedBy = "flashcard")
    private List<FlashcardEducationList> flashcardEducationList;

    @OneToOne(mappedBy = "flashcard")
    private FlashcardRepetitionList flashcardRepetitionList;

    @Column
    private String question;

    @Column
    private String answer;
}
