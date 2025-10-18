package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowAllPackagesService {

    private FlashcardPackageRepository flashcardPackageRepository;


//    get list of packages
    public List<FlashcardPackage> getListOfPackages() throws NoSuchElementException {
        return (List<FlashcardPackage>) flashcardPackageRepository.findAll();
    }

//    show all packages for users in list of buttons of packages
    public SendMessage getAllPackages(Long chatId) {
        List<FlashcardPackage> flashcardPackageList = getListOfPackages();

        return SendMessage.builder()
                .chatId(chatId)
                .text("Choose package:")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(flashcardPackageList
                        .stream()
                        .map(flashcardPackage -> List.of(InlineKeyboardButton.builder()
                                .callbackData("SHOW_ALL_PACKAGES_%d_SELECTED".formatted(flashcardPackage.getId()))
                                .text(flashcardPackage.getTitle()).build())).collect(Collectors.toList())).build())
                                .build();
    }

//    show chosen package
    public EditMessageText showPackage(long packageId,int messageId, long chatId) {
        FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(packageId).orElseThrow();

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(String.format("%s                Number of cards: %d\n\n%s",
                        flashcardPackage.getTitle(),
                        flashcardPackage.getFlashcardList().size(),
                        flashcardPackage.getDescription()))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(List.of(InlineKeyboardButton.builder()
                                .text("Start education")
                                .callbackData("FLASHCARD_PACKAGE_%d_SELECTED".formatted(packageId))
                                .build()),
                                List.of(InlineKeyboardButton.builder()
                                .text("Show first card of package")
                                .callbackData("FIRST_CARD_%d_OF_PACKAGE_%d_CLICKED".formatted(packageId,0))
                                .build())))
                        .build())
                .build();
    }


    //    get list of cards
    public List<Flashcard> getAllCardsOfPackage(long packageId) {
        FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(packageId).orElseThrow();

        return new ArrayList<>(flashcardPackage.getFlashcardList());
    }

    //    show next or previous card of chosen package
    public EditMessageText getPreviousOrNextCard(long packageId, int index,int messageId, long chatId) {
        List<Flashcard> allCards = getAllCardsOfPackage(packageId);

        if (allCards.isEmpty()) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("Flashcard package is empty")
                    .build();
        }

        Flashcard flashcard = allCards.get(index);

        int currentCardNumber = allCards.indexOf(flashcard) + 1;

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder markupBuilder = InlineKeyboardMarkup.builder();
        List<InlineKeyboardButton> row = new ArrayList<>();

//        check if user is not on first card, if user see first card - this button will not be shown
        if(index > 0)
            row.add(createButtonForCards(packageId, index - 1, "PREVIOUS_CARD_%d_OF_PACKAGE_%d_CLICKED", "Previous"));

//        check if user is not on first card or last card, if user see first card or last card - this button will not be shown
        if(index != 0 && index != allCards.size() - 1 )
            row.add(createButtonForCards(packageId, 0, "SHOW_ALL_PACKAGES_%d_SELECTED", "Back to description"));


//        check if user is not on last, if user see last card - this button will not be shown
        if(index < allCards.size() - 1)
            row.add(createButtonForCards(packageId, index + 1, "NEXT_CARD_%d_OF_PACKAGE_%d_CLICKED", "Next"));

        markupBuilder.keyboardRow(row);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(String.format("Card: %d \n\nQuestion:\n%s\n\nAnswer:\n%s",
                        currentCardNumber,
                        flashcard.getQuestion(),
                        flashcard.getAnswer()))
                .replyMarkup(markupBuilder.build())
                .build();
    }

//    create button for cards
    private InlineKeyboardButton createButtonForCards(long packageId, int index, String callbackData, String text) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData.formatted(packageId, index))
                .build();
    }
}
