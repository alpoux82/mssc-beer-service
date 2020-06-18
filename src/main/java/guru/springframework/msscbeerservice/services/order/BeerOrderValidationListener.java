package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.events.ValidateOrderRequest;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static guru.springframework.msscbeerservice.config.JmsConfig.VALIDATE_ORDER_QUEUE;
import static guru.springframework.msscbeerservice.config.JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE;

@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final BeerOrderValidator beerOrderValidator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest validateOrderRequest) {
        Boolean isValid = beerOrderValidator.validateOrder(validateOrderRequest.getBeerOrderDto());
        jmsTemplate.convertAndSend(VALIDATE_ORDER_RESPONSE_QUEUE,
                ValidateOrderResult.builder()
                        .isValid(isValid)
                        .orderId(validateOrderRequest.getBeerOrderDto().getId())
                        .build());
    }
}
