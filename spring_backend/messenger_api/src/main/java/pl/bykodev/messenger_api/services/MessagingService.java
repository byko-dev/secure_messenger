package pl.bykodev.messenger_api.services;

import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.pojos.SendMessageRequestDTO;

public interface MessagingService {

    MessageEntity send(String conversationId, SendMessageRequestDTO request);

}