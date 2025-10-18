package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import bot.telegram.flashcards.models.temporary.FlashcardStatus;
import bot.telegram.flashcards.repository.FlashcardEducationListRepository;
import bot.telegram.flashcards.repository.FlashcardRepetitionListRepository;
import bot.telegram.flashcards.repository.FlashcardStatusRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class EducationService {
    private final FlashcardEducationListRepository flashcardEducationListRepository;
    private final FlashcardRepetitionListRepository flashcardRepetitionListRepository;
    private final FlashcardStatusRepository flashcardStatusRepository;

    private final UserService userService;
    private final FlashcardService flashcardService;

    public FlashcardPackage getFlashcardPackage(long packageId) throws NoSuchElementException {
// TODO: add ifPresent() or isPresent() for this code, 'cause tests show NoSuchElementException
        return flashcardService.getFlashcardPackage(packageId);
    }

    public FlashcardEducationList getFlashcardEducationList(long id, User user) {
        return flashcardEducationListRepository.findById(
                new FlashcardEducationList.FlashcardEducationListPK(id, user)).orElseThrow();
    }

    public List<FlashcardPackage> getFlashcardPackageListByUser(long chatId) throws NoSuchElementException {
        try {
            User user = userService.getUser(chatId);

            return user.getFlashcardPackageList();
        }catch (Exception e){
            log.error("Cannot get flashcard package list", e);
            return List.of();
        }
    }

    public EditMessageText generateFlashcardList(long flashcardPackageId, long chatId, int messageId) {
        try {
            FlashcardPackage flashcardPackage = flashcardService.getFlashcardPackage(flashcardPackageId);
            List<Flashcard> flashcardList = flashcardPackage.getFlashcardList();
            User user = userService.getUser(chatId);

            Collections.shuffle(flashcardList);

            List<FlashcardEducationList> flashcardEducationList = new ArrayList<>();
            for (int i = 0; i < flashcardList.size(); i++) {
                flashcardEducationList.add(
                        new FlashcardEducationList(
                                new FlashcardEducationList.FlashcardEducationListPK(i + 1, user),
                                flashcardList.get(i)
                        )
                );
            }

            flashcardEducationListRepository.saveAll(flashcardEducationList);
            user.setCurrentFlashcard(1L);
            userService.save(user);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("Flashcard 1/" + flashcardList.size() + "\n\nQuestion:\n" + flashcardList.get(0).getQuestion())
                    .replyMarkup(new InlineKeyboardMarkup(List.of(List.of(
                            InlineKeyboardButton.builder()
                                    .callbackData("SHOW_ANSWER_CLICKED").text("Show answer").build()))))
                    .build();
            return editMessage;
        }catch (Exception e){
            log.error("Cannot generate flashcard list", e);
            return null;
        }
    }


    public EditMessageText changeMsgToMsgWithShownAnswer(long chatId, int messageId) {
        User user = userService.getUser(chatId);
        long numberOfFlashcards = flashcardEducationListRepository.
                countFlashcardEducationListByFlashcardEducationListPK_User(user);
        FlashcardEducationList flashcardEducationList =
                getFlashcardEducationList(user.getCurrentFlashcard(), user);
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();

        EditMessageText messageWithShownAnswer = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder().text("Idk").callbackData("0%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("25%").callbackData("25%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("50%").callbackData("50%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("75%").callbackData("75%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("Easy").callbackData("100%_BUTTON_CLICKED").build()))
                        .build())
                .text("Flashcard " + flashcardEducationList.getFlashcardEducationListPK().getId()
                        + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion() +
                        "\n\nAnswer:\n" + currentFlashcard.getAnswer())
                .build();

        return messageWithShownAnswer;
    }

    public EditMessageText nextRepetitionFlashcard(long chatId, int messageId) {
        User user = userService.getUser(chatId);
        return nextRepetitionFlashcard(chatId, messageId, user.getCurrentFlashcard());
    }

    public EditMessageText nextRepetitionFlashcard(long chatId, int messageId, long currentFlashcardId) {
        User user = userService.getUser(chatId);

        user.setCurrentFlashcard(currentFlashcardId + 1);
        userService.save(user);

        long numberOfFlashcards = flashcardRepetitionListRepository.
                countFlashcardRepetitionListByFlashcardRepetitionListPK_User(user);

        if (user.getCurrentFlashcard() > numberOfFlashcards) {
            FlashcardRepetitionList firstCard = flashcardRepetitionListRepository.findAllByFlashcardRepetitionListPK_User(user).get(0);
            long packageId = firstCard.getFlashcard().getFlashcardPackage().getId();
            clearTemporaryResourcesAfterEducation(chatId);
            return createCongratulationMessage(chatId, messageId, packageId);
        }

        FlashcardRepetitionList flashcardRepetitionList = flashcardRepetitionListRepository.findById(
                new FlashcardRepetitionList.FlashcardRepetitionListPK(user.getCurrentFlashcard(), user)).orElseThrow();
        Flashcard currentFlashcard = flashcardRepetitionList.getFlashcard();

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Flashcard (repetition) " +
                        flashcardRepetitionList.getFlashcardRepetitionListPK().getId() +
                        "/" + numberOfFlashcards + "\n\nQuestion:\n" + currentFlashcard.getQuestion())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .callbackData("SHOW_ANSWER_REPETITION_CLICKED").text("Show answer").build())).build())
                .build();

        return editMessage;
    }

    public void clearTemporaryResourcesAfterEducation(long chatId) {
        User user = userService.getUser(chatId);
        flashcardEducationListRepository.deleteAllByFlashcardEducationListPK_User(user);
        flashcardRepetitionListRepository.deleteAllByFlashcardRepetitionListPK_User(user);
        flashcardStatusRepository.deleteAllByFlashcardStatusPK_User(user);

        user.setCurrentFlashcard(null);
        userService.save(user);
    }

    public EditMessageText createCongratulationMessage(long chatId, int messageId, long packageId) {

        User user = userService.getUser(chatId);
        user.setEndStudyTime(LocalDateTime.now());

        Duration studyTime = Duration.between(user.getStartStudyTime(), user.getEndStudyTime());

        long hardest =  user.getHardestCard();

        long hard = user.getHardCard();

        userService.save(user);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .parseMode(ParseMode.MARKDOWN)
                .text(String.format("""
                            *Congratulations!*\s
                                                            
                            You have completed all flashcards in the package\s
                                                            
                            Statistics:\s
                            Study time: %02d:%02d:%02d  Hardest card: %d  Hard card: %d\s
                            
                            You can choose another package for education with command:
                            /starteducation
                            
                            OR
                            
                            You can restart the learning of a completed package below
                            """,
                        studyTime.toHoursPart(), studyTime.toMinutesPart(), studyTime.toSecondsPart(), hardest, hard))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .callbackData("FLASHCARD_PACKAGE_%d_SELECTED".formatted(packageId))
                                .text("Learn again")
                                .build()))
                        .build())
                .build();
    }
    public EditMessageText nextFlashcard(long chatId, int messageId) {
        User user = userService.getUser(chatId);

        user.setCurrentFlashcard(user.getCurrentFlashcard() + 1);
        userService.save(user);

        long numberOfFlashcards = flashcardEducationListRepository.
                countFlashcardEducationListByFlashcardEducationListPK_User(user);

        if (user.getCurrentFlashcard() > numberOfFlashcards) {
            return nextRepetitionFlashcard(chatId, messageId, 0);
        }

        FlashcardEducationList flashcardEducationList =
                getFlashcardEducationList(user.getCurrentFlashcard(), user);
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Flashcard " + flashcardEducationList.getFlashcardEducationListPK().getId()
                        + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .callbackData("SHOW_ANSWER_CLICKED").text("Show answer").build())).build())
                .build();

        return editMessage;
    }

    public void moveFlashcardToRepetitionList(long chatId) {
        User user = userService.getUser(chatId);
        Flashcard currentFlashcard =
                getFlashcardEducationList(user.getCurrentFlashcard(), user).getFlashcard();
        if (flashcardRepetitionListRepository.findAllByFlashcard(currentFlashcard).isEmpty()) {
            FlashcardRepetitionList flashcardRepetitionList =
                    new FlashcardRepetitionList(new FlashcardRepetitionList.FlashcardRepetitionListPK(
                            getAvailableIdForRepetitionList(chatId), user), currentFlashcard);
            flashcardRepetitionListRepository.save(flashcardRepetitionList);
        }
    }

    private long getAvailableIdForRepetitionList(long chatId) {
        List<Long> idsList = flashcardRepetitionListRepository.findIds(chatId);

        if (idsList.isEmpty()) {
            return 1L;
        } else {
            return idsList.get(0) + 1;
        }
    }

    public EditMessageText changeMsgToMsgWithShownAnswerRepetition(long chatId, int messageId) {
        User user = userService.getUser(chatId);
        long numberOfFlashcards = flashcardRepetitionListRepository.
                countFlashcardRepetitionListByFlashcardRepetitionListPK_User(user);
        FlashcardRepetitionList flashcardRepetitionList = flashcardRepetitionListRepository.findById(
                new FlashcardRepetitionList.FlashcardRepetitionListPK(user.getCurrentFlashcard(), user)).orElseThrow();
        Flashcard currentFlashcard = flashcardRepetitionList.getFlashcard();

        EditMessageText messageWithShownAnswer = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder().text("Next Question").callbackData("NEXT_QUESTION_REPETITION_CLICKED").build()))
                        .build())
                .text("Flashcard " + flashcardRepetitionList.getFlashcardRepetitionListPK().getId()
                        + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion() +
                        "\n\nAnswer:\n" + currentFlashcard.getAnswer())
                .build();

        return messageWithShownAnswer;
    }

    public void duplicateFlashcard(long chatId, int numberOfDuplicates) {
        User user = userService.getUser(chatId);
        FlashcardEducationList flashcardEducationList =
                getFlashcardEducationList(user.getCurrentFlashcard(), user);
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();


        Optional<FlashcardStatus> flashcardStatusOptional = flashcardStatusRepository
                .findById(new FlashcardStatus.FlashcardStatusPK(user, currentFlashcard)); // TODO: repair problem https://stackoverflow.com/questions/76887311/listresultsconsumer-duplicate-row-was-found-and-assert-was-specified


        if (flashcardStatusOptional.isEmpty()){
            flashcardStatusRepository.save(new FlashcardStatus(
                    new FlashcardStatus.FlashcardStatusPK(user, currentFlashcard),
                    numberOfDuplicates,
                    null)); // TODO: set to appropriate difficulty
            if(numberOfDuplicates == 1){
                user.addHardCard(1L);
            } else if (numberOfDuplicates == 2) {
                user.addHardestCard(1L);
            }
        } else {
            FlashcardStatus flashcardStatus = flashcardStatusOptional.get();
            flashcardStatus.setNumberOfDuplicatedCards(flashcardStatus.getNumberOfDuplicatedCards() - 1);

            if (numberOfDuplicates >= flashcardStatus.getNumberOfDuplicatedCards()) {
                int difference = numberOfDuplicates - flashcardStatus.getNumberOfDuplicatedCards();

                numberOfDuplicates -= flashcardStatus.getNumberOfDuplicatedCards();

                flashcardStatus.setNumberOfDuplicatedCards(flashcardStatus.getNumberOfDuplicatedCards() + difference);
                flashcardStatusRepository.save(flashcardStatus);
            }
        }

        if (numberOfDuplicates <= 0) {
            return;
        }

        long numberOfAllFlashcardsInDeck =
                flashcardEducationListRepository.countFlashcardEducationListByFlashcardEducationListPK_User(
                        user);
        long currentFlashcardId = flashcardEducationList.getFlashcardEducationListPK().getId();

        double divider = 1.0 / (numberOfDuplicates + 1);


        for (int i = 0; i < numberOfDuplicates; i++) {
            long numberOfFlashcardsAhead = numberOfAllFlashcardsInDeck - currentFlashcardId;
            long flashcardStep = (long) (numberOfFlashcardsAhead * divider) + 1; // because of "+ 1" newCoord can be out of range
            long newCoord = currentFlashcardId + flashcardStep * (i + 1);

            if (newCoord >= numberOfAllFlashcardsInDeck) {
                flashcardEducationListRepository.save(new FlashcardEducationList(
                        new FlashcardEducationList.FlashcardEducationListPK(
                                numberOfAllFlashcardsInDeck + 1, user),
                        currentFlashcard));
            } else {
                FlashcardEducationList savedFlashcardEducationList = flashcardEducationListRepository
                        .findById(new FlashcardEducationList.FlashcardEducationListPK(
                                newCoord, user)).orElseThrow();

                flashcardEducationListRepository.save(
                        new FlashcardEducationList(new FlashcardEducationList.FlashcardEducationListPK(
                                newCoord, user), currentFlashcard));
                newCoord++;
                FlashcardEducationList nextFlashcard;
                while (newCoord <= numberOfAllFlashcardsInDeck) {
                    nextFlashcard = getFlashcardEducationList(newCoord, user);
                    savedFlashcardEducationList.setFlashcardEducationListPK(
                            new FlashcardEducationList.FlashcardEducationListPK(
                                    nextFlashcard.getFlashcardEducationListPK().getId(), user));
                    flashcardEducationListRepository.save(savedFlashcardEducationList);
                    savedFlashcardEducationList = nextFlashcard;
                    newCoord++;
                }

                savedFlashcardEducationList.setFlashcardEducationListPK(
                        new FlashcardEducationList.FlashcardEducationListPK(
                                newCoord, user));
                flashcardEducationListRepository.save(savedFlashcardEducationList);
            }

            userService.save(user);

            numberOfAllFlashcardsInDeck++;
        }
    }

    public void decreaseNumberOfDuplicatesIfExists(long chatId) {
        User user = userService.getUser(chatId);
        FlashcardEducationList flashcardEducationList =
                getFlashcardEducationList(user.getCurrentFlashcard(), user);
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();
        Optional<FlashcardStatus> flashcardStatusOptional =
                flashcardStatusRepository.findById(new FlashcardStatus.FlashcardStatusPK(user, currentFlashcard));

        if (flashcardStatusOptional.isPresent()) {
            FlashcardStatus flashcardStatus = flashcardStatusOptional.get();
            flashcardStatus.setNumberOfDuplicatedCards(flashcardStatus.getNumberOfDuplicatedCards() - 1);
            flashcardStatusRepository.save(flashcardStatus);
        }
    }
}

