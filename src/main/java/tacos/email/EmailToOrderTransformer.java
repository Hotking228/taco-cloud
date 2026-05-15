package tacos.email;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import tacos.entity.Ingredient;
import tacos.entity.Taco;
import tacos.repository.IngredientRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailToOrderTransformer
    extends AbstractMailMessageTransformer<EmailOrder> {

    private final IngredientRepository ingredientRepo;

    private static Logger log =
            LoggerFactory.getLogger(EmailToOrderTransformer.class);

    private static final String SUBJECT_KEYWORDS = "TACO ORDER";

    @Override
    protected AbstractIntegrationMessageBuilder<EmailOrder> doTransform(Message mailMessage) {
        EmailOrder tacoOrder = processPayload(mailMessage);
        return MessageBuilder.withPayload(tacoOrder);
    }

    private EmailOrder processPayload(Message mailMessage){
        try {
            String subject = mailMessage.getSubject();
            if(subject.toUpperCase().contains(SUBJECT_KEYWORDS)){
                String email = ((InternetAddress)mailMessage.getFrom()[0]).getAddress();
                String content = getTextFromMessage(mailMessage);
                return parseEmailToOrder(email, content);
            }
        } catch (MessagingException e){
            log.error("Messaging exception: {}", e);
        } catch (IOException e) {
            log.error("Messaging exception: {}", e);
        }

        return null;
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        Object content = message.getContent();

        if(content instanceof String){
            return (String) content;
        }

        if(content instanceof MimeMultipart){
            MimeMultipart multipart = (MimeMultipart) content;
            return getTextFromMultipart(multipart);
        }

        return content.toString();
    }

    private String getTextFromMultipart(MimeMultipart multipart) throws MessagingException, IOException {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < multipart.getCount(); i++){
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType().toLowerCase();

            if(contentType.contains("text/plain")){
                return bodyPart.getContent().toString();
            }

            if(bodyPart.getContent() instanceof MimeMultipart){
                String text = getTextFromMultipart((MimeMultipart) bodyPart.getContent());
                if(text != null && !text.isEmpty()){
                     return text;
                }
            }
        }

        return "No text found";
    }

    private EmailOrder parseEmailToOrder(String email, String content){
        EmailOrder order = new EmailOrder(email, new ArrayList<>());
        String[]lines = content.split("\\r?\\n");
        for(String line : lines){
            if(line.trim().length() > 0 && line.contains(":")){
                String[] lineSplit = line.split(":");
                String tacoName = lineSplit[0].trim();
                String ingredients = lineSplit[1].trim();
                String[]ingredientsSplit = ingredients.split(",");
                List<String> ingredientCodes = new ArrayList<>();
                for(String ingredientName : ingredientsSplit){
                    String code = lookupIngredientCode(ingredientName.trim());
                    if(code != null){
                        ingredientCodes.add(code);
                    }
                }


                Taco taco = Taco.builder()
                        .name(tacoName)
                        .ingredients(ingredientCodes.stream()
                                .map(i -> {
                                    Optional<Ingredient> ingredient = ingredientRepo.findById(i);
                                    return ingredient.get();
                                })
                                .toList())
                        .build();
                order.addTaco(taco);
            }
        }

        System.out.println(order);
        return order;
    }

    private String lookupIngredientCode(String ingredientName){
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();

        for(Ingredient ingredient : ingredients){
            String ucIngredientName = ingredientName.toUpperCase();
            if(LevenshteinDetailedDistance.getDefaultInstance()
                    .apply(ucIngredientName, ingredient.getName()).getDistance() < 3 ||
               ucIngredientName.contains(ingredient.getName()) ||
               ingredient.getName().contains(ucIngredientName)) {
                return ingredient.getId();
            }
        }

        return null;
    }
}
