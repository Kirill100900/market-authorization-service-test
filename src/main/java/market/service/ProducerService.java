package market.service;

import market.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


public interface ProducerService {
    public void produce(Message message);
}
