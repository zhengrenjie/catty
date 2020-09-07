/*
 * Copyright 2019 The Catty Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pink.catty.core.config.definition;

import java.util.List;
import pink.catty.core.config.definition.ConfigDefine.Validator;

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
