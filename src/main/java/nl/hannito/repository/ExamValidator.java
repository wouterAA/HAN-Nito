package nl.hannito.repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import nl.hannito.model.Exam;
import nl.hannito.model.Question;

/**
 * Validator of the exams
 * 
 */
public class ExamValidator {
    /**
     * Validates the exam according to the set bean validation constraints
     * @param jsonExam The exam in json format
     * @return The exam as an Exam Object
     */
    public static Exam validate(String jsonExam) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject object = jsonParser.parse(jsonExam).getAsJsonObject();
        List<Question> questions = new ArrayList<Question>();
        if(object.has("questions")) {
            Iterator questionsIterator = object.get("questions").getAsJsonArray().iterator();
            while(questionsIterator.hasNext()) {
                String jsonQuestion = questionsIterator.next().toString();
                questions.add(QuestionValidator.jsonToQuestion(jsonQuestion));
            }
            object.get("questions").getAsJsonArray().size();
        }
        String owner = "";
        if(object.has("owner")) {
            owner = object.get("owner").getAsString();
        }
        
        Exam exam = new Exam(owner, object.get("subject").getAsString(), object.get("instruction").getAsString(), questions, null);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Set<ConstraintViolation<Exam>> examViolations = validator.validate(exam);
        
        if (!examViolations.isEmpty()) {
            throw new ValidationException();
        }
        return exam;
    }
}
