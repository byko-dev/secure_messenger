package pl.bykodev.messenger_api.services;

import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.UserEntity;

public interface NotificationService {

    void messageNotify(UserEntity friend, MessageEntity messageEntity);

}
