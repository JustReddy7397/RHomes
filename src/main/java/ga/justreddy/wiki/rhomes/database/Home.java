package ga.justreddy.wiki.rhomes.database;

import lombok.Data;

@Data
public class Home {

  private final String name;
  private final String displayName;
  private final String uuid;
  private final String location;
  private final boolean _private;
  private final long created;

  public boolean isPrivate() {
    return _private;
  }

}
