package pink.catty.core.extension.spi;

import pink.catty.core.meta.MetaInfo;

/**
 * Extension to define how serialize MetaInfo to a string. MetaInfo will final register to Registry,
 * so the serialization of MetaInfo needs to be a customizable process.
 * <p>
 * Of cause there are a few off the shelf options for users to choose, like PATH-style serialization
 * and json serialization(default).
 */
@SPI(scope = Scope.SINGLETON)
public interface MetaSerializer {

  <T extends MetaInfo> String serialize(T meta);

  MetaInfo deserialize(String string);

  /**
   * serialize & deserialize method will pass each field of MetaInfo to this method when serializing
   * to determine whether should to handle this field. By default, static field and transient field
   * will not be passed to this method which treated as "false" silently.
   *
   * @param field MetaInfo's field
   * @return true if need serialize
   */
  default boolean fieldFilter(String field) {
    return true;
  }

}
