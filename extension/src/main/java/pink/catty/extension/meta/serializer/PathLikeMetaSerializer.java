package pink.catty.extension.meta.serializer;

import pink.catty.core.extension.spi.MetaSerializer;
import pink.catty.core.meta.MetaInfo;

public class PathLikeMetaSerializer implements MetaSerializer {

  @Override
  public <T extends MetaInfo> String serialize(T meta) {
    return null;
  }

  @Override
  public MetaInfo deserialize(String string) {
    return null;
  }

  @Override
  public boolean fieldFilter(String field) {
    return false;
  }
}
