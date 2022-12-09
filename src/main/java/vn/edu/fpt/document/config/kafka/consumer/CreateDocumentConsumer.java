package vn.edu.fpt.document.config.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.edu.fpt.document.dto.event.CreateDocumentEvent;
import vn.edu.fpt.document.exception.BusinessException;
import vn.edu.fpt.document.service.DocumentService;


@Service
@RequiredArgsConstructor
public class CreateDocumentConsumer extends Consumer{

    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(id = "createDocumentConsumer", topics = "flab.profile.create-profile", groupId = "profile_group")
    protected void listen(String value, String topic, String key) {
        super.listen(value, topic, key);
        try {
            CreateDocumentEvent event = objectMapper.readValue(value, CreateDocumentEvent.class);
            documentService.createDocument(event);
        }catch (Exception ex){
            throw new BusinessException("Can't create profile from event");
        }

    }
}
