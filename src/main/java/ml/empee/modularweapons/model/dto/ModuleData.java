package ml.empee.modularweapons.model.dto;

import com.google.gson.JsonParseException;
import lombok.Data;
import ml.empee.json.validator.annotations.Required;
import ml.empee.json.validator.annotations.Validator;

@Data
public class ModuleData {

  @Required
  private String id;
  @Required
  private String category;

  @Validator
  private void validateId() {
    if(id.contains(";")) {
      throw new JsonParseException("Module id '" + id + "' can't contain a ';'");
    }
  }

}
