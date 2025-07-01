package pl.bykodev.messenger_api.services;

import pl.bykodev.messenger_api.database.MessageEntity;

public interface AIBotService {

    void reply(MessageEntity entity, String message);

}