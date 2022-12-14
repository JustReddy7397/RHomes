package ga.justreddy.wiki.rhomes.database;

import ga.justreddy.wiki.rhomes.utils.Cuboid;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Home {

  @Getter
  @Setter
  private String name;
  @Getter
  @Setter
  private String displayName;
  @Getter
  private final String uuid;
  @Getter
  private final String location;
  private boolean _private;
  @Getter
  private final long created;
  @Getter
  @Setter
  private Cuboid claimArea;

  public Home(String name, String displayName, String uuid, String location, boolean _private, long created) {
    this.name = name;
    this.displayName = displayName;
    this.uuid = uuid;
    this.location = location;
    this._private = _private;
    this.created = created;
  }

  public boolean isPrivate() {
    return _private;
  }

  public void setPrivate(boolean _private) {
    this._private = _private;
  }

}
