package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.ShowAllPackagesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@AllArgsConstructor
public class ShowAllPackagesController {
    private ShowAllPackagesService showAllPackagesService;

//    show all packages
    public SendMessage showAllPackagesCommandReceived(Update update){
        long chatId = update.getMessage().getChatId();
        return showAllPackagesService.getAllPackages(chatId);
    }

    //    show description of chosen package
    public EditMessageText showPackageDescription(CallbackQuery callbackQuery) {
        long flashcardPackageId = Long.parseLong(callbackQuery.getData().split("_")[3]);
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();

        return showAllPackagesService.showPackage(flashcardPackageId ,messageId, chatId);
    }

//    show previous or next card of chosen package
    public EditMessageText showPreviousOrNextCard(CallbackQuery callbackQuery) {
        long packageId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        int indexOfCard = Integer.parseInt(callbackQuery.getData().split("_")[5]);
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();

        return showAllPackagesService.getPreviousOrNextCard(packageId, indexOfCard, messageId, chatId);
    }

}
