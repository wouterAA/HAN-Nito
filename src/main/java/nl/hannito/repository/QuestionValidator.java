package nl.hannito.repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import nl.hannito.model.MultipleChoiceQuestion;
import nl.hannito.model.OpenQuestion;
import nl.hannito.model.Question;
import nl.hannito.model.TheoremQuestion;
import nl.hannito.model.YesNoQuestion;

public class QuestionValidator {
    /**
     * Validates a question according to the bean validation
     * @param jsonQuestion The question to validate
     * @return The validated question
     */
    public static String validate(String jsonQuestion) {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            JsonObject object = jsonParser.parse(jsonQuestion).getAsJsonObject();
            
            String questionType = object.get("question_type").getAsString();
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            
            switch(questionType.toLowerCase()){
                case "openquestion":
                    OpenQuestion openquestion = gson.fromJson(object, OpenQuestion.class);
                    Set<ConstraintViolation<OpenQuestion>> openquestionviolations = validator.validate(openquestion);
                    if(!openquestionviolations.isEmpty()) {
                        throw new ValidationException();
                    }
                    return gson.toJson(openquestion);
                case "multiplechoicequestion":
                    MultipleChoiceQuestion multiplechoicequestion = gson.fromJson(object, MultipleChoiceQuestion.class);
                    System.out.println(multiplechoicequestion.toString());
                    Set<ConstraintViolation<MultipleChoiceQuestion>> multiplechoicequestionviolations = validator.validate(multiplechoicequestion);
                    if(!multiplechoicequestionviolations.isEmpty()) {
                        throw new ValidationException();
                    }
                    return gson.toJson(multiplechoicequestion);
                case "yesnoquestion":
                    YesNoQuestion yesnoquestion = gson.fromJson(object, YesNoQuestion.class);
                    Set<ConstraintViolation<YesNoQuestion>> yesnoquestionviolations = validator.validate(yesnoquestion);
                    if(!yesnoquestionviolations.isEmpty()) {
                        throw new ValidationException();
                    }
                    return gson.toJson(yesnoquestion);
                case "theoremquestion":
                    TheoremQuestion theoremquestion = gson.fromJson(object, TheoremQuestion.class);
                    Set<ConstraintViolation<TheoremQuestion>> theoremquestionviolations = validator.validate(theoremquestion);
                    if(!theoremquestionviolations.isEmpty()) {
                        throw new ValidationException();
                    }
                    return gson.toJson(theoremquestion);
                default: 
                    throw new ValidationException("This question type does not exist.");
            }
    }
    
    /**
     * Parses a json string to the appropriate Question object
     * @param jsonQuestion The json question string to parse
     * @return Question object from the json string
     */
    public static Question jsonToQuestion(String jsonQuestion) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject object = jsonParser.parse(jsonQuestion).getAsJsonObject();            
        String questionType = object.get("question_type").getAsString();
        switch(questionType.toLowerCase()){
            case "openquestion":
                return gson.fromJson(object, OpenQuestion.class);
            case "multiplechoicequestion":
                return gson.fromJson(object, MultipleChoiceQuestion.class);
            case "yesnoquestion":
                return gson.fromJson(object, YesNoQuestion.class);
            case "theoremquestion":
                return gson.fromJson(object, TheoremQuestion.class);
            default: 
                throw new ValidationException("This question type does not exist.");
        }
    }
}
