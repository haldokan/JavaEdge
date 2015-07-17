package org.haldokan.edge.guava;

public interface ICompletionListener {
    void onSuccess(String s);

    void onFailure(Exception e);
}
