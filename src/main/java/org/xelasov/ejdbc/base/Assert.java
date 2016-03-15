package org.xelasov.ejdbc.base;

public class Assert {
  public static final void argumentNotNull(Object v) {
    if (v == null)
      throw new IllegalArgumentException("Argument can't be null");
  }

  public static final void argument(boolean v) {
    if (!v)
      throw new IllegalArgumentException();
  }

  public static final void state(boolean v) {
    if (!v)
      throw new IllegalStateException();
  }
}
