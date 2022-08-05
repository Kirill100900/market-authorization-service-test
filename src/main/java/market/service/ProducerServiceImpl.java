package market.service;
import market.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public void produce(Message message) {
        System.out.println("Producing the message: " + message);
        kafkaTemplate.send("messages", message);
    }
}
