package pink.catty.config.define;

import java.util.List;
import pink.catty.config.define.ConfigDefine.Validator;

public enum Validators implements Validator {

  MUST_POSITIVE {
    @Override
    public void check(ConfigDefine definition, Object value) {
      if ((long) value < 0) {
        throw new ValidException(
            definition.getName() + " must be positive. actual: " + (long) value);
      }
    }
  },

  MUST_LIST {
    @Override
    public void check(ConfigDefine definition, Object value) {
      if ((value instanceof List)) {
        throw new ValidException(
            definition.getName() + " must instanceof java.util.List. actual: " + value.getClass());
      }
    }
  },

  MUST_CLASS {
    @Override
    public void check(ConfigDefine definition, Object value) {
      if ((value instanceof Class)) {
        throw new ValidException(
            definition.getName() + " must instanceof java.lang.Class. actual: " + value.getClass());
      }
    }
  },

  ;
}
