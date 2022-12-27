package vn.edu.fpt.document.config.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.edu.fpt.document.dto.event.ModifyMembersToWorkspaceEvent;
import vn.edu.fpt.document.exception.BusinessException;
import vn.edu.fpt.document.service.DocumentService;

@Service
@RequiredArgsConstructor
public class ModifyMembersToDocumentConsumer extends Consumer{
    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(id = "modifyMembersToDocumentConsumer", topics = "flab.workspace.add-member-to-workspace", groupId = "document_group")
    protected void listen(String value, String topic, String key) {
        super.listen(value, topic, key);
        try {
            ModifyMembersToWorkspaceEvent event = objectMapper.readValue(value, ModifyMembersToWorkspaceEvent.class);
            documentService.modifyMembersToWorkspace(event);
        }catch (Exception ex){
            throw new BusinessException("Can't modify members to document from event");
        }

    }
}
