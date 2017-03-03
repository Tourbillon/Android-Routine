package com.anbillon.routine;

import java.util.List;

/**
 * A concrete filter chain that carries the entire filter chain.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class FilterChain implements Filter.Chain {
  private final List<Filter> filters;
  private final Matcher matcher;
  private final int index;
  private int calls;

  FilterChain(List<Filter> filters, int index, Matcher matcher) {
    this.filters = filters;
    this.index = index;
    this.matcher = matcher;
  }

  @Override public Matcher matcher() {
    return matcher;
  }

  @Override public Matcher proceed(Matcher matcher) {
    if (matcher == null) {
      throw new IllegalArgumentException("Matcher in filter can not be null.");
    }

    if (index == 0 && filters.size() == 0 || index >= filters.size()) {
      return matcher;
    }

    calls++;

    /* call the next filter in the chain */
    FilterChain next = new FilterChain(filters, index + 1, matcher);
    Filter filter = filters.get(index);
    Matcher result = filter.filter(next);

    /* confirm that the next filter made its required call to chain.proceed() */
    if (index + 1 < filters.size() && next.calls != 1) {
      throw new IllegalStateException(
          "Routine interceptor " + filter + " must call proceed() exactly once.");
    }

    return result;
  }
}
